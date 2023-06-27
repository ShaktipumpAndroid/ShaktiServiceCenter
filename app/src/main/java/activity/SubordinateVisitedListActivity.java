package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import activity.AdaperVk.SubordinateVisitedListAdapter;
import activity.BeanVk.ComplainAllResponse;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class SubordinateVisitedListActivity extends BaseActivity {

    private Context mContext;
    private ProgressDialog progressDialog;
    private List<ComplainAllResponse> mComplainAllResponse;
    RecyclerView rclyPendingComplainList;
    private ImageView imgBackID;
    private TextView txtHeaderID;
    private  String mHeaderTittle = "";
    private  String mStatusValue = "";
    private String mUserID;
    LinearLayout no_data;
    private Intent mmIntent;
    private SubordinateVisitedListAdapter mPendingComplainListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_visited_list);

        mContext = this;
        mmIntent = getIntent();
        initView();
    }

    private void initView() {

        mUserID = CustomUtility.getSharedPreferences(mContext,"userID");
        no_data = findViewById(R.id.no_data);
        mHeaderTittle = mmIntent.getStringExtra("heading");
        mStatusValue = mmIntent.getStringExtra("complaint");

        mComplainAllResponse = new ArrayList<>();
        imgBackID = findViewById(R.id.imgBackID);
        txtHeaderID = findViewById(R.id.txtHeaderID);
        rclyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
        rclyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));

        txtHeaderID.setText(mHeaderTittle);

        initClickEvent();
        callVisitedListAPI();

        Log.e("Values===>",mStatusValue+"  ,  "+ mUserID);
    }


    private void initClickEvent() {

        imgBackID.setOnClickListener(view -> finish());

    }

    private void callVisitedListAPI() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        param.add(new BasicNameValuePair("kunnr", mUserID));//kunur it means user id
        param.add(new BasicNameValuePair("cmpno", mStatusValue));///Pending Complaint

        /******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {
                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.VISITED_COMPLAIN_VK_PAGE, param);

/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/

                    JSONObject jo = new JSONObject(obj);

                    String mStatus = jo.getString("status");
                    final String mMessage = jo.getString("message");
                    String jo11 = jo.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {

                        if(mComplainAllResponse.size()>0)
                            mComplainAllResponse.clear();

                        JSONArray ja = new JSONArray(jo11);
                        System.out.println("ja==>>"+ja.get(0));

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject join = ja.getJSONObject(i);
                            ComplainAllResponse mmComplainAllResponse = new ComplainAllResponse();

                            mmComplainAllResponse.setCmpno(join.getString("cmpno"));
                            mmComplainAllResponse.setCmpdt(join.getString("cmpdt"));
                            mmComplainAllResponse.setCaddress(join.getString("caddress"));
                            mmComplainAllResponse.setMblno(join.getString("mblno"));
                            mmComplainAllResponse.setMblno1(join.getString("mblno1"));
                            mmComplainAllResponse.setCstname(join.getString("cstname"));
                            mmComplainAllResponse.setKunnr(join.getString("kunnr"));
                            mmComplainAllResponse.setName1(join.getString("name1"));
                            mmComplainAllResponse.setPernr(join.getString("pernr"));
                            mmComplainAllResponse.setEname(join.getString("ename"));
                            mmComplainAllResponse.setLand(join.getString("land"));
                            mmComplainAllResponse.setCity(join.getString("city"));
                            mmComplainAllResponse.setRegio(join.getString("regio"));
                            mmComplainAllResponse.setTehsil(join.getString("tehsil"));
                            mmComplainAllResponse.setCatgry(join.getString("catgry"));
                            mmComplainAllResponse.setEdit(join.getString("edit"));
                            mmComplainAllResponse.setCatgry1(join.getString("catgry1"));
                            mmComplainAllResponse.setDeln0(join.getString("deln0"));
                            mmComplainAllResponse.setDelname(join.getString("delname"));
                            mmComplainAllResponse.setEpc(join.getString("epc"));
                            mmComplainAllResponse.setLifnr(join.getString("lifnr"));
                            mmComplainAllResponse.setCmpPenRe(join.getString("cmp_pen_re"));
                            mmComplainAllResponse.setFdate(join.getString("fdate"));
                            mmComplainAllResponse.setAwaitAprPernr(join.getString("await_apr_pernr"));
                            mmComplainAllResponse.setAwaitAprPernrNm(join.getString("await_apr_pernr_nm"));
                            mmComplainAllResponse.setPendAprPernr(join.getString("pend_apr_pernr"));
                            mmComplainAllResponse.setPendAprPernrNm(join.getString("pend_apr_pernr_nm"));
                            mmComplainAllResponse.setAwaitApproval(join.getString("await_approval"));
                            mmComplainAllResponse.setPendApproval(join.getString("pend_approval"));
                            mmComplainAllResponse.setAwaitAprRemark(join.getString("await_apr_remark"));
                            mmComplainAllResponse.setPendAprRemark(join.getString("pend_apr_remark"));
                            mmComplainAllResponse.setScStatus(join.getString("sc_status"));
                            mmComplainAllResponse.setSengno(join.getString("sengno"));
                            mmComplainAllResponse.setVistedstatus("");

                            mComplainAllResponse.add(mmComplainAllResponse);

                        }

                        runOnUiThread(() -> {

                            mPendingComplainListAdapter = new SubordinateVisitedListAdapter(mContext, mComplainAllResponse, mStatusValue);
                            rclyPendingComplainList.setAdapter(mPendingComplainListAdapter);
                            progressDialog.dismiss();
                        });

                        progressDialog.dismiss();

                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(mContext, mMessage, Toast.LENGTH_SHORT).show();
                            no_data.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        });
                        progressDialog.dismiss();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                }

            }

        }.start();

    }

}