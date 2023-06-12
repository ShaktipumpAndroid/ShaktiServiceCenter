package activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import activity.complainvk.Freelauncer.PendingComplainListFreelancerActivity;
import activity.services.LocationUpdateService;
import activity.utility.Constant;
import activity.utility.Utility;
import bean.LatLongBeanVK;
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

    @SuppressWarnings({"deprecation", "InstantiationOfUtilityClass"})
    public class HomeFragmentFreeLauncer extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ProgressDialog progressDialog;
        Context context;
    private static final int REQUEST_CODE_PERMISSION = 123;
    LocalConvenienceBean localConvenienceBean;
    LocationManager locationManager;
    String current_start_date, current_end_date, current_start_time, current_end_time;
    TextView  pending_site, visted_site;
    TextView  start, end, offlineData;

    boolean start_photo_flag = false,
            end_photo_flag = false;
    FusedLocationProviderClient fusedLocationClient;

    protected Location location;
    String start_photo_text,end_photo_text;
        WayPoints wayPoints;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        Handler mHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mString = (String) msg.obj;
                Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
            }
        };

        List<LatLongBeanVK> mLatLongBeanVKList;
        private Uri fileUri;
    private final ArrayList<String> permissions = new ArrayList<>();
        DatabaseHelper dataHelper;
        String mobile;
        private CustomUtility customutility = null;
        String username;
        String from_lat;
        String from_lng;
        String to_lat;
        String allLatLong = "";
        String to_lng, value = "1";
        String fullAddress = null;
        String fullAddress1 = null;
        String distance1 = null;
        String startphoto = null,totalWayPoint="";
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    TextView photo1,photo2;
    String mServiceCenterName;

    public HomeFragmentFreeLauncer() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        progressDialog = new ProgressDialog(context);

        customutility = new CustomUtility();
        username = CustomUtility.getSharedPreferences(context,"username");
        mServiceCenterName = CustomUtility.getSharedPreferences(context,"ServiceCenterName");
        mLatLongBeanVKList = new ArrayList<>();
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        ArrayList<String> permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!checkPlayServices()) {
            Toast.makeText(context, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_feild, container, false);

        pending_site = rootView.findViewById(R.id.pendingVisit);
        visted_site = rootView.findViewById(R.id.visitedSite);
        start = rootView.findViewById(R.id.start);
        end = rootView.findViewById(R.id.end);
        offlineData = rootView.findViewById(R.id.offlinedata);
        dataHelper = new DatabaseHelper(getActivity());
        deleteCache(context);

        if (CustomUtility.getSharedPreferences(context,Constant.LocalConveyance).equalsIgnoreCase("1")){
            changeButtonVisibility(false, 0.5f, start);
            changeButtonVisibility(true, 1f, end);
        }else {
            changeButtonVisibility(false, 0.5f, end);
            changeButtonVisibility(true, 1f, start);
        }

        pending_site.setOnClickListener(view -> {

            WebURL.STATUS_CHECK_FOR_COMPLAIN = "01";
            Intent intent = new Intent(context, PendingComplainListFreelancerActivity.class);
            intent.putExtra("complaint", "Pending Site to Visit");
            startActivity(intent);
        });

        visted_site.setOnClickListener(view -> {
            WebURL.STATUS_CHECK_FOR_COMPLAIN = "02";
            Intent intent = new Intent(context, VisitedComplainListActivity.class);
            intent.putExtra("complaint", "Visited Site");
            startActivity(intent);
        });

        start.setOnClickListener(this);
        end.setOnClickListener(this);
        offlineData.setOnClickListener(this);

        return rootView;
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    @Override
    public void onStart() {

        super.onStart();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                getActivity().finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

        private void requestPermission() {
            if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO},
                        REQUEST_CODE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);

            }
        }

    private void changeButtonVisibility(boolean state, float alphaRate, TextView txtSubmiteOrderID) {
        txtSubmiteOrderID.setEnabled(state);
        txtSubmiteOrderID.setAlpha(alphaRate);
    }

        @Override
        public void onResume() {
            super.onResume();
            final LocationManager manager =
                    (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!gps_enabled && !network_enabled) {
                buildAlertMessageNoGps();
            } else {
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    if (CustomUtility.getSharedPreferences(context, Constant.LocalConveyance).equalsIgnoreCase("1")) {
                        startLocationService();
                    }
                }
            }

        }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }



    @Override
    public void onDestroy() {

        if (progressDialog!=null) {
            progressDialog.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {

        if (progressDialog!=null) {
            progressDialog.cancel();
        }
        super.onStop();
    }


    public void showConfirmationGallery(final String keyimage, final String name) {

        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            boolean result = CustomUtility.checkPermission(context);
            if (items[item].equals("Take Photo")) {

                if (result) {
                    openCamera();
                    setFlag(keyimage);
                }

            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // if the result is capturing Image
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {

                    try {
                        Bitmap bitmap =
                                MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);

                        Bitmap UserBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                                fileUri);

                        String path = Utils.getPath(context, fileUri);

                        if (path == null) {
                            path = data.getData().getPath();
                        }

                        Log.e("Activity", "PathHolder22= " + path);

                        String filename = path.substring(path.lastIndexOf("/") + 1);
                        String file;
                        if (filename.indexOf(".") > 0) {
                            file = filename.substring(0, filename.lastIndexOf("."));
                        } else {
                            file = "";
                        }
                        if (android.text.TextUtils.isEmpty(file)) {
                            CustomUtility.ShowToast("File not valid", getActivity());
                        } else {

                            if (start_photo_flag) {
                                start_photo_text = path;
                                setIcon(DatabaseHelper.KEY_PHOTO1);
                            }

                            if (end_photo_flag) {
                                end_photo_text = path;
                                setIcon(DatabaseHelper.KEY_PHOTO2);
                            }
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }

    public void setIcon(String key) {

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (start_photo_text == null || start_photo_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (end_photo_text == null || end_photo_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


        }

    }


    public void setFlag(String key) {
        start_photo_flag = false;
        end_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                start_photo_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                end_photo_flag = true;
                break;

        }

    }

        public void openCamera() {
            ContentValues values = new ContentValues();
            fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.start:
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                        startLocationUpdates();

                    } else {
                        buildAlertMessageNoGps();
                    }
                    break;

                case R.id.end:
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
                    break;

                case R.id.offlinedata:
                    Intent intnt = new Intent(context, OfflineDataConveyance.class);
                    startActivity(intnt);
                    break;
            }
        }

        @SuppressLint("UseRequireInsteadOfGet")
        public void startLocationUpdates() {
            start_photo_text = "";

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                final Double[] lat = new Double[1];
                final Double[] lng = new Double[1];
                from_lat = "";
                from_lng = "";
                to_lat = "";
                to_lng = "";
                fullAddress = "";
                fullAddress1 = "";
                try {
                    current_start_date =  CustomUtility.getCurrentDate();
                    current_start_time = CustomUtility.getCurrentTime();
                    if (location != null) {
                        from_lat = String.valueOf(location.getLatitude());
                        from_lng = String.valueOf(location.getLongitude());
                        lat[0] = location.getLatitude();
                        lng[0] = location.getLongitude();
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        if (location1 != null) {
                            from_lat = String.valueOf(location1.getLatitude());

                        from_lng = String.valueOf(location1.getLongitude());
                        lat[0] = location1.getLatitude();
                        lng[0] = location1.getLongitude();
                        }
                    }

                    progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        //Do something here
                        if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng)) {
                            if (progressDialog != null)
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.custom_dialog1);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            dialog.getWindow().setAttributes(lp);

                            final TextInputEditText etlat = dialog.findViewById(R.id.tiet_lat);
                            final TextInputEditText etlng = dialog.findViewById(R.id.tiet_lng);
                            final TextInputEditText etadd = dialog.findViewById(R.id.tiet_add);
                            final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                            final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                            photo1 = dialog.findViewById(R.id.photo1);
                            final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                            final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);

                            if (CustomUtility.isOnline(getActivity())) {
                                Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                if (location != null) {
                                    try {
                                        addresses = geo.getFromLocation(lat[0], lng[0], 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (addresses != null) {
                                    if (addresses.isEmpty()) {
                                        etadd.setText(getResources().getString(R.string.no_location_found));
                                    } else {
                                        etadd.setText(addresses.get(0).getAddressLine(0));
                                    }
                                }
                            }
                            etlat.setText(from_lat);
                            etlng.setText(from_lng);

                            ettxt1.setText(getResources().getString(R.string.Current_Location));
                            ettxt2.setText(getResources().getString(R.string.confirm_));

                            // Toast.makeText(getActivity(), "from_lat="+from_lat+"\nfrom_lng="+from_lng, Toast.LENGTH_SHORT).show();

                            photo1.setOnClickListener(v -> {
                                value = "1";
                                if (start_photo_text == null || start_photo_text.isEmpty()) {
                                    if (checkPermission()) {
                                        showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                                    } else {
                                        requestPermission();
                                    }
                                }
                            });

                            etcncl.setOnClickListener(v -> dialog.dismiss());

                            etconfm.setOnClickListener(v -> {
                                LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(String.valueOf(username),
                                        current_start_date,
                                        "",
                                        current_start_time,
                                        "",
                                        from_lat,
                                        "",
                                        from_lng,
                                        "",
                                        "",
                                        "",
                                        "",
                                        start_photo_text,
                                        ""
                                );
                                dataHelper.insertLocalconvenienceData(localConvenienceBean);

                                String latlng = "via:" + from_lat + "," + from_lng;
                                Log.e("latlng=====>", latlng);
                                WayPoints wayPoints = new WayPoints(String.valueOf(username), current_start_date,
                                        "",
                                        current_start_time,
                                        "", latlng);

                                dataHelper.insertWayPointsData(wayPoints);

                                CustomUtility.setSharedPreference(getActivity(), Constant.LocalConveyance, "1");
                                CustomUtility.setSharedPreference(getActivity(), Constant.FromLatitude, from_lat);
                                CustomUtility.setSharedPreference(getActivity(), Constant.FromLongitude, from_lng);
                                CustomUtility.setSharedPreference(getActivity(), Constant.DistanceInMeter, "0");
                                changeButtonVisibility(false, 0.5f, start);
                                changeButtonVisibility(true, 1f, end);
                                startLocationService();
                                Toast.makeText(getActivity(), getResources().getString(R.string.YourJourney), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            });

                            dialog.show();
                        } else {
                            if (progressDialog != null)
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            Toast.makeText(getActivity(), getResources().getString(R.string.Pleasewaitcurrentlocation), Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        private void startLocationService() {
            if (!LocationUpdateService.isServiceRunning) {
                Intent intent = new Intent(getActivity(), LocationUpdateService.class);
                getActivity().startService(intent);
            }
        }

        private boolean checkPermission() {
            int cameraPermission =
                    ContextCompat.checkSelfPermission(getActivity(), CAMERA);
            int writeExternalStorage =
                    ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
            int ReadExternalStorage =
                    ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);

            int AccessCoarseLocation =
                    ContextCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION);
            int AccessFineLocation =
                    ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
            int ReadMediaImage =
                    ContextCompat.checkSelfPermission(getActivity(), READ_MEDIA_IMAGES);


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

                            if (CustomUtility.getSharedPreferences(context, Constant.LocalConveyance).equalsIgnoreCase("1")) {
                                startLocationService();
                            }

                            if(value.equals("1")){
                                showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                            }else {
                                showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                            }
                        }else {
                            Toast.makeText(getActivity(), R.string.all_permission, Toast.LENGTH_LONG).show();
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

                            if (CustomUtility.getSharedPreferences(context, Constant.LocalConveyance).equalsIgnoreCase("1")) {
                                startLocationService();
                            }
                            if(value.equals("1")){

                                showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                            }else {
                                showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                            }
                        }else {
                            Toast.makeText(getActivity(), R.string.all_permission, Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        }

        public void startLocationUpdates1() {
            end_photo_text = "";
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
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

                        current_end_date = customutility.getCurrentDate();
                        current_end_time = customutility.getCurrentTime();

                        from_lat = localConvenienceBean.getFrom_lat();
                        from_lng = localConvenienceBean.getFrom_lng();
                        startphoto = localConvenienceBean.getPhoto1();
                        if (location != null) {
                            to_lat = String.valueOf(location.getLatitude());
                            to_lng = String.valueOf(location.getLongitude());
                        } else {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            to_lat = String.valueOf(location1.getLatitude());
                            to_lng = String.valueOf(location1.getLongitude());


                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    fullAddress = localConvenienceBean.getStart_loc();
                    if (CustomUtility.isOnline(getActivity())) {
                        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng) && !TextUtils.isEmpty(to_lat) && !TextUtils.isEmpty(to_lng)) {
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
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Pleasewaitcurrentlocation), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 2000);

                    } else {
                        Toast.makeText(getActivity(), R.string.saved_travel_data, Toast.LENGTH_SHORT).show();
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
                                end_photo_text
                        );
                        dataHelper.updateLocalconvenienceData(localConvenienceBean);
                        wayPoints = dataHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());
                        WayPoints wP = new WayPoints(username, current_start_date,
                                current_end_date,
                                current_start_time,
                                current_end_time, wayPoints.getWayPoints());
                        dataHelper.updateWayPointData1(wP);
                        stopLocationService();
                        CustomUtility.setSharedPreference(getActivity(), Constant.LocalConveyance, "0");
                        CustomUtility.removeFromSharedPreference(getActivity(), Constant.FromLatitude);
                        CustomUtility.removeFromSharedPreference(getActivity(), Constant.FromLongitude);
                        changeButtonVisibility(false, 0.5f, end);
                        changeButtonVisibility(true, 1f, start);

                    }
                }
            });
        }

        private void stopLocationService() {
            getActivity().stopService(new Intent(getActivity(), LocationUpdateService.class));
        }


        public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm, String mFlotDistanceKM, String allLatLong) {

            String docno_sap ;
            String invc_done ;

            DatabaseHelper db = new DatabaseHelper(this.getActivity());

            LocalConvenienceBean param_invc ;

            param_invc = db.getLocalConvinienceData(endat, endtm);

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                jsonObj.put("cmpno","");
                jsonObj.put("mobile",mobile);
                jsonObj.put("begda", param_invc.getBegda());
                jsonObj.put("endda", param_invc.getEndda());
                jsonObj.put("start_time", param_invc.getFrom_time());
                jsonObj.put("end_time", param_invc.getTo_time());
                jsonObj.put("start_lat", param_invc.getFrom_lat());
                jsonObj.put("end_lat", param_invc.getTo_lat());
                jsonObj.put("start_long", param_invc.getFrom_lng());
                jsonObj.put("end_long", param_invc.getTo_lng());
                if (param_invc.getStart_loc() != null && !param_invc.getStart_loc().isEmpty()) {
                    jsonObj.put("start_location", param_invc.getStart_loc());
                } else {
                    jsonObj.put("start_location", Utility.retrieveAddress(param_invc.getFrom_lat(), param_invc.getFrom_lng(), getActivity()));
                }

                if (param_invc.getEnd_loc() != null && !param_invc.getEnd_loc().isEmpty()) {
                    jsonObj.put("end_location", param_invc.getEnd_loc());
                } else {
                    jsonObj.put("end_location", Utility.retrieveAddress(param_invc.getTo_lat(), param_invc.getTo_lng(), getActivity()));
                }
                jsonObj.put("distance", mFlotDistanceKM);
                jsonObj.put("TRAVEL_MODE", mode);
                jsonObj.put("LAT_LONG", allLatLong);
                if (param_invc.getPhoto1() != null && !param_invc.getPhoto1().isEmpty()) {
                    jsonObj.put("PHOTO1", Utility.getBase64FromBitmap(context, param_invc.getPhoto1()));
                } else {
                    jsonObj.put("PHOTO1", "");
                }

                if (param_invc.getPhoto2() != null && !param_invc.getPhoto2().isEmpty()) {
                    jsonObj.put("PHOTO2", Utility.getBase64FromBitmap(context, param_invc.getPhoto2()));
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
                            ;
                            Message msg = new Message();
                            msg.obj = docno_sap;
                            mHandler.sendMessage(msg);
                            db.deleteLocalconvenienceDetail1(endat, endtm);
                            db.deleteWayPointsDetail1(endat, endtm);
                            stopLocationService();
                            CustomUtility.setSharedPreference(getActivity(), Constant.LocalConveyance, "0");
                            CustomUtility.removeFromSharedPreference(getActivity(), Constant.FromLatitude);
                            CustomUtility.removeFromSharedPreference(getActivity(), Constant.FromLongitude);
                            changeButtonVisibility(false, 0.5f, end);
                            changeButtonVisibility(true, 1f, start);

                        } else if (invc_done.equalsIgnoreCase("E")) {

                            if ((progressDialog != null) && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ;
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

        private void buildAlertMessageNoGps() {

            if (CustomUtility.isInternetOn(context)) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            CustomUtility.setSharedPreference(context,Constant.LocalConveyance,"1");
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            startLocationUpdates();

                            dialog.dismiss();


                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                final AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                double position = (double) json.length /8;
                Log.e("position=====>", String.valueOf(position));
                position = position*2 ;

                int pos = (int) position;
                Log.e("position1=====>", String.valueOf(position));
                Log.e("pos=====>", String.valueOf(Math.round(pos)));

                for (int i = 0; i <= json.length; i++) {
                    if(i!=0 && i!=json.length-1) {
                        if (totalWayPoint.isEmpty()) {
                            if (!totalWayPoint.contains(json[pos * i])) {

                                totalWayPoint = json[pos * i];
                                Log.e("positi====>", String.valueOf(i) + "=====>" + json[pos * i]);
                            }

                        } else {
                            if (pos * i < json.length) {
                                if (!totalWayPoint.contains(json[pos * i])) {
                                    totalWayPoint = totalWayPoint + "|" + json[pos * i];
                                    Log.e("positi====>", String.valueOf(i) + "=====>" + json[pos * i]);
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
            Log.e("totalWayPoint", totalWayPoint);

            Map<String, String> mapQuery = new HashMap<>();
            mapQuery.put("origin",lat1+ "," + lon1);
            mapQuery.put("destination", lat2+ "," + lon2);
            mapQuery.put("waypoints", totalWayPoint);
            mapQuery.put("key", getResources().getString(R.string.google_API_KEY));

            DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

            Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
            Log.e("URL===>", String.valueOf(call.request().url()));
            Log.e("URL===>", String.valueOf(call.request().url()));

     /*    totalWayPoint = totalWayPoint.replaceAll("via:","");
        String newUri =
                "https://www.google.com/maps/dir/?api=1&origin=" +lat1 + "," + lon1 + "&destination=" +lat2 + "," + lon2+"&waypoints="+totalWayPoint;
        Log.e("maproute", "Uri: " + newUri);


       Uri googleIntentURI = Uri.parse(String.valueOf(call.request().url()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleIntentURI);
        mapIntent.setPackage("com.google.android.apps.maps");
       context.startActivity(mapIntent);*/
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


                        fullAddress = Utility.retrieveAddress(lat1, lon1, getActivity());

                        fullAddress1 = Utility.retrieveAddress(lat2, lon2, getActivity());

                        distance1 = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();

                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        Log.e("distance1=====>", distance1);

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.custom_dialog2);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);

                        final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                        final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                        final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                        final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                        final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                        final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                        final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                        final TextInputLayout til_trvl_mod = dialog.findViewById(R.id.til_trvl_mod);
                        final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);


                        final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                        final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                        final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                        final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                        photo2 = dialog.findViewById(R.id.photo2);


                        etstrdt.setText(CustomUtility.formateDate1(current_start_date) + " " + CustomUtility.formatTime1(current_start_time));
                        etstrlatlng.setText(from_lat + "," + from_lng);
                        etenddt.setText(CustomUtility.formateDate1(current_end_date) + " " + CustomUtility.formatTime1(current_end_time));
                        etendlatlng.setText(to_lat + "," + to_lng);
                        etstrlocadd.setText(fullAddress);
                        etendlocadd.setText(fullAddress1);
                        ettotdis.setText(distance1);

                        ettxt1.setText(getResources().getString(R.string.localconveniencedetails));
                        ettxt2.setText(getResources().getString(R.string.endyourJourney));

                        photo2.setOnClickListener(v -> {
                            if (end_photo_text == null || end_photo_text.isEmpty()) {

                                if (checkPermission()) {
                                    openCamera();
                                } else {
                                    requestPermission();
                                }
                            }
                        });

                        etcncl.setOnClickListener(v -> dialog.dismiss());

                        etconfm.setOnClickListener(v -> {
                            if (CustomUtility.isOnline(getActivity())) {
                                if (!ettrvlmod.getText().toString().isEmpty()) {

                                    progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.sending_please_wait));

                                    new Thread(() -> getActivity().runOnUiThread(() -> {
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
                                                end_photo_text
                                        );

                                        dataHelper.updateLocalconvenienceData(localConvenienceBean);
                                        WayPoints  wayPoints = dataHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());

                                        WayPoints wp1 = new WayPoints(String.valueOf(username), current_start_date,
                                                current_end_date,
                                                current_start_time,
                                                current_end_time, wayPoints.getWayPoints());
                                        dataHelper.updateWayPointData1(wp1);
                                        SyncLocalConveneinceDataToSap(ettrvlmod.getText().toString(), current_end_date, current_end_time, distance1, allLatLong);
                                    })).start();

                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Please_Enter_Travel_Mode), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.ConnectToInternet), Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.show();

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

    }