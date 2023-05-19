package activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.snackbar.Snackbar;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.io.File;
import java.util.ArrayList;

import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MainActivity2 extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, SharedPreferences.OnSharedPreferenceChangeListener {
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;
    private static final String TAG = MainActivity2.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    FusedLocationProviderClient fusedLocationProviderClient;
    // The BroadcastReceiver used to listen from broadcasts from the service.

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    // A reference to the service used to get location updates.

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.

    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    ProgressDialog progressBar;
    CustomUtility customUtility;
    ProgressDialog dialog;
    Context mContex;
    String versionName = "0.0";
    DatabaseHelper dataHelper = null;
    SAPWebService con = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String newVersion = "0.0";
    String logout_value = "";
    String current_date = "null", current_time;
    String latitude = "0.0";
    String longitude = "0.0";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MainActivity2.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private PendingIntent pendingIntent;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContex = this;
        con = new SAPWebService();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        fusedLocationProviderClient = getFusedLocationProviderClient(this);

        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);



        deleteCache(mContex);

        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        customUtility = new CustomUtility();

        dataHelper = new DatabaseHelper(this);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        // int versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

       // dataHelper.deletecmpattach();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");

        LoginBean.username = pref.getString("key_ename", "username");


        // check background service is running or not

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        if (checkPlayServices()) {


        } else {
            Toast.makeText(mContex, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

/******* Create SharedPreferences *******/





// set login person name to navigation drawer

        TextView tv = (TextView) findViewById(R.id.ename);
        tv.setText("Welcome,  " + LoginBean.getUsername() + "   V " + versionName);

        // delete data from mobile which is save in sap



        // display the first navigation drawer view on app launch
        displayView(0);


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



    public void logOut() {

        progressDialog = ProgressDialog.show(MainActivity2.this, "", "Sync Offline Data !");

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isOnline(MainActivity2.this)) {

//           new Capture_employee_gps_location(mContex, "16", "");

                    editor.putString("key_logout", "logout");
                    editor.commit();

                 //   new SyncDataToSAP_New().SendDataToSap(mContex);
                      if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    //delete_data_save_in_sap();

                    editor.putString("key_login", "N");
                    editor.putString("key_username", "");
                    editor.putString("key_ename", "");

                    editor.commit(); //

                    // CustomUtility.setSharedPreference(mContext,"objs",spinner_login_type_text);
                    //   CustomUtility.setSharedPreference(mContext,"pernr",username);
                    CustomUtility.setSharedPreference(mContex,"userID","");
                    CustomUtility.setSharedPreference(mContex,"ServiceCenterName","");

                    Intent mIntent = new Intent(MainActivity2.this, LoginActivity.class);
                    startActivity(mIntent);
                    finish();


                } else {
                      if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    Message msg = new Message();
                    msg.obj = "No internet Connection. Log Out Failed";
                    mHandler.sendMessage(msg);

                }


            }
        }
        ).start();


    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        // String title = getString(R.string.app_name);
        //String title = "shakti_white";

        Intent intent = null;
        Context ctx = null;
        switch (position) {


            case 0:
             //   fragment = new HomeFragment();
                fragment = new HomeFragmentFreeLauncer();
               // Intent mIntent = new Intent(mContex, ComplaintDashboard.class);
            //    startActivity(mIntent);
                //   title = getString(R.string.title_home);
                break;

            case 1:

                progressDialog = ProgressDialog.show(MainActivity2.this, "", "Loading..");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (CustomUtility.isOnline(MainActivity2.this)) {
                              if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.shaktipumps.com/faq.php"));
                            startActivity(browserIntent);

                        } else {
                              if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            Message msg = new Message();
                            msg.obj = "Please ON Internet Connection For This Function.";
                            mHandler.sendMessage(msg);

                        }

                    }
                }).start();

                break;

            case 2:
                logOut();
                // Intent mIntent = new Intent(mContex, ComplaintDashboard.class);
                //    startActivity(mIntent);
                //   title = getString(R.string.title_home);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);

            fragmentTransaction.commit();


            // set the toolbar title
            getSupportActionBar().setTitle(" V  " + versionName);
            getSupportActionBar().setIcon(R.drawable.new_logo);

        }
    }

    /*public void OnBackPressed() {


        System.exit(0);
    }*/







    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);




    }



    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }




    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {

        }
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
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContex);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) mContex, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                //    mService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                        findViewById(R.id.lin1),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    private boolean checkPermissions() {

        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.lin1),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity2.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity2.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
