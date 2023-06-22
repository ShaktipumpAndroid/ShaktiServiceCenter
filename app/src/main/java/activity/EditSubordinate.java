package activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import bean.SubordinateBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class EditSubordinate extends AppCompatActivity {

    private SubordinateBean subordinateBean;
    AlertDialog dialog;
    Button submit;
    Toolbar toolbar;
    String passwordtxt=null,statetxt=null,districttxt=null,sDatetxt=null,eDatetxt=null;
    Context context;
    DatabaseHelper db;
    Spinner spinner_state,spinner_district;
    String spinner_state_txt = null , spinner_district_txt = null , spinner_stateid= null , spinner_districtid= null ;
    TextView mobile= null , name= null , aadhar= null ;
    EditText start_date= null , end_date= null , password= null ;
    private String mStart= null , mEnd= null ;
    int index , index1 ;
    List<String> list_state_inst = null;
    List<String> list_district_inst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subordinate);

        context =this;
        db = new DatabaseHelper(context);
        Bundle bundle = getIntent().getExtras();
        subordinateBean = bundle.getParcelable("subordinateDetail");

        mobile = findViewById(R.id.mobileNo);
        name = findViewById(R.id.name);
        aadhar = findViewById(R.id.aadharCard);
        password = findViewById(R.id.password);
        start_date =findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        spinner_state =  findViewById(R.id.spinner_state);
        spinner_district =  findViewById(R.id.spinner_district);
        submit = findViewById(R.id.send);

        start_date.setFocusable(false);
        end_date.setFocusable(false);

        spinner_state.setPrompt("Select State");
        spinner_district.setPrompt("Select District");

        list_state_inst    = new ArrayList<>();
        list_district_inst = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_subordinate));

        setValue();


        setData();
        setSpinner();

        submitData();
    }

    private void setData() {
        passwordtxt = password.getText().toString();
        districttxt = spinner_district_txt;
        statetxt = spinner_state_txt;
        setDate();
    }

    private void submitData() {

        submit.setOnClickListener(v -> {

            setData();
            Log.e("VALUE","passwordtxt"+ password.getText().toString()+"districttxt"+spinner_district_txt +"statetxt"+spinner_state_txt);

            checkDataValidation();
            if(!passwordtxt.isEmpty() && passwordtxt!= null
                    && !statetxt.isEmpty() && statetxt !=null
                    && !districttxt.isEmpty() && districttxt != null
                    && !mStart.isEmpty() && mStart != null
                    && !mEnd.isEmpty() && mEnd != null ){

                 new editDataDetails().execute();
              //  Log.e("SERVER","data1:: p"+ passwordtxt+" s"+ statetxt+" d"+ districttxt+" sd"+ mStart+" ed"+ mEnd+" ");

            }
            else{
                passwordtxt = null;
                new editDataDetails().execute();
                //  Log.e("SERVER","data2:: p"+ passwordtxt+" s"+ statetxt+" d"+ districttxt+" sd"+ mStart+" ed"+ mEnd+" ");

            }



        });
    }


    private void setSpinner() {

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
                             //   Toast.makeText(context, "Please Select District.", Toast.LENGTH_SHORT).show();
                                CustomUtility.setSharedPreference(context, "districtid", "");
                                CustomUtility.setSharedPreference(context, "districttext", "");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                } else {
                  //  Toast.makeText(context, "Please Select State.", Toast.LENGTH_SHORT).show();
                    CustomUtility.setSharedPreference(context, "stateid", "");
                    CustomUtility.setSharedPreference(context, "statetext", "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void setDate() {

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

    private void setValue() {
        mobile.setText(subordinateBean.getMobileNo());
        aadhar.setText(subordinateBean.getAadharNo());
        name.setText(subordinateBean.getName());
    }

    // toolbar back button code.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;

        }
        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("deprecation")
    private class editDataDetails extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap;
            String invc_done;
            String obj2 = null;

            final ArrayList<NameValuePair> param1_invc = new ArrayList<>();

            param1_invc.add(new BasicNameValuePair("mobile", subordinateBean.getMobileNo()));///array name lr_save
            param1_invc.add(new BasicNameValuePair("state", statetxt));
            param1_invc.add(new BasicNameValuePair("district", districttxt));
            param1_invc.add(new BasicNameValuePair("to_dt", eDatetxt));
            param1_invc.add(new BasicNameValuePair("frm_dt", sDatetxt));
            param1_invc.add(new BasicNameValuePair("pwd",passwordtxt));

            Log.e("DATA", "$$$$" + param1_invc);

            System.out.println(param1_invc);

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVEEDITDETAIL, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);
                JSONObject object = new JSONObject(obj2);
                String stauts = object.getString("status");

                if(stauts.equalsIgnoreCase("true")){

                    Message msg = new Message();
                    msg.obj = "Data Submitted Successfully...";
                    mHandler.sendMessage(msg);
                    progressDialog.dismiss();
                    finish();

                }
                else {
                    Message msg = new Message();
                    msg.obj = "Failed .....";
                    mHandler.sendMessage(msg);

                    progressDialog.dismiss();
                    finish();

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
            progressDialog.dismiss();  


        }
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


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(EditSubordinate.this, mString, Toast.LENGTH_LONG).show();

        }
    };
}