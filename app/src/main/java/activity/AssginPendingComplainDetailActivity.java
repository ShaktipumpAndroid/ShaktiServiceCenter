package activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import activity.complainvk.SubordinateImageActivity;
import activity.utility.Constant;
import adapter.AssginPendingComplainDetailsListAdapter;
import bean.SubordinateAssginComplainBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class AssginPendingComplainDetailActivity extends AppCompatActivity {

    private Context mContext;
    private List<SubordinateAssginComplainBean> mComplainDetailListResponse;
    ProgressDialog progressDialog;
    private ImageView imgBackID;
    private String textRemarkValue;
    private  String mComplainNO= "";
    private  String mStatusValue= "";
    private  String mUserID= "";
    String mobile;
    public TextView txtBTNActionID,  txtBTNUploadID;
    private RecyclerView rclyPendingComplainList;

    private Intent mmIntent;
    private Dialog dialog ;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complain_detail);
        mContext = this;
        mmIntent = getIntent();

        initView();
        mobile = CustomUtility.getSharedPreferences(mContext,"username");
        db = new DatabaseHelper(mContext);
        // Database
        mComplainDetailListResponse = db.getSubordinateAssginComplainNo(mComplainNO);

        AssginPendingComplainDetailsListAdapter mPendingComplainDetailsListAdapter = new AssginPendingComplainDetailsListAdapter(mContext, mComplainDetailListResponse);
        rclyPendingComplainList.setAdapter(mPendingComplainDetailsListAdapter);

    }

    private void initView() {

        mUserID = CustomUtility.getSharedPreferences(mContext,"kunnr");

        mComplainDetailListResponse = new ArrayList<>();

        rclyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
        rclyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));

        imgBackID = findViewById(R.id.imgBackID);
        TextView txtHeaderID = findViewById(R.id.txtHeaderID);

        RelativeLayout rlvBottomViewID = findViewById(R.id.rlvBottomViewID);
        txtBTNUploadID =  findViewById(R.id.txtBTNUploadID);
        // txtBTNSaveID =  findViewById(R.id.txtBTNSaveID);

        txtBTNActionID = findViewById(R.id.txtBTNActionID);

        mmIntent.getStringExtra("mobile_number");
        mComplainNO = mmIntent.getStringExtra("Complain_number");
        String mHeaderTittle = mmIntent.getStringExtra("complaint");
        mStatusValue = mmIntent.getStringExtra("StatusValue");

        txtHeaderID.setText(mHeaderTittle);

        initClickEvent();
        //callgetCompalinAllListAPI();


        if(mStatusValue.equalsIgnoreCase("01"))
        {
            rlvBottomViewID.setVisibility(View.VISIBLE);
        }
        else
        {
            rlvBottomViewID.setVisibility(View.GONE);
        }

    }

    private void initClickEvent() {

        imgBackID.setOnClickListener(view -> finish());


        // Toast.makeText(mContext, "Action optiin working commimg soon", Toast.LENGTH_SHORT).show();
        txtBTNActionID.setOnClickListener(this::initRemarkViewBox);



        txtBTNUploadID.setOnClickListener(view -> {

            if (CustomUtility.getSharedPreferences(mContext,Constant.LocalConveyance).equalsIgnoreCase("1")){
                Intent intent = new Intent(mContext, SubordinateImageActivity.class);
                intent.putExtra("inst_id", mComplainDetailListResponse.get(0).getCmpno());
                intent.putExtra("cust_name", mComplainDetailListResponse.get(0).getEngg_name());
                intent.putExtra("StatusCheck", mStatusValue);

                mContext.startActivity(intent);
            }else {
                Toast.makeText(mContext, "Start the journey first ", Toast.LENGTH_SHORT).show();
            }



        });



    }

    private void initRemarkViewBox(View view) {

        EditText editText;
        TextView txtBTNRemarkID;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_remark_as_complain);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        editText = dialog.findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = dialog.findViewById(R.id.txtBTNRemarkID);
        dialog.setCancelable(true);

        dialog.show();

        txtBTNRemarkID.setOnClickListener(view1 -> {

            textRemarkValue = editText.getText().toString().trim();
            if(!textRemarkValue.equalsIgnoreCase(""))
            {
                // callInsertRemarkAPI(textRemarkValue);
                SyncLocalConveneinceDataToSap();
                new callInsertRemarkAPI().execute();
            }
            else
            {
                Toast.makeText(mContext, "Please enter remark!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();

        }
    };

    public void SyncLocalConveneinceDataToSap() {

        String docno_sap ;
        String invc_done ;
        progressDialog = ProgressDialog.show(AssginPendingComplainDetailActivity.this, getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));


        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("mobile",mobile );
            jsonObj.put("cmpno",mComplainNO);
            jsonObj.put("begda", "");
            jsonObj.put("endda", "");
            jsonObj.put("start_time", "");
            jsonObj.put("end_time", "");
            jsonObj.put("start_lat", "");
            jsonObj.put("end_lat", "");
            jsonObj.put("start_long","");
            jsonObj.put("end_long", "");

            jsonObj.put("start_location", "");

            jsonObj.put("end_location", "");

            jsonObj.put("distance", "0 km");
            jsonObj.put("TRAVEL_MODE", "");
            jsonObj.put("LAT_LONG", "");
                jsonObj.put("PHOTO1", "");
                jsonObj.put("PHOTO2", "");

            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final ArrayList<NameValuePair> param1_invc = new ArrayList<>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));


        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.LOCAL_CONVENIENVCE, param1_invc);

            if (!obj2.isEmpty()) {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");
                    if (invc_done.equalsIgnoreCase("S")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);


                        activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");

                        Intent intent = new Intent(AssginPendingComplainDetailActivity.this , MainActivity1.class);
                        startActivity(intent);

                    } else if (invc_done.equalsIgnoreCase("E")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                            activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");

                        }

                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }

    private class callInsertRemarkAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                // jsonObj.put("project_no", projno);
                jsonObj.put("cmpno",mComplainNO);

                jsonObj.put("kunnr", mUserID);
                jsonObj.put("action", textRemarkValue);
                jsonObj.put("mobile",mobile);

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("action", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");

                        progressDialog.dismiss();
                        //  finish();
                    }
                    else
                    {

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        activity.CustomUtility.setSharedPreference(getApplicationContext(), Constant.LocalConveyance, "0");
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //  finish();
                    }
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
            dialog.dismiss();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

