package activity.complainvk;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static activity.utility.FileUtils.getPath;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import activity.CameraActivity2;
import activity.GPSTracker;
import activity.MainActivity1;
import activity.PhotoViewerActivity;
import activity.services.LocationUpdateService;
import activity.utility.Constant;
import activity.utility.CustomUtility;
import activity.utility.Utility;
import adapter.ImageSelectionAdapter;
import bean.ImageModel;
import bean.LocalConvenienceBean;
import bean.WayPoints;
import database.DatabaseHelper;
import models.DistanceResponse;
import rest.DistanceApiClient;
import rest.RestUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings({"deprecation", "resource", "StringConcatenationInLoop"})
public class SubordinateImageActivity extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener {


    LocalConvenienceBean localConvenienceBean;
    String current_start_date, current_end_date, current_start_time, current_end_time, username, from_lat,
            from_lng, to_lat, allLatLong = "", to_lng, fullAddress = null, fullAddress1 = null,
            distance1 = null, startphoto = null, totalWayPoint = "",mobile, enq_docno, cust_nm,
            mUserID,pendRemarkValue,kunnr, userid,mStatusCheck;

    private static final int REQUEST_CODE_PERMISSION = 123;
    WayPoints wayPoints;
    LocationManager locationManager;
    Context mContext;

    Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getApplicationContext(), mString, Toast.LENGTH_LONG).show();
        }
    };

    FusedLocationProviderClient fusedLocationClient;
    int selectedIndex;
    boolean isBackPressed = false, isUpdate = false;
    AlertDialog alertDialog;

    private static final int PICK_FROM_FILE = 102;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();


    DatabaseHelper dataHelper;
    double inst_latitude_double, inst_longitude_double;

    RecyclerView recyclerview;
    Toolbar mToolbar;
    ImageSelectionAdapter ImageAdapter;

    TextView txtBTNSaveID;
    EditText edtRemarkAMTID;
    ProgressDialog progressDialog;

    CheckBox borewellInCheckBox,borewellOutCheckBox,transportLoadCheckBox,transportUnloadCheckBox;

    Boolean boerwellLiffiting=false, borewellLowering=false, transportLoading=false, transportUnLoading=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_image);

        mContext = this;
        getGpsLocation();
        Init();
    }
    private void Init() {
        mUserID = activity.CustomUtility.getSharedPreferences(mContext, "userID");
        dataHelper = new DatabaseHelper(mContext);


        txtBTNSaveID = findViewById(R.id.txtBTNSaveID);
        mobile = CustomUtility.getSharedPreferences(mContext, "username");
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        kunnr = CustomUtility.getSharedPreferences(mContext, "kunnr");
        userid = CustomUtility.getSharedPreferences(mContext, "username");

        recyclerview = findViewById(R.id.recyclerview);
        edtRemarkAMTID = findViewById(R.id.edtRemarkAMTID);

        borewellInCheckBox = findViewById(R.id.borewellInCheckBox);
        borewellOutCheckBox = findViewById(R.id.borewellOutCheckBox);
        transportLoadCheckBox = findViewById(R.id.transportLoadCheckBox);
        transportUnloadCheckBox = findViewById(R.id.transportUnloadCheckBox);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        enq_docno = bundle.getString("inst_id");
        cust_nm = bundle.getString("cust_name");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint:- "+enq_docno);

        CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "");



        Log.e("Values===>", enq_docno + "  " + cust_nm);

        SetAdapter();
        listener();

    }

    private void listener() {
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        txtBTNSaveID.setOnClickListener(view -> {
            Save();
            pendRemarkValue = edtRemarkAMTID.getText().toString().trim();

            if (CustomUtility.getSharedPreferences(mContext, "AUDSYNC" + enq_docno).equalsIgnoreCase("1")) {
                if (!pendRemarkValue.isEmpty()) {
                    calculateDistance();

                } else {
                    Toast.makeText(mContext, "Enter Remark", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Select all image", Toast.LENGTH_SHORT).show();
            }
        });

        borewellInCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                boerwellLiffiting = true;
                addItemInImageList(getResources().getString(R.string.borwellInlet));
            }else {
                removeItemFromList(getResources().getString(R.string.borwellInlet));
                boerwellLiffiting = false;
            }
        });

        borewellOutCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                addItemInImageList(getResources().getString(R.string.borwellOutlet));
                borewellLowering = true;
            }else {
                removeItemFromList(getResources().getString(R.string.borwellOutlet));
                borewellLowering = false;
            }
        });

        transportLoadCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                addItemInImageList(getResources().getString(R.string.transportLoad));
                transportLoading =true;
            }else {
                removeItemFromList(getResources().getString(R.string.transportLoad));
                transportLoading =false;
            }
        });

        transportUnloadCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                addItemInImageList(getResources().getString(R.string.transportUnload));
                transportUnLoading = true;
            }else {
                removeItemFromList(getResources().getString(R.string.transportUnload));
                transportUnLoading = false;
            }
        });
    }

    private void calculateDistance() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (checkPermission()) {
                startLocationUpdates1();
            } else {
                requestPermission();
            }

        } else {
            buildAlertMessageNoGps();
        }

    }

    private void buildAlertMessageNoGps() {

        if (activity.CustomUtility.isInternetOn(mContext)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        //startLocationUpdates();
                        dialog.dismiss();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void startLocationUpdates1() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            from_lat = " ";
            from_lng = " ";
            to_lat = " ";
            to_lng = " ";
            fullAddress = "";
            fullAddress1 = "";
            startphoto = "";
            try {
                localConvenienceBean = dataHelper.getLocalConvinienceData();
                current_start_date = localConvenienceBean.getBegda();
                current_start_time = localConvenienceBean.getFrom_time();

                current_end_date = activity.CustomUtility.getCurrentDate();
                current_end_time = activity.CustomUtility.getCurrentTime();

                from_lat = localConvenienceBean.getFrom_lat();
                from_lng = localConvenienceBean.getFrom_lng();
                startphoto = localConvenienceBean.getPhoto1();
                if (location != null) {
                    to_lat = String.valueOf(location.getLatitude());
                    to_lng = String.valueOf(location.getLongitude());
                } else {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    assert location1 != null;
                    to_lat = String.valueOf(location1.getLatitude());
                    to_lng = String.valueOf(location1.getLongitude());


                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            fullAddress = localConvenienceBean.getStart_loc();
            if (activity.CustomUtility.isOnline(SubordinateImageActivity.this)) {
                progressDialog = ProgressDialog.show(SubordinateImageActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (!org.apache.http.util.TextUtils.isEmpty(from_lat) && !org.apache.http.util.TextUtils.isEmpty(from_lng) && !org.apache.http.util.TextUtils.isEmpty(to_lat) && !org.apache.http.util.TextUtils.isEmpty(to_lng)) {
                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        allLatLong = from_lat + "," + from_lng + "," + to_lat + "," + to_lng;
                        getDistanceInfo(from_lat, from_lng, to_lat, to_lng, allLatLong);
                    } else {
                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }

                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("Please start your journey first")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id) -> dialog.dismiss())
                                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                        final AlertDialog alert = builder.create();
                        alert.show();

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleasewaitcurrentlocation), Toast.LENGTH_SHORT).show();
                    }
                }, 2000);

            } else {
                Toast.makeText(getApplicationContext(), R.string.saved_travel_data, Toast.LENGTH_SHORT).show();
                LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(String.valueOf(username), current_start_date,
                        current_end_date,
                        current_start_time,
                        current_end_time,
                        from_lat,
                        to_lat,
                        from_lng,
                        to_lng,
                        fullAddress,
                        fullAddress1,
                        distance1,
                        startphoto,
                        ""
                );
                dataHelper.updateLocalconvenienceData(localConvenienceBean);
                wayPoints = dataHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());
                WayPoints wP = new WayPoints(username, current_start_date,
                        current_end_date,
                        current_start_time,
                        current_end_time, wayPoints.getWayPoints());
                dataHelper.updateWayPointData1(wP);
                stopLocationService();
                activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");
                activity.CustomUtility.removeFromSharedPreference(getApplicationContext(), Constant.FromLatitude);
                activity.CustomUtility.removeFromSharedPreference(getApplicationContext(), Constant.FromLongitude);
            }
        });
    }

    private void stopLocationService() {
        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationUpdateService.class));
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(SubordinateImageActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(SubordinateImageActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);

        }
    }

    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int AccessFineLocation =
                ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ReadMediaImage =
                ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED && ReadMediaImage == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContact = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudio = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadMediaImage = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadMediaAudio = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && ReadContact && ReadPhoneState && RecordAudio
                            && AccessCoarseLocation && AccessFineLocation && ReadMediaImage && ReadMediaAudio) {

                        if (activity.CustomUtility.getSharedPreferences(mContext, Constant.LocalConveyance).equalsIgnoreCase("1")) {
                            startLocationService();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), R.string.all_permission, Toast.LENGTH_LONG).show();
                    }
                } else {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage =
                            grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContact = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneState = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudio = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage && ReadContact && ReadPhoneState
                            && RecordAudio && AccessCoarseLocation && AccessFineLocation) {

                        if (activity.CustomUtility.getSharedPreferences(mContext, Constant.LocalConveyance).equalsIgnoreCase("1")) {
                            startLocationService();
                        }

                    } else {
                        Toast.makeText(SubordinateImageActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }

    private void startLocationService() {
        if (!LocationUpdateService.isServiceRunning) {
            Intent intent = new Intent(getApplicationContext(), LocationUpdateService.class);
            getApplicationContext().startService(intent);
        }
    }


    private void Save() {

        if (!imageArrayList.get(0).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_Product_serial_no), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(1).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_site_error), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(2).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_discharge), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(3).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_site_images), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(4).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_fir_field), Toast.LENGTH_SHORT).show();
        } else if (!imageArrayList.get(5).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_customer), Toast.LENGTH_SHORT).show();
        }else if (!imageArrayList.get(6).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.Please_other), Toast.LENGTH_SHORT).show();
        }else {
            CustomUtility.setSharedPreference(mContext, "AUDSYNC" + enq_docno, "1");
            isBackPressed = true;
        }

    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.old_serial_no));
        itemNameList.add(getResources().getString(R.string.site_error));
        itemNameList.add(getResources().getString(R.string.discharge_image));
        itemNameList.add(getResources().getString(R.string.site_images));
        itemNameList.add(getResources().getString(R.string.fir_field));
        itemNameList.add(getResources().getString(R.string.customer_satisfaction));
        itemNameList.add(getResources().getString(R.string.other));


        DatabaseHelper db = new DatabaseHelper(this);
        imageList = db.getAllImages(enq_docno);

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getName().equals(getResources().getString(R.string.borwellInlet))){
                    borewellInCheckBox.setChecked(true);
                    itemNameList.add(getResources().getString(R.string.borwellInlet));
                }else if (imageList.get(i).getName().equals(getResources().getString(R.string.borwellOutlet))){
                    borewellOutCheckBox.setChecked(true);
                    itemNameList.add(getResources().getString(R.string.borwellOutlet));
                }else if (imageList.get(i).getName().equals(getResources().getString(R.string.transportLoad))){
                    transportLoadCheckBox.setChecked(true);
                    itemNameList.add(getResources().getString(R.string.transportLoad));

                }else if (imageList.get(i).getName().equals(getResources().getString(R.string.transportUnload))){
                    transportUnloadCheckBox.setChecked(true);
                    itemNameList.add(getResources().getString(R.string.transportUnload));
                }
            }
        }


        for (int i = 0; i < itemNameList.size(); i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setBillNo("");
            imageModel.setImageSelected(false);
            imageArrayList.add(imageModel);
        }

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {

                    if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                        ImageModel imageModel = new ImageModel();
                        imageModel.setName(imageList.get(i).getName());
                        imageModel.setBillNo(imageList.get(i).getBillNo());
                        imageModel.setImagePath(imageList.get(i).getImagePath());
                        imageModel.setImageSelected(true);
                        imageArrayList.set(j, imageModel);
                    }
                }
            }
        }

        ImageAdapter = new ImageSelectionAdapter(SubordinateImageActivity.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(ImageAdapter);
        ImageAdapter.ImageSelection(this);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_FROM_FILE) {
            try {
                Uri mImageCaptureUri = data.getData();
                String path = getPath(SubordinateImageActivity.this, mImageCaptureUri); // From Gallery
                Log.e("Activity", "PathHolder22= " + path);
                String filename = path.substring(path.lastIndexOf("/") + 1);
                String file;
                if (filename.indexOf(".") > 0) {
                    file = filename.substring(0, filename.lastIndexOf("."));
                } else {
                    file = "";
                }
                if (TextUtils.isEmpty(file)) {
                    Toast.makeText(SubordinateImageActivity.this, "File not valid!", Toast.LENGTH_LONG).show();
                } else {
                    UpdateArrayList(path);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();


        }
    };

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            isUpdate = false;
            selectImage("0");
        }

    }

    private void selectImage(String value) {

        Log.e("select==>", "status" + value);

        LayoutInflater inflater = (LayoutInflater) SubordinateImageActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(SubordinateImageActivity.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView camera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            gallery.setVisibility(View.GONE);
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                openGallery();
            } else {
                Intent i_display_image = new Intent(SubordinateImageActivity.this, PhotoViewerActivity.class);
                i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                startActivity(i_display_image);
            }
        });

        camera.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                openCamera();
            } else {
                selectImage("0");
            }
        });

        cancel.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void openCamera() {

        camraLauncher.launch(new Intent(SubordinateImageActivity.this, CameraActivity2.class)
                .putExtra("cust_name", cust_nm));
    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {

                        Bundle bundle = result.getData().getExtras();
                        Log.e("bundle====>", Objects.requireNonNull(bundle.get("data")).toString());
                        UpdateArrayList(Objects.requireNonNull(bundle.get("data")).toString());
                    }
                }
            });

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setBillNo(enq_docno);
        imageModel.setImageSelected(true);

        imageArrayList.set(selectedIndex, imageModel);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (isUpdate) {
            db.updateImageRecord(imageArrayList.get(selectedIndex).getName(), path, true, enq_docno , boerwellLiffiting,borewellLowering,transportLoading,transportUnLoading);
        } else {
            db.insertImageRecord(imageArrayList.get(selectedIndex).getName(), path, true, enq_docno, boerwellLiffiting,borewellLowering,transportLoading,transportUnLoading);
        }
        ImageAdapter.notifyDataSetChanged();
    }

    public void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    private class savePendingPhotoDataAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait!");

        }

        @Override
        protected String doInBackground(String... params) {
            String obj2 = null;

            String mobile = CustomUtility.getSharedPreferences(getApplicationContext(), "username");

            JSONArray ja_invc_data = new JSONArray();
            JSONObject jsonObj = new JSONObject();


            try {
                jsonObj.put("CMPNO", enq_docno);
                jsonObj.put("kunnr", kunnr);
                jsonObj.put("mobile", mobile);
                jsonObj.put("sc_status", mStatusCheck);
                jsonObj.put("ACTION", pendRemarkValue);
                jsonObj.put("bore1_image", boerwellLiffiting);
                jsonObj.put("bore2_image",borewellLowering);
                jsonObj.put("transport1",transportLoading);
                jsonObj.put("transport2",transportUnLoading);
                System.out.println("only_text_jsonObj==>>" + jsonObj);

                if (imageArrayList.size() > 0) {

                    if (imageArrayList.get(0).isImageSelected()) {
                        jsonObj.put("PHOTO1", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(0).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO1", "");
                    }
                    if (1 < imageArrayList.size() && imageArrayList.get(1).isImageSelected()) {
                        jsonObj.put("PHOTO2", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(1).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO2", "");
                    }
                    if (2 < imageArrayList.size() && imageArrayList.get(2).isImageSelected()) {
                        jsonObj.put("PHOTO3", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(2).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO3", "");
                    }
                    if (3 < imageArrayList.size() && imageArrayList.get(3).isImageSelected()) {
                        jsonObj.put("PHOTO4", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(3).getImagePath()));
                    }  else{
                        jsonObj.put("PHOTO4", "");
                    }
                    if (4 < imageArrayList.size() && imageArrayList.get(4).isImageSelected()) {
                        jsonObj.put("PHOTO5", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(4).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO5", "");
                    }
                    if (5 < imageArrayList.size() && imageArrayList.get(5).isImageSelected()) {
                        jsonObj.put("PHOTO6", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(5).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO6", "");
                    }
                    if (6 < imageArrayList.size() && imageArrayList.get(6).isImageSelected()) {
                        jsonObj.put("PHOTO7", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(6).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO7", "");
                    }
                    if (7 < imageArrayList.size() && imageArrayList.get(7).isImageSelected()) {
                        jsonObj.put("PHOTO8", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(7).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO8", "");
                    }
                    if (8 < imageArrayList.size() && imageArrayList.get(8).isImageSelected()) {
                        jsonObj.put("PHOTO10", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(8).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO10", "");
                    }
                    if (9 < imageArrayList.size() && imageArrayList.get(9).isImageSelected()) {
                        jsonObj.put("PHOTO11", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(9).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO11", "");
                    }
                    if (10 < imageArrayList.size() && imageArrayList.get(10).isImageSelected()) {
                        jsonObj.put("PHOTO12", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(7).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO12", "");
                    }
                    if (11 < imageArrayList.size() && imageArrayList.get(11).isImageSelected()) {
                        jsonObj.put("PHOTO13", CustomUtility.getBase64FromBitmap(SubordinateImageActivity.this, imageArrayList.get(11).getImagePath()));
                    }else{
                        jsonObj.put("PHOTO13", "");
                    }

                }

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();

            param1_invc.add(new BasicNameValuePair("pending_save", String.valueOf(ja_invc_data)));
            Log.e("DATA$$$$", "" + param1_invc);

            Log.e("param1_invc_vihu==>>", "" + param1_invc);
            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_SUB_IMAGES, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equals("")) {


                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>" + jo11);
                    Message msg = new Message();
                    if (mStatus.equalsIgnoreCase("true")) {
                        msg.obj = "Data Submitted Successfully...";
                        mHandler2.sendMessage(msg);
                        progressDialog.dismiss();
                        dataHelper.deleteTableData(DatabaseHelper.TABLE_IMAGES);

                        Intent intent = new Intent(SubordinateImageActivity.this, MainActivity1.class);
                        startActivity(intent);
                        finish();
                    } else {

                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler2.sendMessage(msg);
                    }
                    progressDialog.dismiss();

                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();


        } else {
            gps.showSettingsAlert();
        }
    }

    private void getDistanceInfo(String lat1, String lon1, String lat2, String lon2, String allLatLong) {
        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY

        wayPoints = dataHelper.getWayPointsData(current_start_date, current_start_time);

        String Waypoint = wayPoints.getWayPoints();
        String wp = Waypoint.replaceAll("%3A", ":");
        wp = wp.replaceAll("%2C", ",");
        wp = wp.replaceAll("%7C", "|");
        String[] json = wp.split("\\|");

        Log.e("jsonSize=====>", String.valueOf(json.length));
        if (json.length > 20) {
            double position = (double) json.length / 8;
            Log.e("position=====>", String.valueOf(position));
            position = position * 2;

            int pos = (int) position;
            Log.e("position1=====>", String.valueOf(position));
            Log.e("pos=====>", String.valueOf(Math.round(pos)));

            for (int i = 0; i <= json.length; i++) {
                if (i != 0 && i != json.length - 1) {
                    if (totalWayPoint.isEmpty()) {
                        if (!totalWayPoint.contains(json[pos * i])) {

                            totalWayPoint = json[pos * i];
                            Log.e("positi====>", i + "=====>" + json[pos * i]);
                        }

                    } else {
                        if (pos * i < json.length) {
                            if (!totalWayPoint.contains(json[pos * i])) {
                                totalWayPoint = totalWayPoint + "|" + json[pos * i];
                                Log.e("positi====>", i + "=====>" + json[pos * i]);
                            }
                        }
                    }
                }
            }


        } else {
            for (int i = 0; i <= json.length; i++) {
                if (totalWayPoint.isEmpty()) {
                    if (!totalWayPoint.contains(json[i])) {
                        totalWayPoint = json[i];
                    }
                } else {
                    if (i < json.length) {
                        if (!totalWayPoint.contains(json[i])) {

                            totalWayPoint = totalWayPoint + "|" + json[i];
                        }
                    }
                }
            }
        }

        Log.e("json", Arrays.toString(json));
        Log.e("totalWayPoint", totalWayPoint.replaceAll("via:",""));

        Map<String, String> mapQuery = new HashMap<>();
        mapQuery.put("origin", lat1 + "," + lon1);
        mapQuery.put("destination", lat2 + "," + lon2);
        mapQuery.put("waypoints", totalWayPoint);
        mapQuery.put("key", getResources().getString(R.string.google_API_KEY));

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        Log.e("URL===>", String.valueOf(call.request().url()));
        Log.e("URL===>", String.valueOf(call.request().url()));


        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {

                if (response.body() != null &&
                        response.body().getRoutes() != null &&
                        response.body().getRoutes().size() > 0 &&
                        response.body().getRoutes().get(0) != null &&
                        response.body().getRoutes().get(0).getLegs() != null &&
                        response.body().getRoutes().get(0).getLegs().size() > 0 &&
                        response.body().getRoutes().get(0).getLegs().get(0) != null &&
                        response.body().getRoutes().get(0).getLegs().get(0).getDistance() != null &&
                        response.body().getRoutes().get(0).getLegs().get(0).getDuration() != null) {
                    Log.e("Response======>", String.valueOf(response.body()));


                    fullAddress = Utility.retrieveAddress(lat1, lon1, getApplicationContext());

                    fullAddress1 = Utility.retrieveAddress(lat2, lon2, getApplicationContext());

                    distance1 = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();

                    if (progressDialog != null)
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    Log.e("distance1=====>", distance1);
                    LayoutInflater inflater = (LayoutInflater) SubordinateImageActivity.this.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.custom_dialog2, null);
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(SubordinateImageActivity.this, R.style.MyDialogTheme);

                    builder.setView(layout);
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.white);
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.show();

                    final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                    final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                    final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                    final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                    final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                    final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                    final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                    //final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);
                    final TextInputEditText tiet_complaintNo = dialog.findViewById(R.id.tiet_complaintNo);
                    final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                    final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                    final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                    final TextView ettxt2 = dialog.findViewById(R.id.txt2);



                    tiet_complaintNo.setText(enq_docno);
                    etstrdt.setText(activity.CustomUtility.formateDate1(current_start_date) + " " + activity.CustomUtility.formatTime1(current_start_time));
                    etstrlatlng.setText(from_lat + "," + from_lng);
                    etenddt.setText(activity.CustomUtility.formateDate1(current_end_date) + " " + activity.CustomUtility.formatTime1(current_end_time));
                    etendlatlng.setText(to_lat + "," + to_lng);
                    etstrlocadd.setText(fullAddress);
                    etendlocadd.setText(fullAddress1);
                    ettotdis.setText(distance1);

                    ettxt1.setText(getResources().getString(R.string.localconveniencedetails));
                    ettxt2.setText(getResources().getString(R.string.endyourJourney));



                    etcncl.setOnClickListener(v -> dialog.dismiss());

                    etconfm.setOnClickListener(v -> {

                        if (activity.CustomUtility.isOnline(getApplicationContext())) {
                          //  if (!Objects.requireNonNull(ettrvlmod.getText()).toString().isEmpty()) {
                                new SubordinateImageActivity.savePendingPhotoDataAPI().execute();

                                new Thread(() -> runOnUiThread(() -> {
                                    LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(username, current_start_date,
                                            current_end_date,
                                            current_start_time,
                                            current_end_time,
                                            from_lat,
                                            to_lat,
                                            from_lng,
                                            to_lng,
                                            fullAddress,
                                            fullAddress1,
                                            distance1,
                                            startphoto,
                                            ""
                                    );

                                    dataHelper.updateLocalconvenienceData(localConvenienceBean);
                                    WayPoints wayPoints = dataHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());

                                    WayPoints wp1 = new WayPoints(String.valueOf(username), current_start_date,
                                            current_end_date,
                                            current_start_time,
                                            current_end_time, wayPoints.getWayPoints());
                                    dataHelper.updateWayPointData1(wp1);
                                    SyncLocalConveneinceDataToSap("", current_end_date, current_end_time, distance1, allLatLong);
                                })).start();

                                dialog.dismiss();


                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.ConnectToInternet), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //dialog.show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<DistanceResponse> call, @NonNull Throwable t) {

                Log.e("Failed", "&&&", t);

                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
            }
        });
    }

    public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm, String mFlotDistanceKM, String allLatLong) {

        String docno_sap;
        String invc_done;

        DatabaseHelper db = new DatabaseHelper(mContext);

        LocalConvenienceBean param_invc;

        param_invc = db.getLocalConvinienceData(endat, endtm);

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("cmpno", enq_docno);
            jsonObj.put("mobile", mobile);
            jsonObj.put("begda", CustomUtility.formateDate1(param_invc.getBegda()));
            jsonObj.put("endda", CustomUtility.formateDate1(param_invc.getEndda()));
            jsonObj.put("start_time", param_invc.getFrom_time());
            jsonObj.put("end_time", param_invc.getTo_time());
            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            if (param_invc.getStart_loc() != null && !param_invc.getStart_loc().isEmpty()) {
                jsonObj.put("start_location", param_invc.getStart_loc());
            } else {
                jsonObj.put("start_location", Utility.retrieveAddress(param_invc.getFrom_lat(), param_invc.getFrom_lng(), getApplicationContext()));
            }

            if (param_invc.getEnd_loc() != null && !param_invc.getEnd_loc().isEmpty()) {
                jsonObj.put("end_location", param_invc.getEnd_loc());
            } else {
                jsonObj.put("end_location", Utility.retrieveAddress(param_invc.getTo_lat(), param_invc.getTo_lng(), getApplicationContext()));
            }
            jsonObj.put("distance", mFlotDistanceKM);
            jsonObj.put("TRAVEL_MODE", mode);
            jsonObj.put("LAT_LONG", allLatLong);
            jsonObj.put("LAT1_LONG1", totalWayPoint.replaceAll("via:",""));
            if (param_invc.getPhoto1() != null && !param_invc.getPhoto1().isEmpty()) {
                jsonObj.put("PHOTO1", Utility.getBase64FromBitmap(mContext, param_invc.getPhoto1()));
            } else {
                jsonObj.put("PHOTO1", "");
            }

            if (param_invc.getPhoto2() != null && !param_invc.getPhoto2().isEmpty()) {
                jsonObj.put("PHOTO2", Utility.getBase64FromBitmap(mContext, param_invc.getPhoto2()));
            } else {
                jsonObj.put("PHOTO2", "");
            }
            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));


        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.LOCAL_CONVENIENVCE, param1_invc);

            if (!obj2.isEmpty()) {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");
                    if (invc_done.equalsIgnoreCase("S")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail1(endat, endtm);
                        db.deleteWayPointsDetail1(endat, endtm);
                        stopLocationService();
                        activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");
                        activity.CustomUtility.removeFromSharedPreference(getApplicationContext(), Constant.FromLatitude);
                        activity.CustomUtility.removeFromSharedPreference(getApplicationContext(), Constant.FromLongitude);


                    } else if (invc_done.equalsIgnoreCase("E")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }

                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    public void addItemInImageList(String Name){
        ImageModel imageModel = new ImageModel();
        imageModel.setName(Name);
        imageModel.setImagePath("");
        imageModel.setBillNo("");
        imageModel.setImageSelected(false);
        imageArrayList.add(imageModel);

        ImageAdapter.notifyDataSetChanged();
    }

    private void removeItemFromList(String name) {
        for (int i=0; i<imageArrayList.size(); i++){

            if(imageArrayList.get(i).getName().equals(name)){
                imageArrayList.remove(i);
            }
        }

        ImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("VALUES====>"," " +borewellLowering+"  "+boerwellLiffiting+"  "+transportLoading+"  "+transportUnLoading);

    }
}
