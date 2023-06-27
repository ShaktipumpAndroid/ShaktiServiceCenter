package activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import activity.retrofit.APIInterface;
import activity.retrofit.ApiClient;
import activity.retrofit.Model.Register.RegisterResponse;
import database.DatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends BaseActivity {

    Context context;
    APIInterface apiInterface;
    EditText mobileNo;
    EditText name;
    EditText password;
    EditText aadharCard;
    EditText rePassword;
    Button send;
    int index, index1;
    String mobileNo_txt= null,name_txt=null,district_txt=null,password_txt=null,state_txt=null,aadharCard_txt=null,rePassword_txt=null, userid=null;
    private EditText start_date, end_date;
    AlertDialog dialog;
    private String mStart, mEnd;
    String spinner_state_txt, spinner_district_txt, spinner_stateid, spinner_districtid;
    DatabaseHelper db;
    Spinner spinner_state,spinner_district;
    List<String> list_state_inst = null;
    List<String> list_district_inst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context =this;

        userid = CustomUtility.getSharedPreferences(context,"username");
        initView();
        save();
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        db = new DatabaseHelper(context);

        list_state_inst    = new ArrayList<>();
        list_district_inst = new ArrayList<>();

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Subordinate Details");

        mobileNo =findViewById(R.id.mobileNo);
        name =findViewById(R.id.name);
        password = findViewById(R.id.password);
        aadharCard =findViewById(R.id.aadharCard);
        rePassword = findViewById(R.id.repassword);
        spinner_state =  findViewById(R.id.spinner_state);
        spinner_district =  findViewById(R.id.spinner_district);

        start_date =  findViewById(R.id.start_date);
        end_date =  findViewById(R.id.end_date);

        start_date.setFocusable(false);
        end_date.setFocusable(false);


        send =findViewById(R.id.send);

        spinner_state.setPrompt("Select State");
        spinner_district.setPrompt("Select District");

        list_state_inst.clear();
        list_state_inst = db.getStateDistrictList(DatabaseHelper.KEY_STATE_TEXT, null);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(context, R.layout.spinner_item_center, list_state_inst);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(R.layout.spinner_item_center);
        // attaching data adapter to spinner
        spinner_state.setAdapter(dataAdapter1);
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                spinner_state_txt = spinner_state.getSelectedItem().toString();

                list_district_inst.clear();
                list_district_inst = db.getStateDistrictList(DatabaseHelper.KEY_DISTRICT_TEXT, spinner_state_txt);
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, R.layout.spinner_item_center, list_district_inst);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

                // attaching data adapter to spinner
                spinner_district.setAdapter(dataAdapter);

                if (!spinner_state_txt.equalsIgnoreCase("Select State") && !TextUtils.isEmpty(spinner_state_txt)) {
                    spinner_stateid = db.getStateDistrictValue(DatabaseHelper.KEY_STATE, spinner_state_txt);

                    CustomUtility.setSharedPreference(context, "statetext", spinner_state_txt);
                    CustomUtility.setSharedPreference(context, "stateid", spinner_stateid);

                    Log.e("ID", "&&&" + spinner_stateid);

                    spinner_district.setSelection(0);

                    spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                            index1 = arg0.getSelectedItemPosition();
                            spinner_district_txt = spinner_district.getSelectedItem().toString();

                            if (!spinner_district_txt.equalsIgnoreCase("Select District") && !TextUtils.isEmpty(spinner_district_txt)) {

                                spinner_districtid = db.getStateDistrictValue(DatabaseHelper.KEY_DISTRICT, spinner_district_txt);

                                CustomUtility.setSharedPreference(context, "districtid", spinner_districtid);
                                CustomUtility.setSharedPreference(context, "districttext", spinner_district_txt);

                                Log.e("ID1", "&&&" + spinner_districtid);
                            } else {
                                Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                                CustomUtility.setSharedPreference(context, "districtid", "");
                                CustomUtility.setSharedPreference(context, "districttext", "");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                    Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
                    CustomUtility.setSharedPreference(context, "stateid", "");
                    CustomUtility.setSharedPreference(context, "statetext", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        start_date.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                start_date.setText(i2 + "/" + i1 + "/" + i);
                mStart = start_date.getText().toString().trim();
                parseDateToddMMyyyy1(mStart);
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("Start Date");
            datePickerDialog.show();
        });

        // Date help for leave to
        end_date.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                end_date.setText(i2 + "/" + i1 + "/" + i);
                mEnd = end_date.getText().toString().trim();
                parseDateToddMMyyyy2(mEnd);
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle("End Date");
            datePickerDialog.show();
        });
    }

    public void parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        try {
            date = inputFormat.parse(time);
            assert date != null;
            mStart = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void parseDateToddMMyyyy2(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;


        try {
            date = inputFormat.parse(time);
            assert date != null;
            mEnd = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void save() {

        send.setOnClickListener(v -> {

            mobileNo_txt = mobileNo.getText().toString();
            name_txt = name.getText().toString();
            aadharCard_txt = aadharCard.getText().toString();
            district_txt = spinner_district_txt;
            state_txt = spinner_state_txt;
            password_txt = password.getText().toString().trim();
            rePassword_txt = rePassword.getText().toString().trim();

            checkDataValidation();
            if(!mobileNo_txt.isEmpty() && !name_txt.isEmpty() && !aadharCard_txt.isEmpty() && !district_txt.isEmpty() && !state_txt.isEmpty() && !password_txt.isEmpty() && !rePassword_txt.isEmpty() && mobileNo_txt != null && name_txt != null && aadharCard_txt != null && password_txt != null && rePassword_txt != null){

                 if(mobileNo_txt.length() == 10 && aadharCard_txt.length() == 12){
                     if(password.getText().toString().trim().equals(rePassword.getText().toString().trim()))
                     {
                          sendDatatoSap();

                     }
                     else {
                         Toast.makeText(context, "Password Don't Match.", Toast.LENGTH_SHORT).show();
                     }
                 }
                 else {
                     Toast.makeText(context, "Enter valid mobile no and aadhar card no", Toast.LENGTH_SHORT).show();
                 }
            }
            else{
                Toast.makeText(context, "Please enter all field.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void checkDataValidation() {
        try {
            if (mStart == null || mStart.equalsIgnoreCase("")) {
                start_date.setFocusable(true);
                start_date.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_select_start), Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
            } else if (mEnd == null || mEnd.equalsIgnoreCase("")) {
                end_date.setFocusable(true);
                end_date.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_select_end), Toast.LENGTH_SHORT).show();
                if (dialog != null)
                    dialog.dismiss();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.layout_loading);
                dialog = builder.create();
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private void sendDatatoSap() {

        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("MOBILE",mobileNo_txt);
            jsonObj.put("NAME",name_txt);
            jsonObj.put("AADHAR",aadharCard_txt);
            jsonObj.put("DISTRICT",district_txt);
            jsonObj.put("STATE",state_txt);
            jsonObj.put("PASSWARD",password_txt);
            jsonObj.put("Date_from",mStart);
            jsonObj.put("Date_to",mEnd);
            jsonObj.put("kunnr",userid);

            ja_invc_data.put(jsonObj);

            Log.e("Data====>",""+ja_invc_data);

        } catch (Exception e) {
            e.printStackTrace();
        }
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<RegisterResponse> call = apiInterface.sendData(ja_invc_data);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                Log.e("DATA",response.body().message);
                Log.e("DATA",response.body().status);

                assert response.body() != null;
                if (response.body().status.equalsIgnoreCase("true"))
                {
                    Toast.makeText(Register.this, response.body().message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Register.this,  response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                call.cancel();
            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}