/*    public void callgetCompalinAllListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("cmpno", mComplainNO));//kunur it means user id
        param.add(new BasicNameValuePair("STATUS", mStatusValue));///Pending Complaint
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));

        //  jsonObject.addProperty("lrno", mLRNumberValue);
        // jsonObject.addProperty("mobno", mLRMobileValue);

        //  param.add(new BasicNameValuePair("pernr", username));
        // param.add(new BasicNameValuePair("pass", password));
        *//******************************************************************************************//*
*//*                   server connection
/******************************************************************************************//*
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);
*//******************************************************************************************//*
*//*                       get JSONwebservice Data
/******************************************************************************************//*
                    //      JSONObject jo = new JSONObject(obj);
                    //  JSONArray ja = new JSONArray(obj);
                    // JSONObject jo = ja.getJSONObject(0);

                   *//* try {
                        Gson gson = new Gson();
                        //////////////add model class here
                        progressDialog.dismiss();
                        InstallarCustListModel mInstallarCustListModel = gson.fromJson(obj, InstallarCustListModel.class);
                        getInstallarCustListModel(mInstallarCustListModel);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*//*

                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        if(mComplainDetailListResponse.size()>0)
                            mComplainDetailListResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        // JSONObject jo = ja.getJSONObject(0);

                        System.out.println("ja==>>"+ja.get(0));

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            ComplainDetailListResponse mmComplainDetailListResponse = new ComplainDetailListResponse();

                            mmComplainDetailListResponse.setCmpno(join.getString("cmpno"));
                            mmComplainDetailListResponse.setPosnr(join.getString("posnr"));
                            mmComplainDetailListResponse.setMatnr(join.getString("matnr"));
                            mmComplainDetailListResponse.setMaktx(join.getString("maktx"));
                            mmComplainDetailListResponse.setReason(join.getString("reason"));
                            mmComplainDetailListResponse.setWarrantee(join.getString("warrantee"));
                            mmComplainDetailListResponse.setSernr(join.getString("sernr"));
                            mmComplainDetailListResponse.setCloserReason(join.getString("closer_reason"));
                            mmComplainDetailListResponse.setDefect(join.getString("defect"));
                            mmComplainDetailListResponse.setExtwg(join.getString("extwg"));
                            mmComplainDetailListResponse.setPaymentBy(join.getString("payment_by"));
                            mmComplainDetailListResponse.setCusamt(join.getString("cusamt"));
                            mmComplainDetailListResponse.setComamt(join.getString("comamt"));
                            mmComplainDetailListResponse.setDelamt(join.getString("delamt"));
                            mmComplainDetailListResponse.setReComp(join.getString("re_comp"));
                            mmComplainDetailListResponse.setReDel(join.getString("re_del"));
                            mmComplainDetailListResponse.setHistory(join.getString("history"));
                            mmComplainDetailListResponse.setPayToFreelancer(join.getString("pay_to_freelancer"));
                            mmComplainDetailListResponse.setVbeln(join.getString("vbeln"));
                            mmComplainDetailListResponse.setFkdat(join.getString("fkdat"));
                            mmComplainDetailListResponse.setWarrantyCondition(join.getString("warranty_condition"));
                            mmComplainDetailListResponse.setWarDate(join.getString("war_date"));
                            mmComplainDetailListResponse.setCmplnRelatedTo(join.getString("cmpln_related_to"));
                            mmComplainDetailListResponse.setInsuranceTxt(join.getString("insurance_txt"));
                            mmComplainDetailListResponse.setFocamt(join.getString("focamt"));

                            mComplainDetailListResponse.add(mmComplainDetailListResponse);

                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                                mPendingComplainDetailsListAdapter = new PendingComplainDetailsListAdapter(mContext, mComplainDetailListResponse, mMobileNumber);
                                rclyPendingComplainList.setAdapter(mPendingComplainDetailsListAdapter);
                                progressDialog.dismiss();
                            }


                        });

*//*
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


                        });*//*


                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                               *//* mPendingComplainListAdapter = new PendingComplainListAdapter(mContext, mComplainAllResponse);
                                rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);*//*
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

    }*/

}