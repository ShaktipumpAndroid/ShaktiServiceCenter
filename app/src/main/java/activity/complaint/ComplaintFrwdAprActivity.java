package activity.complaint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.shaktipumps.shakti.shaktiServiceCenter.R;


import java.util.ArrayList;
import java.util.List;

import activity.CustomUtility;
import backgroundservice.SyncDataService;
import bean.ForwardForAppEntryBean;
import bean.LoginBean;
import database.DatabaseHelper;

public class ComplaintFrwdAprActivity extends AppCompatActivity {
    Context mContext;
    String cmp_no;
    ProgressDialog progressDialog;
    Spinner spinner;
    Button btn_dsr_save;
    int index;
    SharedPreferences pref;
    EditText remark;
    List<String> list = null;
    String pernr_no,userid;
    private TextInputLayout inputlayoutremark;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ComplaintFrwdAprActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_frwdapr);
        mContext = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_dsr_save = (Button) findViewById(R.id.btn_dsr_save);

        userid = LoginBean.getUseid();

        progressDialog = new ProgressDialog(mContext);

        inputlayoutremark = (TextInputLayout) findViewById(R.id.input_layout_remark);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Spinner_list = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        cmp_no = bundle.getString("cmp_no");

        pref = mContext.getSharedPreferences("MyPref", 0);

        String title = "Complaint No. " + cmp_no;
        getSupportActionBar().setTitle(title);

        list = new ArrayList<String>();
        getpernrValue();

        spinner = (Spinner) findViewById(R.id.spinner_frwd_pernr);

        remark = (EditText) findViewById(R.id.remark);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                pernr_no = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinner.setPrompt("Select Person No.");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);

        // Drop down layout style - list view with radio button
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        btn_dsr_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitForm()) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("Data Save alert !");
                    alertDialog.setMessage("Do you want to forward complaint for approval?");

                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });


                    // on pressing cancel button

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }


            }
        });

    }

    private boolean submitForm() {
        boolean value;
        if ((validateActivityType()) &&
                (validateComment())) {
            value = true;
        } else {
            value = false;
        }
        return value;
    }

    private boolean validateActivityType() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Person No.", Toast.LENGTH_SHORT).show();

            value = false;
        } else {
            value = true;
        }

        return value;
    }

    private boolean validateComment() {
        if (remark.getText().toString().trim().isEmpty()) {
            inputlayoutremark.setError("Please Enter Remark");

            requestFocus(remark);
            return false;
        }
        else {
            inputlayoutremark.setErrorEnabled(false);

        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getpernrValue() {
        list.add("Select Person No.");
        list.add("00000318--Vishnu Katariya");
        list.add("00001432--Rahul Gupta");
        list.add("0000960--Kamal Patidar");
    }



    public void SyncDsrEntryInBackground() {


        Intent i = new Intent(mContext, SyncDataService.class);
        startService(i);


    }


}
