package activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.shaktipumps.shakti.shaktiServiceCenter.BuildConfig;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.ArrayList;

import model.VersionResponse;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends Activity implements AnimationListener {

    // Splash screen timer
    //private static int SPLASH_TIME_OUT = 10000;
    private static int SPLASH_TIME_OUT = 3000;
    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
    // DatabaseHelper databaseHelper;
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
    String userType = "";

    @Override
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContex = this;
        /******* Create SharedPreferences *******/

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        versionName = BuildConfig.VERSION_NAME;

        if(versionName.equalsIgnoreCase(""))
        {
            versionName = "1.1";
        }
        deleteCache(mContex);

        new Worker1().execute();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                String data = null;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                if (pref.getString("key_login", "login").equalsIgnoreCase("Y")) {

                    i = new Intent(SplashActivity.this, MainActivity1.class);

                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();


            }
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
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation


    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }


    public void getVersion() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<VersionResponse> call = apiService.getVersionCode();
        call.enqueue(new Callback<VersionResponse>() {
            @Override
            public void onResponse(@NonNull Call<VersionResponse> call, @NonNull Response<VersionResponse> response) {
                try {
                    VersionResponse dashResponse = response.body();
                    if (dashResponse != null) {

                        newVersion = dashResponse.getVersion();
                        Log.e("VERSION", "&&&&" + newVersion);

                       // Toast.makeText(SplashActivity.this, "Success...", Toast.LENGTH_SHORT).show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VersionResponse> call, @NonNull Throwable t) {

                Toast.makeText(SplashActivity.this, "FAILED...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class Worker1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isOnline(mContex)) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    getVersion();


                }

            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


}