package activity.complaint;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import activity.BaseActivity;
import activity.CustomUtility;
import bean.LoginBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.SAPWebService;

public class ComplaintDashboard extends BaseActivity {
    TextView open_complaint, btn_review_complaint, btn_close_complaint, btn_new_servie_center, btn_old_servie_center,btn_cmpln_approved,
            btn_new_complaint_notification, btn_delar_pend_notification, btn_pending_complaint_notification,
            btn_closure_notification, btn_closer_request,btn_pending_for_app,btn_await_for_app, btn_pending_complaint,
            btn_delar_pend_cmp,btn_pending_for_app_notification,btn_await_for_app_notification,btn_cmpln_approved_notification;
    SAPWebService con = null;
    DatabaseHelper dataHelper = null;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userid;

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ComplaintDashboard.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_dashboard);
        context = this;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(context);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Management");


        dataHelper = new DatabaseHelper(this);
        open_complaint = (TextView) findViewById(R.id.open_complaint);
        btn_review_complaint = (TextView) findViewById(R.id.review_complaint);
        btn_close_complaint = (TextView) findViewById(R.id.close_complaint);
        btn_pending_complaint = (TextView) findViewById(R.id.pending_complaint);
        btn_closer_request = (TextView) findViewById(R.id.closer_request);
        btn_delar_pend_cmp = (TextView) findViewById(R.id.delar_pend_cmp);
        btn_pending_for_app = (TextView) findViewById(R.id.pending_for_app);
        btn_await_for_app = (TextView) findViewById(R.id.await_for_app);

        //btn_new_servie_center = (TextView) findViewById(R.id.new_service_center);
        btn_old_servie_center = (TextView) findViewById(R.id.old_service_center);
        btn_cmpln_approved = (TextView) findViewById(R.id.cmpln_approved1);


        btn_new_complaint_notification = (TextView) findViewById(R.id.new_complaint_notification);
        btn_delar_pend_notification   =(TextView) findViewById(R.id.delar_pend_notification);
        btn_pending_complaint_notification = (TextView) findViewById(R.id.pending_complaint_notification);
        btn_closure_notification = (TextView) findViewById(R.id.closure_notification);
        btn_pending_for_app_notification = (TextView) findViewById(R.id.pending_for_app_notification);
        btn_await_for_app_notification = (TextView) findViewById(R.id.await_for_app_notification);
        btn_cmpln_approved_notification = (TextView) findViewById(R.id.cmpln_approved_notification);

        con = new SAPWebService();

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        userid = pref.getString("key_username", "userid");


        Log.d("d_key", ""+ pref.getString("key_download_complaint","date"));
        if (!pref.getString("key_download_complaint", "date").equalsIgnoreCase(new CustomUtility().getCurrentDate())) {
            download_complaint_data();
        }


        open_complaint.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, ComplaintSearchActivity.class);
                intent.putExtra("complaint", "New Complaint");
                startActivity(intent);
            }
        });

        btn_delar_pend_cmp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, DealerPendCompActivity.class);
                intent.putExtra("complaint", "Dealer Complaint");
                startActivity(intent);
            }
        });


        btn_review_complaint.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


            //    Intent intent = new Intent(context, ComplaintReviewFilter.class);//pupupu
              //  intent.putExtra("complaint", "Review Complaint");
               // startActivity(intent);
            }
        });


//        btn_close_complaint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getCloseComplaint();
//
//                Intent intent = new Intent(context, ClosedComplaintActivity.class);
//                intent.putExtra("complaint","Closed Complaint");
//                startActivity(intent);
//            }
//        });


        btn_pending_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // complaint count


                Intent intent = new Intent(context, PendingComplaintActivity.class);

                startActivity(intent);
            }
        });

        btn_closer_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getClosureComplaint();

                Intent intent = new Intent(context, ClosureComplaintActivity.class);
                startActivity(intent);
            }
        });


        btn_old_servie_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ServiceCenterActivity.class);
                startActivity(intent);
            }
        });

       /* btn_new_servie_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, NewServiceCenterActivity.class);
                startActivity(intent);
            }
        });*/

        btn_cmpln_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ApprovedCompActivity.class);
                startActivity(intent);
            }
        });

        btn_pending_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PendingForApprovalActivity.class);
                intent.putExtra("complaint", "Pending for Complaint");
                startActivity(intent);
            }
        });

        btn_await_for_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AwaitingForApprovalActivity.class);
                intent.putExtra("complaint", "Awaiting for Complaint");
                startActivity(intent);
            }
        });



        //***************** set notifications count *********************************



        btn_delar_pend_notification.setText(String.valueOf( getDealerpendComplaint() ));
       if (getDealerpendComplaint() <= 0) {
            btn_delar_pend_notification.setTextColor(Color.parseColor("#ffffff"));
      }


        btn_closure_notification.setText(String.valueOf(getClosureComplaint()));
        if (getClosureComplaint() <= 0) {
            btn_closure_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_pending_for_app_notification.setText(String.valueOf(getPendingforappComplaint()));
        if (getPendingforappComplaint() <= 0) {
            btn_pending_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_await_for_app_notification.setText(String.valueOf(getAwaitforappComplaint()));
        if (getAwaitforappComplaint() <= 0) {
            btn_await_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_cmpln_approved_notification.setText(String.valueOf(getCmplnapproved()));
        if (getCmplnapproved() <= 0) {
            btn_cmpln_approved_notification.setTextColor(Color.parseColor("#ffffff"));
        }

      //  callgetCompalinAllListAPI();
        //***************** end notifications count *********************************
    }


    public void callgetCompalinAllListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();

        param.add(new BasicNameValuePair("id", "9500001850"));///Pending Complaint
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));

        //  jsonObject.addProperty("lrno", mLRNumberValue);
        // jsonObject.addProperty("mobno", mLRMobileValue);

        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));
        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(getApplicationContext(), "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1("https://solar10.shaktisolarrms.com/RMSAppTest/MoterParamList", param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);


                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {



                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "mStatus==>>"+mStatus+"\nmMessage==>>"+mMessage, Toast.LENGTH_SHORT).show();
                              /*  mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse, mStatusValue);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);*/
                                // addDynamicViewProNew(mSettingParameterResponse);
                                progressDialog.dismiss();
                            }


                        });

