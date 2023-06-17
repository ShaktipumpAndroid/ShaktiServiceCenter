package activity.complainvk;

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
import android.widget.EditText;
import android.widget.ImageView;
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

import activity.AdaperVk.PendingComplainDetailsListAdapter;
import activity.BeanVk.ComplainDetailListResponse;
import activity.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class ApproveComplainDetailActivity extends AppCompatActivity {

    private Context mContext;
    private ProgressDialog progressDialog;

    private List<ComplainDetailListResponse> mComplainDetailListResponse;

    private ImageView imgBackID;
    private TextView txtHeaderID;
private String textRemarkValue;
    private  String mHeaderTittle= "";
    private  String mComplainNO= "";
    private  String mStatusValue= "";
    private  String mMobileNumber= "";
    private  String mUserID= "";
    String mobile;
    //private BaseRequest baseRequest;
    public TextView txtBTNActionID, txtBTNPendingID, txtBTNClodeID, txtBTNUploadID;
    private RecyclerView rclyPendingComplainList;
    private PendingComplainDetailsListAdapter mPendingComplainDetailsListAdapter;

    private Intent mmIntent;
    private Dialog dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_complain_detail);
        mContext = this;
        mmIntent = getIntent();
        initView();
    }

    private void initView() {

        mUserID = CustomUtility.getSharedPreferences(mContext,"kunnr");

        mComplainDetailListResponse = new ArrayList<>();
        mobile = CustomUtility.getSharedPreferences(mContext,"username");
        rclyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
        rclyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));

        imgBackID = findViewById(R.id.imgBackID);
        txtHeaderID = findViewById(R.id.txtHeaderID);

        txtBTNUploadID =  findViewById(R.id.txtBTNUploadID);

        txtBTNActionID = findViewById(R.id.txtBTNActionID);

        mMobileNumber = mmIntent.getStringExtra("mobile_number");
        mComplainNO = mmIntent.getStringExtra("Complain_number");
        mHeaderTittle = mmIntent.getStringExtra("complaint");
        mStatusValue = mmIntent.getStringExtra("StatusValue");

        txtHeaderID.setText(mHeaderTittle);
        WebURL.EDITTEXT_SHOW_SIZE =0;
        initClickEvent();
        callgetCompalinAllListAPI();

       /* if(WebURL.EDITTEXTBOX_SHOW_SIZE == 0)
        {
            txtBTNUploadID.setVisibility(View.GONE);
        }
        else
        {
            txtBTNUploadID.setVisibility(View.VISIBLE);
        }*/

    }

    private void initClickEvent() {

        imgBackID.setOnClickListener(view -> finish());


       txtBTNActionID.setOnClickListener(view -> {

          // Toast.makeText(mContext, "Action optiin working commimg soon", Toast.LENGTH_SHORT).show();
         initRemarkViewBox(view);
       });






       txtBTNUploadID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itm1="", itm2="",itm3="";
                int iiii = mComplainDetailListResponse.size();
                if(iiii == 1)
                {
                    itm1 = mComplainDetailListResponse.get(0).getPosnr();
                    itm2 ="0";
                            itm3 ="0";
                   /* Intent intent = new Intent(mContext, ApproveReportImageActivity.class);
                    intent.putExtra("inst_id", mComplainDetailListResponse.get(2).getCmpno());
                    intent.putExtra("cust_name", mComplainDetailListResponse.get(2).getCloserReason());
                    intent.putExtra("StatusCheck", mStatusValue);
                    intent.putExtra("ListSize", mComplainDetailListResponse.size());
                    intent.putExtra("item1", mComplainDetailListResponse.get(0).getPosnr());
                  //  intent.putExtra("item2", mComplainDetailListResponse.get(1).getPosnr());
                   // intent.putExtra("item3", mComplainDetailListResponse.get(2).getPosnr());

                    mContext.startActivity(intent);*/

                }
                else  if(iiii == 2)
                {
                    itm2 = mComplainDetailListResponse.get(1).getPosnr();
                    itm1 =mComplainDetailListResponse.get(0).getPosnr();
                    itm3 ="0";

                    /*
                    Intent intent = new Intent(mContext, ApproveReportImageActivity.class);
                    intent.putExtra("inst_id", mComplainDetailListResponse.get(2).getCmpno());
                    intent.putExtra("cust_name", mComplainDetailListResponse.get(2).getCloserReason());
                    intent.putExtra("StatusCheck", mStatusValue);
                    intent.putExtra("ListSize", mComplainDetailListResponse.size());
                    intent.putExtra("item1", mComplainDetailListResponse.get(0).getPosnr());
                    intent.putExtra("item2", mComplainDetailListResponse.get(1).getPosnr());
                   // intent.putExtra("item3", mComplainDetailListResponse.get(2).getPosnr());

                    mContext.startActivity(intent);*/
                }
                else  if(iiii == 3)
                {
                    itm2 = mComplainDetailListResponse.get(1).getPosnr();
                    itm1 =mComplainDetailListResponse.get(0).getPosnr();
                    itm3 =mComplainDetailListResponse.get(3).getPosnr();

                    /*Intent intent = new Intent(mContext, ApproveReportImageActivity.class);
                    intent.putExtra("inst_id", mComplainDetailListResponse.get(2).getCmpno());
                    intent.putExtra("cust_name", mComplainDetailListResponse.get(2).getCloserReason());
                    intent.putExtra("StatusCheck", mStatusValue);
                    intent.putExtra("ListSize", mComplainDetailListResponse.size());
                    intent.putExtra("item1", mComplainDetailListResponse.get(0).getPosnr());
                    intent.putExtra("item2", mComplainDetailListResponse.get(1).getPosnr());
                    intent.putExtra("item3", mComplainDetailListResponse.get(2).getPosnr());


                    mContext.startActivity(intent);
*/
                }


                Intent intent = new Intent(mContext, ApproveReportImageActivity.class);
                intent.putExtra("inst_id", mComplainDetailListResponse.get(0).getCmpno());
                intent.putExtra("cust_name", mComplainDetailListResponse.get(0).getCloserReason());
                intent.putExtra("StatusCheck", mStatusValue);
                intent.putExtra("ListSize", mComplainDetailListResponse.size());
                intent.putExtra("item1", itm1);
                intent.putExtra("item2", itm2);
                intent.putExtra("item3", itm3);


                mContext.startActivity(intent);



               /* Intent intent = new Intent(mContext, ApproveReportImageActivity.class);
                intent.putExtra("inst_id", mComplainDetailListResponse.get(0).getCmpno());
                intent.putExtra("cust_name", mComplainDetailListResponse.get(0).getCloserReason());
                intent.putExtra("StatusCheck", mStatusValue);
                intent.putExtra("ListSize", mComplainDetailListResponse.size());
                intent.putExtra("item1", mComplainDetailListResponse.size());
                intent.putExtra("item1", mComplainDetailListResponse.size());
                intent.putExtra("item1", mComplainDetailListResponse.size());


                mContext.startActivity(intent);*/

            }
        });

    }

    private void initRemarkViewBox(View view) {

        EditText editText;
        TextView txtBTNRemarkID;
          dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_remark_as_complain);

        editText = dialog.findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = dialog.findViewById(R.id.txtBTNRemarkID);
        dialog.setCancelable(true);

        dialog.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(PendingComplainDetailActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_remark_as_complain, viewGroup, false);
        editText = findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = findViewById(R.id.txtBTNRemarkID);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();*/


        txtBTNRemarkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textRemarkValue = editText.getText().toString().trim();
                if(!textRemarkValue.equalsIgnoreCase(""))
                {
                   // callInsertRemarkAPI(textRemarkValue);
                    new SyncInstallationData1().execute();
                }
                else
                {
                    Toast.makeText(mContext, "Please enter remark!", Toast.LENGTH_SHORT).show();
                }
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


    private class SyncInstallationData1 extends AsyncTask<String, String, String> {

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
                 jsonObj.put("mobile",mobile);
                //  jsonObj.put("mobno",mLrInvoiceResponse.get(0).getMobno());
                jsonObj.put("kunnr", mUserID);
                jsonObj.put("action", textRemarkValue);

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
                        progressDialog.dismiss();
                      //  finish();
                    }
                    else
                    {

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
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



    public void callInsertRemarkAPI(String textRemarkValue) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        //   username = inputName.getText().toString();
        //   password = inputPassword.getText().toString();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("cmpno", mComplainNO));//kunur it means user id
      //  param.add(new BasicNameValuePair("STATUS", mStatusValue));///Pending Complaint
        param.add(new BasicNameValuePair("kunnr", mUserID));///Pending Complaint
        param.add(new BasicNameValuePair("action", textRemarkValue));///Pending Complaint
        // param.add(new BasicNameValuePair("mobno", mLRMobileValue));


        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);


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

                            mComplainDetailListResponse.add(mmComplainDetailListResponse);

                        }

                        //   Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();

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


    public void callgetCompalinAllListAPI() {

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
        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE, param);
                    Log.d("check_error", obj);
                    Log.e("check_error", obj);
/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                    //      JSONObject jo = new JSONObject(obj);
                    //  JSONArray ja = new JSONArray(obj);
                    // JSONObject jo = ja.getJSONObject(0);

                   /* try {
                        Gson gson = new Gson();
                        //////////////add model class here
                        progressDialog.dismiss();
                        InstallarCustListModel mInstallarCustListModel = gson.fromJson(obj, InstallarCustListModel.class);
                        getInstallarCustListModel(mInstallarCustListModel);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

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

                            WebURL.EDITTEXT_SHOW_SIZE = i+1;

                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Toast.makeText(mContext, mComplainDetailListResponse.size(), Toast.LENGTH_SHORT).show();

                             //   System.out.println("mComplainDetailListResponse.size()==>"+mComplainDetailListResponse.size());
                                mPendingComplainDetailsListAdapter = new PendingComplainDetailsListAdapter(mContext, mComplainDetailListResponse, mMobileNumber,"");
                                rclyPendingComplainList.setAdapter(mPendingComplainDetailsListAdapter);
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
                                Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
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

}