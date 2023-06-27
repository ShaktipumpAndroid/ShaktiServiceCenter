package activity.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.ArrayList;
import java.util.Locale;

import activity.BaseActivity;
import activity.CustomUtility;
import database.DatabaseHelper;
import searchlist.complaint.SearchComplaint;
import searchlist.complaint.SearchSerailNoHistoryListViewAdapter;
import webservice.SAPWebService;

public class SerailNoHistoryActivity extends BaseActivity {
    String lv_download = "null", cmp_matnr = "", cmp_no = "", cmp_sernr = "";
    EditText editsearch;
    ListView list;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContex;
    SAPWebService con = null;
    private final int WAIT_TIME = 3000;
    //    SearchClosedComplaintListViewAdapter adapter;
    SearchSerailNoHistoryListViewAdapter adapter;
    ArrayList<SearchComplaint> arraylist = new ArrayList<SearchComplaint>();
    ;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(SerailNoHistoryActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serail_no_history);
        mContex = this;
        progressDialog = new ProgressDialog(mContex);

        SerailNoHistoryActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        progressDialog = new ProgressDialog(mContex);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Serial No. History");

        list = (ListView) findViewById(R.id.listview);



        //Log.d("lv_cmpno",lv_cmpno);

        Bundle bundle = getIntent().getExtras();
        lv_download = bundle.getString("lv_download");
        cmp_no = bundle.getString("cmp_no");
        cmp_sernr = bundle.getString("lv_sernr");
        cmp_matnr = bundle.getString("lv_matnr");


        // Log.d("cmp_sernr",cmp_sernr);


        if (lv_download.equalsIgnoreCase("null")) {
            DatabaseHelper dataHelper = new DatabaseHelper(mContex);

            downloadData(cmp_no,cmp_sernr,cmp_matnr);
        }





        // Pass results to ListViewAdapter Class
        adapter = new SearchSerailNoHistoryListViewAdapter(this, arraylist);


        // Binds the Adapter to the ListView
        list.setAdapter(adapter);


        con = new SAPWebService();

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_material_analysis, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_menu_material_analysis:
                downloadData(cmp_no,cmp_sernr,cmp_matnr);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadData(String cmpno,String cmpsernr,String cmpmatnr) {


        progressDialog = ProgressDialog.show(SerailNoHistoryActivity.this, "", "Downloading Serial Number History...!");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // do something...
                if (CustomUtility.isOnline(SerailNoHistoryActivity.this)) {


                    try {

                        lv_download = con.getComplaintSerailNoHistory(SerailNoHistoryActivity.this, cmpno, cmpsernr, cmpmatnr);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                        if (!lv_download.equalsIgnoreCase("null")) {

                            SerailNoHistoryActivity.this.finish();
                            Intent intent = new Intent(SerailNoHistoryActivity.this, SerailNoHistoryActivity.class);

                            intent.putExtra("lv_download", lv_download);
                            intent.putExtra("cmp_no", cmp_no);
                            intent.putExtra("lv_sernr", cmp_sernr);
                            intent.putExtra("lv_matnr", cmp_matnr);


                            startActivity(intent);

                        }

                        //selectTargetData() ;

                    } catch (Exception e) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Log.d("exce", "" + e);
                    }
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);

                }
            }
        }, 100);

    }



}