/*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Intent mIntent = new Intent(ActivityPODSearchInfo.this, LrtransportList.class);
                                    mIntent.putExtra("InvoiceList", (Serializable) mLrInvoiceResponse);
                                    startActivity(mIntent);
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }


                        });*/


                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_SHORT).show();
                               /* mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);*/
                                progressDialog.dismiss();
                            }


                        });
                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                    // dismiss the progress dialog
                    progressDialog.dismiss();
                }

            }

        }.start();

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

            case R.id.action_download_complaint:

                download_complaint_data();
                return true;

            case R.id.action_download_service_center:
                download_service_center_data();
                return true;

            case R.id.action_sync_offline:
                // sync all data
                syncOfflineData();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }




    public int getCmplnapproved() {
        int tot_pending_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

//        try {
//
//            selectQuery = "SELECT * FROM "+ DatabaseHelper.TABLE_ZINPROCESS_COMPLAINT ;
//
//            cursor = db.rawQuery(selectQuery, null);
//            // Log.d("cmp_pending",""+cursor.getCount());
//            if (cursor.getCount() > 0) {
//
//                tot_pending_complaint = cursor.getCount() ;
//
//                while (cursor.moveToNext())
//                {
//                    setUpdatePendingComplaint(cursor.getString(cursor.getColumnIndex("cmpno")));
//                }
//
//
//            }
//        }
//        catch (SQLiteException e) {
//            e.printStackTrace();
//        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        int tdDate = Integer.parseInt(df.format(c));


        try {

           /* selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "'" + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'";*/
            // +  " AND " + DatabaseHelper.KEY_SAVE_BY  + " = '" + "APP" + "'" ;
             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "NEW" + "' "
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " AND " + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'";
          /*  selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "'"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " != '" + "07" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " != '" + "Y" + "'";
*/

            cursor = db.rawQuery(selectQuery, null);
            // Log.d("cmp_pending",""+cursor.getCount());
            if (cursor.getCount() > 0) {
               /* while (cursor.moveToNext()) {

                    String fdate = cursor.getString(cursor.getColumnIndex("fdate"));
                    String save = cursor.getString(cursor.getColumnIndex("save_by"));

                    int fdate1 = 0;
                    if (!TextUtils.isEmpty(fdate) ) {
                        fdate1 = Integer.parseInt(CustomUtility.formateDate(fdate));
                    }

                    if (fdate1 > tdDate || !TextUtils.isEmpty(save)) {

                        tot_pending_complaint = tot_pending_complaint + 1;

                        Log.e("Count","&&&&"+tot_pending_complaint);
                    }
                }
*/
                tot_pending_complaint = tot_pending_complaint + 1;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_pending_complaint;
    }

    public int getClosureComplaint() {
        int tot_closure_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

//        try {
//
//           selectQuery = "SELECT * FROM "+ DatabaseHelper.TABLE_CLOSE_COMPLAINT ;
//
//            cursor = db.rawQuery(selectQuery, null);
//
//           // Log.d("closed_cmp",""+cursor.getCount());
//
//            if (cursor.getCount() > 0) {
//
//                tot_closure_complaint = cursor.getCount() ;
//
//                while (cursor.moveToNext())
//                {
//                    setUpdateClouserComplaint(cursor.getString(cursor.getColumnIndex("cmpno")));
//                }
//
//            }
//        }
//
//
//        catch (SQLiteException e) {
//            e.printStackTrace();
//        }


        try {

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "CLOSURE" + "'";
                   /* + " AND " + DatabaseHelper.KEY_SAVE_BY + " = '" + "APP" + "'";*/


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("closed_cmp",""+cursor.getCount());

            if (cursor.getCount() > 0) {

                // tot_closure_complaint =   tot_closure_complaint + cursor.getCount() ;


                tot_closure_complaint = cursor.getCount();


            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_closure_complaint;
    }

    @Override
    public void onResume() {

        //***************** set notifications count *********************************



//        btn_close_complaint_notification.setText(String.valueOf( getCloseComplaint() ));
//        if (getCloseComplaint() <= 0) {
//            btn_close_complaint_notification.setTextColor(Color.parseColor("#ffffff"));
//        }




        btn_closure_notification.setText(String.valueOf(getClosureComplaint()));
        if (getClosureComplaint() <= 0) {
            btn_closure_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_delar_pend_notification.setText(String.valueOf( getDealerpendComplaint() ));
        if (getDealerpendComplaint() <= 0) {
            btn_delar_pend_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_pending_for_app_notification.setText(String.valueOf(getPendingforappComplaint()));
        if (getPendingforappComplaint() <= 0) {
            btn_pending_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }

        btn_await_for_app_notification.setText(String.valueOf(getAwaitforappComplaint()));
        if (getAwaitforappComplaint() <= 0) {
            btn_await_for_app_notification.setTextColor(Color.parseColor("#ffffff"));
        }


        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complaint, menu);
        return true;
    }

    public void download_complaint_data() {

        progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Download Complaint...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {


                    try {

                        con.getCustomerComplaint(ComplaintDashboard.this);

                        editor.putString("key_download_complaint", new CustomUtility().getCurrentDate());
                        editor.commit(); //
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Log.d("exce", "" + e);
                    }
                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);


                }


            }

        }.start();

    }

    public void download_service_center_data() {

        progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Download Service Center List...!");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {
                    try {
                        con.getServiceCenterList(ComplaintDashboard.this);
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Log.d("exce", "" + e);
                    }
                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection, Downloading failed.";
                    mHandler.sendMessage(msg2);
                }


            }

        }.start();

    }

    public void syncOfflineData() {

       /* progressDialog = ProgressDialog.show(ComplaintDashboard.this, "", "Sync Offline Data !");


        new Thread(new Runnable() {
            @Override
            public void run() {

                if (CustomUtility.isOnline(ComplaintDashboard.this)) {


                    dataHelper.insertEmployeeGPSActivity(
                            LoginBean.userid,
                            new CustomUtility().getCurrentDate(),
                            new CustomUtility().getCurrentTime(),
                            "1",
                            "0.0",
                            "0.0",
                            context,
                            "");


                    new SyncDataToSAP_New().SendDataToSap(context);

                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);

                    //Toast.makeText(MainActivity.this, "No internet Connection. ", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();*/


    }


    public void setUpdateClouserComplaint(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "CLOSURE" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if(db!=null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }


        }
    }


    public void setUpdatePendingComplaint(String cmpln_cmpno) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);
        // Open the database for writing
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            String updateQuery = "UPDATE "
                    + dataHelper.TABLE_ZCMPLNHDR +
                    " SET cmpln_status = '" + "REPLY" +
                    "', save_by = '" + "APP" +
                    "' WHERE cmpno = '" + cmpln_cmpno + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            if(db!=null) {
                db.endTransaction();
            }
            if (c != null) {
                c.close();
            }
            if(db!=null) {
                db.close();
            }


        }
    }

    public int getDealerpendComplaint() {
        int tot_dela_pend_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

           /*  selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "'"
                     + " AND " + DatabaseHelper.KEY_PEN_RES + " = '" + "07" + "'"
                     + " AND " + DatabaseHelper.KEY_AWT_APR + " != '" + "Y" + "'";*/

            selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE (" + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "IN PROCESS" + "'"
                    + " OR " + DatabaseHelper.KEY_CMPLN_STATUS + " = '" + "REPLY" + "' )"
                    + " AND " + DatabaseHelper.KEY_PEN_RES + " = '" + "07" + "'"
                    + " AND (" + DatabaseHelper.KEY_AWT_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_AWT_APR + " = '" + "" + "' )"
                    + " AND (" + DatabaseHelper.KEY_PEND_APR + " = '" + "X" + "'"
                    + " OR " + DatabaseHelper.KEY_PEND_APR + " = '" + "" + "' )";



            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_dela_pend_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_dela_pend_complaint;
    }

    public int getPendingforappComplaint() {
        int tot_pend_for_app_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " != '" + "CLOSURE" + "'"
                    + " AND " + DatabaseHelper.KEY_PEND_PERNR + " = '" + userid + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "Y" + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_pend_for_app_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_pend_for_app_complaint;


    }

    public int getAwaitforappComplaint() {

        int tot_await_for_app_complaint = 0;


        DatabaseHelper dh = new DatabaseHelper(context);

        SQLiteDatabase db = dh.getReadableDatabase();

        String userid = LoginBean.getUseid();

        String selectQuery = null;
        Cursor cursor = null;

        try {

             selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ZCMPLNHDR
                    + " WHERE " + DatabaseHelper.KEY_CMPLN_STATUS + " != '" + "CLOSURE" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_APR + " = '" + "Y" + "'"
                    + " AND " + DatabaseHelper.KEY_AWT_PERNR + " = '" + userid + "'";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {

                tot_await_for_app_complaint = cursor.getCount();
                //count_dsr_entry = String.valueOf(cursor.getCount()) ;

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return tot_await_for_app_complaint;


    }
}
