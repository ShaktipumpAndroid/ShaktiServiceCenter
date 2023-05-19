package activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktiServiceCenter.BuildConfig;
import com.shaktipumps.shakti.shaktiServiceCenter.R;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ModelVK.LoginResponse;
import activity.complaint.ComplaintSearchActivity;
import bean.LoginBean;
//import ch.acra.acra.BuildConfig;
import database.DatabaseHelper;
import other.PermissionsIntent;
import syncdata.SyncDataToSAP_New;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    String checkUSERId = "0";


    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ProgressDialog progressBar;
    SAPWebService con = null;
    DatabaseHelper dataHelper = null;
    String username, password, login, ename,spinner_login_type_text;
    //TextView btnforgot;
    SharedPreferences pref;
    List<String> list = null;
    Context mContext;
    String latitude = "0.0";
    String longitude = "0.0";
    int PERMISSION_ALL = 1;
    private List<LoginResponse> mLoginResponseList;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    SharedPreferences.Editor editor;
    boolean flag;
    int index1;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(LoginActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    //private Spinner spinner_login_type;

    private EditText inputName, inputPassword;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private TextView btnSignUp;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void check_Permission(final Context context) {

//        ActivityCompat.requestPermissions((Activity) context,
//                new String[]{Manifest.permission.READ_PHONE_STATE},
//                PermissionsIntent.READ_PHONE_STATE);

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PermissionsIntent.READ_PHONE_STATE);

        } else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PermissionsIntent.READ_PHONE_STATE);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new1);
        mContext = this;

        progressDialog = new ProgressDialog(mContext);

        mLoginResponseList = new ArrayList<>();
        LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dataHelper = new DatabaseHelper(this);

        list = new ArrayList<String>();

        getUserTypeValue();

        inputLayoutName = findViewById(R.id.input_layout_name);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);


        inputLayoutPassword = findViewById(R.id.input_layout_password);
       // spinner_login_type = (Spinner) findViewById(R.id.spinner_login_type);
        inputName = findViewById(R.id.login_Et);

        inputPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btn_signup);
      //  btnforgot = findViewById(R.id.tv_forgot);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        // inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        con = new SAPWebService();

        // if(checkAndRequestPermissions()) {

/******* Create SharedPreferences *******/

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(mContext, "Please select login type", Toast.LENGTH_SHORT).show();
                }
                else {

                    RadioButton radioButton = radioSexGroup.findViewById(selectedId);

                    // Now display the value of selected item
                    // by the Toast message
                  //  checkUSERId = Integer.parseInt(radioButton.getTag().toString());
                    checkUSERId = radioButton.getTag().toString();
                    Toast.makeText(mContext, ""+checkUSERId, Toast.LENGTH_SHORT).show();
                }


                submitForm();


            }
        });


    }

    public void getUserTypeValue() {
        list.add("Select Login Type");
        list.add("Employee");
        list.add("Vendor");
        list.add("Service Center");
    }



    /**********************************************************************************************
     *                Server Login
     *********************************************************************************************/




    private void serverLoginService() {

        ArrayList<String> al;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        username = inputName.getText().toString();
        password = inputPassword.getText().toString();


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("userid", username));
        param.add(new BasicNameValuePair("pass", password));
        param.add(new BasicNameValuePair("type", checkUSERId));

        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(LoginActivity.this)) {

                    try {

                        String obj = CustomHttpClient.executeHttpPost1(WebURL.LOGIN_VK_PAGE, param);

                        Log.d("login_obj", "" + obj);

                        if (!obj.equalsIgnoreCase("")) {
/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                            JSONObject jo = new JSONObject(obj);
                            String mStatus = jo.getString("status");
                            final String mMessage = jo.getString("message");
                            String jo11 = jo.getString("response");
                            if (mStatus.equalsIgnoreCase("true")) {
                                if(mLoginResponseList.size()>0)
                                    mLoginResponseList.clear();
                                JSONArray ja = new JSONArray(jo11);
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject join = ja.getJSONObject(i);
                                    editor.putString("key_login", "Y");
                                    editor.putString("key_username", username);
                                    editor.putString("key_ename", ename);

                                    editor.commit(); //

                                     CustomUtility.setSharedPreference(mContext,"userType",checkUSERId);
                                    CustomUtility.setSharedPreference(mContext,"userID",join.getString("kunnr"));
                                    CustomUtility.setSharedPreference(mContext,"ServiceCenterName",join.getString("name1"));
                                 }
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                };
                                Message msg1 = new Message();
                                msg1.obj = mMessage;
                                mHandler.sendMessage(msg1);
                                Intent mIntent ;

                                if(checkUSERId.equalsIgnoreCase("1"))
                                    mIntent = new Intent(LoginActivity.this, MainActivity1.class);
                                else
                                    mIntent = new Intent(LoginActivity.this, MainActivity2.class);
                                startActivity(mIntent);
                                finish();


                            }
                            else
                            {

                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                };
                                Message msg1 = new Message();
                                msg1.obj = "Invalid username or password";
                                mHandler.sendMessage(msg1);
                            }
                            //JSONArray ja = new JSONArray(obj);
                            //Log.d("ja", "" + ja);

/******************************************************************************************/
/*                       Call DashBoard
/******************************************************************************************/

                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Log.d("exce", "" + e);
                        e.printStackTrace();
                    }


                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection";
                    mHandler.sendMessage(msg2);


                }


            }

        }.start();
    }

    /**********************************************************************************************
     *                Validating form
     *********************************************************************************************/
    private void submitForm() {


        if (!validateName()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }


/********************   Server Login    *******************************************************/



        serverLoginService();

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateOnline() {


        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Connecting to server");


        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isOnline(LoginActivity.this)) {

                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    flag = true;

                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    flag = false;
                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);


                }

            }
        }).start();


        return flag;


//
//        if (CustomUtility.isOnline(LoginActivity.this)) {
//
//            return  true;
//        }
//
//        else {
//
//            Toast.makeText(LoginActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
//        }


    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean checkAndRequestPermissions() {
        int permissionNetworkState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionInternet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
//        int permissionStorage = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

//        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }


        if (permissionNetworkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }

//        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    LoginActivity.this.finish();
                    System.exit(0);

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.login_Et:
                    validateName();
                    break;
                //   case R.id.input_email:
                //     validateEmail();
                //    break;
                // case R.id.input_password:
                //  validatePassword();
                //   break;
            }
        }

    }

    /*public void syncOfflineData() {

        GPSTracker gps = new GPSTracker(mContext);
        latitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude())));
        longitude = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude())));


        dataHelper.insertEmployeeGPSActivity(
                LoginBean.userid,
                new CustomUtility().getCurrentDate(),
                new CustomUtility().getCurrentTime(),
                "17",
                latitude,
                longitude,
                mContext,
                "");


    }*/
}
