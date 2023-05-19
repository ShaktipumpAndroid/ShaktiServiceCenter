package activity;

/**
 * Created by shakti on 10/3/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumps.shakti.shaktiServiceCenter.R;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import activity.complaint.ClosureComplaintActivity;
import activity.complaint.ComplaintDashboard;
import activity.complaint.ComplaintSearchActivity;
import activity.complaint.DealerPendCompActivity;
import activity.complaint.PendingComplaintActivity;
import activity.complaint.ServiceCenterActivity;
import activity.complainvk.Freelauncer.PendingComplainListFreelauncerActivity;
import activity.complainvk.PendingComplainListActivity;
import bean.LocalConvenienceBean;
import bean.LoginBean;
import database.DatabaseHelper;
import models.DistanceResponse;
import models.Element;
import other.CameraUtils;
import rest.DistanceApiClient;
import rest.RestUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import searchlist.UnsyncData;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;


//import com.shaktipumps.shakti.material_design.R;



    public class HomeFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ProgressDialog progressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    Context context;
    TextView btn_unsync;

TextView approved_complaint, closer_complaint, open_complaint, pending_for_Approval, txtServiceCenterID;


    boolean start_photo_flag = false,
            end_photo_flag = false;




    String start_photo_text,end_photo_text;
    public static final int BITMAP_SAMPLE_SIZE = 4;



    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };

    String current_start_date, current_end_date, current_start_time, current_end_time;

    FusedLocationProviderClient fusedLocationClient;
    protected LocationRequest locationRequest;
    protected Location location;
    protected android.location.LocationListener locationListener;
    LocationCallback locationCallback;

    private CustomUtility customutility = null;

    DatabaseHelper dataHelper;
    String fullAddress = null;
    String fullAddress1 = null;


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static String imageStoragePath;
     TextView photo1,photo2;

     String mServiceCenterName;



        public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();

        customutility = new CustomUtility();
        progressDialog = new ProgressDialog(context);

        mServiceCenterName = CustomUtility.getSharedPreferences(context,"ServiceCenterName");
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (checkPlayServices()) {

        } else {
            Toast.makeText(context, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        open_complaint = rootView.findViewById(R.id.open_complaint);
        txtServiceCenterID = rootView.findViewById(R.id.txtServiceCenterID);
        closer_complaint =rootView.findViewById(R.id.closer_complaint);
        pending_for_Approval = rootView.findViewById(R.id.pending_for_Approval);
        approved_complaint = rootView.findViewById(R.id.approved_complaint);

        txtServiceCenterID.setText(mServiceCenterName);

        deleteCache(context);

        open_complaint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //getNewComplaint();

                WebURL.STATUS_CHECK_FOR_COMPLAIN = "01";
                Intent intent = new Intent(context, PendingComplainListActivity.class);
               // Intent intent = new Intent(context, PendingComplainListFreelauncerActivity.class);
                intent.putExtra("complaint", "New Complaint");
                intent.putExtra("StatusValue", "01");
                startActivity(intent);
            }
        });

        pending_for_Approval.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               // getNewComplaint();
                WebURL.STATUS_CHECK_FOR_COMPLAIN = "02";
                Intent intent = new Intent(context, PendingComplainListActivity.class);
                //intent.putExtra("complaint", "Dealer Complaint");
                intent.putExtra("complaint", "Pending for Approval");
                intent.putExtra("StatusValue", "02");
                startActivity(intent);
            }
        });

        approved_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // complaint count
             //   getPendingComplaint();
                WebURL.STATUS_CHECK_FOR_COMPLAIN = "03";
                Intent intent = new Intent(context, PendingComplainListActivity.class);
                intent.putExtra("complaint", "Approved Complain");
                intent.putExtra("StatusValue", "03");
                startActivity(intent);
            }
        });

        closer_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // getClosureComplaint();
                WebURL.STATUS_CHECK_FOR_COMPLAIN = "04";
                Intent intent = new Intent(context, PendingComplainListActivity.class);
                intent.putExtra("complaint", "Clouser Complain");
                intent.putExtra("StatusValue", "04");
                startActivity(intent);
            }
        });

        // get unsync data count
        // Inflate the layout for this fragment
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


    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(context)) {

        } else {
            CustomUtility.showSettingsAlert(context);
            return false;
        }
        return true;
    }



    @Override
    public void onResume() {

        // get unsync data count
        super.onResume();


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


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.start:

                break;

            case R.id.end:

                break;


         /*       Intent intnt = new Intent(context, OfflineDataConveyance.class);

                startActivity(intnt);*/

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    getActivity().finish();
                    System.exit(0);

                }
                break;
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(context).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } /*else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }*/

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void changeButtonVisibility(boolean state, float alphaRate, TextView txtSubmiteOrderID) {
        txtSubmiteOrderID.setEnabled(state);
        txtSubmiteOrderID.setAlpha(alphaRate);
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

        // Permissions ok, we get last location
       /* location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Log.e("Latitude : ","Longitued" + location.getLatitude()+"  "+location.getLongitude());
        }*/

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

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        Bitmap bitmap = null;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {

                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                    if (start_photo_flag) {
                        start_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO1);
                    }

                    if (end_photo_flag) {
                        end_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO2);
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                File file = new File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
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

    public void openCamera(String keyimage) {

        if (CameraUtils.checkPermissions(context)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH","&&&"+imageStoragePath);
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(context, file);

            Log.e("PATH","&&&"+fileUri);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        }


    }


}