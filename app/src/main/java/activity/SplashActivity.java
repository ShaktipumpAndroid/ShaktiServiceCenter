package activity;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static activity.complainvk.Config.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.ArrayList;

import activity.utility.Constant;
import bean.AppConfig;


public class SplashActivity extends BaseActivity  {

    private static final int REQUEST_CODE_PERMISSION = 2;
    private static int SPLASH_TIME_OUT = 3000;
    final ArrayList<NameValuePair> param = new ArrayList<>();
    Intent i;
    ImageView imageView;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String current_date = "null", current_time;
    Context mContex;
    // Animation
    Animation anim;
    String versionName = "0.0";
    String newVersion = "0.0";
    String userType = "" ,username= "";
    public static DocumentReference appConfigRef;

    @Override
     protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);
        mContex = this;
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        deleteCache(mContex);


    }

    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED  && ReadMediaImages == PackageManager.PERMISSION_GRANTED;

        }else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.ACCESS_COARSE_LOCATION,
                            READ_MEDIA_IMAGES},
                    REQUEST_CODE_PERMISSION);
        }  else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {

                    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        boolean CoarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (CoarseLocationAccepted  && Camera && ReadMediaImages ) {
                            // perform action when allow permission success
                            openNextActivity();
                        }else {
                            requestPermission();
                        }
                    } else   {
                        boolean  FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;


                        if(FineLocationAccepted && CoarseLocationAccepted && Camera && ReadPhoneStorage && WritePhoneStorage ){
                            openNextActivity();
                        }else {
                            requestPermission();
                        }
                    }
                }
                break;
        }
    }

    private void openNextActivity() {
        new Handler().postDelayed(() -> {

            userType = CustomUtility.getSharedPreferences(mContex , "userType");
            //username
            String data = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            if (pref.getString("key_login", "login").equalsIgnoreCase("Y")) {

                i = new Intent(SplashActivity.this, MainActivity1.class);

            } else if (userType.equalsIgnoreCase("2")){

                i = new Intent(SplashActivity.this, MainActivity1.class);

            }else {
                i = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(i);
            finish();


        }, SPLASH_TIME_OUT);
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
    protected void onResume() {
        super.onResume();
        retriveFirestoreData();

    }

    private void retriveFirestoreData() {

        if(CustomUtility.isInternetOn(getApplicationContext())) {
            appConfigRef = FirebaseFirestore.getInstance().collection("Setting").document("AppConfig");
            appConfigRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        AppConfig appConfig = documentSnapshot.toObject(AppConfig.class);
                        Log.e("AppConfig====>",appConfig.getServiceCenterAppURL());

                        if (appConfig != null) {
                            try {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                if (pInfo != null && appConfig.getServiceCenterAppVersion() != null
                                        && !appConfig.getServiceCenterAppVersion().isEmpty()) {

                                    if (pInfo.versionCode < Integer.parseInt(appConfig.getServiceCenterAppVersion())) {
                                        CustomUtility.setSharedPreference(getApplicationContext(), Constant.APPURL, appConfig.getServiceCenterAppURL());
                                        Intent intent = new Intent(SplashActivity.this, SwVersionCheckActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        if(checkPermission()){
                                            openNextActivity();
                                        }
                                        else {
                                            requestPermission();
                                        }

                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }
}