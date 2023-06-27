package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ModelVK.LoginResponse;
import activity.retrofit.APIInterface;
import activity.retrofit.ApiClient;
import activity.retrofit.Model.LoginResponse.FiledLoginResponse;
import database.DatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class LoginActivity extends BaseActivity {

    private RadioGroup radioSexGroup;
    Intent mIntent ;
    String checkUSERId = "0";

    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    SAPWebService con = null;
    DatabaseHelper dataHelper = null;
    String username, password,  eName;
    APIInterface apiInterface;
    SharedPreferences pref;
    List<String> list = null;
    Context mContext;

    private List<LoginResponse> mLoginResponseList;

    SharedPreferences.Editor editor;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(LoginActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private EditText inputName, inputPassword;
    private TextInputLayout inputLayoutName,  inputLayoutPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new1);
        mContext = this;

        progressDialog = new ProgressDialog(mContext);

        mLoginResponseList = new ArrayList<>();
        LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dataHelper = new DatabaseHelper(this);

        list = new ArrayList<>();

        getUserTypeValue();

        inputLayoutName = findViewById(R.id.input_layout_name);
        radioSexGroup =  findViewById(R.id.radioSex);

        inputLayoutPassword = findViewById(R.id.input_layout_password);
        inputName = findViewById(R.id.login_Et);

        inputPassword = findViewById(R.id.password);
        TextView btnSignUp = findViewById(R.id.btn_signup);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        con = new SAPWebService();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        btnSignUp.setOnClickListener(view -> {

            int selectedId = radioSexGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(mContext, "Please select login type", Toast.LENGTH_SHORT).show();
            }
            else {

                RadioButton radioButton = radioSexGroup.findViewById(selectedId);
                checkUSERId = radioButton.getTag().toString();
                CustomUtility.setSharedPreference(mContext,"userType",checkUSERId);
                Log.e("checkUSERId","checkUSERId"+checkUSERId);
            }

        if(CustomUtility.isOnline(mContext)){
            if (checkUSERId.equalsIgnoreCase("1")){
                submitForm();
            }else {
                if (!inputName.getText().toString().isEmpty() && !inputPassword.getText().toString().isEmpty()){
                    loginAPI();
                }else {
                    Toast.makeText(mContext, "Enter Username or password", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(mContext, "Please turn on Internet", Toast.LENGTH_SHORT).show();

        }
        });
    }

    private void loginAPI() {
        ProgressDialog progress;

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();


        username = inputName.getText().toString();
        password = inputPassword.getText().toString();

        CustomUtility.setSharedPreference(mContext,"username",username);

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<FiledLoginResponse> call = apiInterface.login(username,password);

        Log.e("URL====>",call.request().url().toString());
        call.enqueue(new Callback<FiledLoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<FiledLoginResponse> call, @NotNull Response<FiledLoginResponse> response) {


                if(response.isSuccessful()){
                    assert response.body() != null;
                    Log.e("response=====>",response.body().toString());
                    if (response.body().status.equalsIgnoreCase("True"))
                    {
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this, "USER LOG IN SUCCESSFULLY ", Toast.LENGTH_SHORT).show();
                        CustomUtility.setSharedPreference(mContext,"userType",checkUSERId);
                        CustomUtility.setSharedPreference(mContext,"kunnr",response.body().response);

                        Log.e("kunnr====>",response.body().response);
                        mIntent = new Intent(LoginActivity.this, MainActivity1.class);
                        startActivity(mIntent);
                        finish();

                    } else {
                        progress.dismiss();
                        Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<FiledLoginResponse> call, @NotNull Throwable t) {
                progress.dismiss();
                call.cancel();

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        username = inputName.getText().toString();
        password = inputPassword.getText().toString();

        CustomUtility.setSharedPreference(mContext,"username",username);

        final ArrayList<NameValuePair> param = new ArrayList<>();
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
                                    editor.putString("key_ename", eName);

                                    editor.commit(); //


                                    CustomUtility.setSharedPreference(mContext,"userID",join.getString("kunnr"));
                                    CustomUtility.setSharedPreference(mContext,"ServiceCenterName",join.getString("name1"));
                                 }
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                                Message msg1 = new Message();
                                msg1.obj = mMessage;
                                mHandler.sendMessage(msg1);

                                mIntent = new Intent(LoginActivity.this, MainActivity1.class);
                                startActivity(mIntent);
                                finish();


                            }
                            else
                            {

                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                                Message msg1 = new Message();
                                msg1.obj = "Invalid username or password";
                                mHandler.sendMessage(msg1);
                            }
                            //JSONArray ja = new JSONArray(obj);
                            //Log.d("ja", "" + ja);

//******************************************************************************************/
/*                       Call DashBoard
/******************************************************************************************/
                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Log.d("exce", "" + e);
                        e.printStackTrace();
                    }

                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
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

//********************   Server Login    *******************************************************/
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission Denied

                LoginActivity.this.finish();
                System.exit(0);

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.login_Et) {
                validateName();

            }
        }
    }
}
