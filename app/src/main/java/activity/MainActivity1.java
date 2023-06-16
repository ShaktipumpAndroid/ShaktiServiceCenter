package activity;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.util.Objects;

import activity.services.LocationUpdateService;
import activity.utility.Constant;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.SAPWebService;


@SuppressWarnings({"deprecation", "InstantiationOfUtilityClass"})
public class MainActivity1 extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity1.class.getSimpleName();
    private int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    ProgressDialog progressBar;

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    FusedLocationProviderClient fusedLocationProviderClient;
    private final ArrayList<String> permissions = new ArrayList<>();

    CustomUtility customUtility;
    Context mContext;
    String versionName = "0.0";
    DatabaseHelper dataHelper = null;
    SAPWebService con = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(MainActivity1.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;

    String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        con = new SAPWebService();
        Toolbar mToolbar = findViewById(R.id.toolbar);

        fusedLocationProviderClient = getFusedLocationProviderClient(this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        ArrayList<String> permissionsToRequest = permissionsToRequest(permissions);
        deleteCache(mContext);

        progressDialog = new ProgressDialog(mContext);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        customUtility = new CustomUtility();

        dataHelper = new DatabaseHelper(this);
        userType = activity.CustomUtility.getSharedPreferences(mContext,"userType");
        Log.e("userType", "" + userType);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        assert drawerFragment != null;
        drawerFragment.setUp(R.id.fragment_navigation_drawer,  findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        versionName = BuildConfig.VERSION_NAME;

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();


        LoginBean.userid = pref.getString("key_username", "userid");

        LoginBean.username = pref.getString("key_ename", "username");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
            }
        }

        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        if (!checkPlayServices()) {
            Toast.makeText(mContext, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

        TextView tv =  findViewById(R.id.ename);
        tv.setText("Welcome,  " + LoginBean.getUsername() + "   V " + versionName);

        displayView(0);

        if(userType.equalsIgnoreCase("1")){
            syncState();
        }
        else {
            downloadSubordinateData();
        }
    }

    private void downloadSubordinateData() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Downloading Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread(() -> {
            while (progressBarStatus < 100) {
                try {
                    progressBarStatus = 20;
                    dataHelper.deleteTableData(dataHelper.TABLE_ASSGIN_COMPLAIN_SUBORDINATE);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                    con.getAssginComplain(MainActivity1.this);

                    progressBarStatus = 39;
                    dataHelper.deleteTableData(dataHelper.TABLE_VISIT_COMPLAIN_SUBORDINATE);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                    con.getVisitedComplain(MainActivity1.this);

                    progressBarStatus = 100;
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressBar.dismiss();
        }).start();
    }

    @SuppressLint("SuspiciousIndentation")
    public void syncState() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Downloading Data...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;

        new Thread(() -> {
            while (progressBarStatus < 100) {
                try {
                        progressBarStatus = 30;
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                        con.getStateData(MainActivity1.this);

                        progressBarStatus = 46;
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                        con.getSubordinateData(MainActivity1.this);

                    progressBarStatus = 100;
                        progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressBar.dismiss();
        }).start();
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


        progressDialog = ProgressDialog.show(MainActivity1.this, "", "Sync Offline Data !");


        new Thread(() -> {


            if (CustomUtility.isOnline(MainActivity1.this)) {

                editor.putString("key_logout", "logout");
                editor.commit();
                  if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                editor.putString("key_login", "N");
                editor.putString("key_username", "");
                editor.putString("key_ename", "");

                editor.commit(); //

                CustomUtility.setSharedPreference(mContext,"userID","");
                CustomUtility.setSharedPreference(mContext,"ServiceCenterName","");

                Intent mIntent = new Intent(MainActivity1.this, LoginActivity.class);
                startActivity(mIntent);
                finish();



            } else {
                  if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                Message msg = new Message();
                msg.obj = "No internet Connection. Log Out Failed";
                mHandler.sendMessage(msg);

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
        switch (position) {


            case 0:

                if(userType.equalsIgnoreCase("1"))
                    fragment = new HomeFragment();
                else
                    fragment = new HomeFragmentFreeLauncer();
                break;

            case 1:

                progressDialog = ProgressDialog.show(MainActivity1.this, "", "Loading..");

                new Thread(() -> {
                    if (CustomUtility.isOnline(MainActivity1.this)) {
                          if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.shaktipumps.com/faq.php"));
                        startActivity(browserIntent);

                    } else {
                          if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                        Message msg = new Message();
                        msg.obj = "Please ON Internet Connection For This Function.";
                        mHandler.sendMessage(msg);

                    }

                }).start();

                break;

            case 2:
                if(userType.equalsIgnoreCase("1"))
                    logOut();
                else
                    Logout();

                break;

            case 3:
                Intent i = new Intent(MainActivity1.this,Register.class);
                startActivity(i);
                break;

            case 4:
                Intent intent = new Intent(MainActivity1.this,SubordinateList.class);
                startActivity(intent);

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);

            fragmentTransaction.commit();

            Objects.requireNonNull(getSupportActionBar()).setTitle(" V  " + versionName);
            getSupportActionBar().setIcon(R.drawable.new_logo);

        }
    }

    private void Logout() {

        dataHelper.deleteTableData(DatabaseHelper.TABLE_ASSGIN_COMPLAIN_SUBORDINATE);
        dataHelper.deleteTableData(DatabaseHelper.TABLE_VISIT_COMPLAIN_SUBORDINATE);
        dataHelper.deleteSiteAuditImages();

        CustomUtility.setSharedPreference(mContext, Constant.LocalConveyance, "0");
        CustomUtility.clearSharedPreference(mContext);

        stopService(new Intent(getApplicationContext(), LocationUpdateService.class));
        Intent mIntent = new Intent(MainActivity1.this, LoginActivity.class);
        startActivity(mIntent);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }



    @Override
    protected void onResume() {
        super.onResume();

/*
        if(!userType.equalsIgnoreCase("1")){

            dataHelper.deleteTableData(DatabaseHelper.TABLE_ASSGIN_COMPLAIN_SUBORDINATE);
            downloadSubordinateData();
        }
*/

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
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
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
            } else {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // Permission denied.
                    Snackbar.make(
                            findViewById(R.id.lin1),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, view -> {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            })
                            .show();
                }
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
                    .setAction(R.string.ok, view -> ActivityCompat.requestPermissions(MainActivity1.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_PERMISSIONS_REQUEST_CODE))
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(MainActivity1.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
