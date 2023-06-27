package activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import activity.retrofit.APIInterface;
import activity.retrofit.ApiClient;
import activity.retrofit.Model.Assgin.AssignResponse;
import activity.retrofit.otp.BaseRequest;
import activity.retrofit.otp.RequestReciever;
import adapter.SubordinateForwardAdapter;
import bean.SubordinateBean;
import database.DatabaseHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForwardList extends BaseActivity {

    Context context;
    APIInterface apiInterface;
    ArrayList<SubordinateBean> subordinateBeanArrayList ;
    List<Boolean> subordinateCheckList;
    RecyclerView relySubordinateList;
    SubordinateForwardAdapter adapter;
    TextView txtSubmitPhotoID;
    DatabaseHelper db;
    Toolbar toolbar;
    String complainNo,userid,status;
    private BaseRequest baseRequest;
    String compNo = null, mobileNo = null , name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forword_list);

        Bundle bundle =getIntent().getExtras();
        baseRequest = new BaseRequest(this);
        context = this;
        complainNo = bundle.getString("comNo");
        status = bundle.getString("status");
        userid = CustomUtility.getSharedPreferences(context,"username");
        Log.e("complain===>",""+complainNo);

        initView();
        sendDataSap();
    }

    private void sendDataSap() {

        txtSubmitPhotoID.setOnClickListener(v -> {
                boolean isSelected = false;


                for (int i = 0; i < subordinateBeanArrayList.size(); i++) {

                    if(subordinateCheckList.get(i)){
                        compNo =  complainNo;
                        mobileNo = subordinateBeanArrayList.get(i).getMobileNo();
                        name =  subordinateBeanArrayList.get(i).getName();
                        isSelected = true;
                    }
                }


                if(isSelected){

                    //api call
                    sendDatatoSap();
                    callInsertAndUpdateDebugDataAPI();
                }
                else {
                    Toast.makeText(context, getResources().getString(R.string.select_subordinate), Toast.LENGTH_SHORT).show();
                }
        });
    }

    private void callInsertAndUpdateDebugDataAPI()  {
        baseRequest.setBaseRequestListner(new RequestReciever() {
            @Override
            public void onSuccess(int APINumber, String Json, Object obj) {
                try {
                    if (!obj.toString().isEmpty()) {
                        Toast.makeText(context, "OTP send successfully.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "OTP send failed please try again.", Toast.LENGTH_LONG).show();
                    }
                    baseRequest.hideLoader();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int APINumber, String errorCode, String message) {
                baseRequest.hideLoader();
                //  Toast.makeText(mContext, "OTP send failed please try again On Failure.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkFailure(int APINumber, String message) {
                baseRequest.hideLoader();
                Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_LONG).show();
            }
        });

        Map<String, String> wordsByKey = new HashMap<>();
        System.out.println("jsonObject==>>" + wordsByKey);
        //baseRequest.callAPIGET(1, wordsByKey, NewSolarVFD.GET_DEVICE_SIM_NUMBER_API);/////
        // baseRequest.callAPIGETDirectURL(1, "http://login.yourbulksms.com/api/sendhttp.php?authkey=8716AQbKpjEHR5b4479de&mobiles=" + edtINSTNumberIDSTR + "&message=Enter The Following OTP To Verify Your Account " + mORG_OTP_VALUE + " SHAKTI&sender=SHAKTl&route=4&country=91&DLT_TE_ID=1707161675029844457");/////
       // baseRequest.callAPIGETDirectURL(1,   "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&mobiles="+etDeliveryBoyMobileNo.getText().toString()+"&message=Dear%20User%20New%20Delivery%20is%20Assigned%20to%20you%20Please%20Use%20the%20below%20link%20to%20install%20the%20App%20https%3A%2F%2Fplay.google.com%2Fstore%2Fsearch%3Fq%3Dshakti%2520Transport%2520App%26c%3Dapps%20SHAKTIPUMPS&sender=SHAKTl&route=2&unicode=0&country=91&DLT_TE_ID=1707168475371194441");

        baseRequest.callAPIGETDirectURL(1,   "http://control.yourbulksms.com/api/sendhttp.php?authkey=393770756d707334373701&mobiles="+mobileNo+"&message=Dear%20User%20"+name+"%20New%20Delivery%20is%20Assigned%20to%20you%20Please Use%20the%20below%20link%20to%20install%20the%20App%20https://bit.ly/3N12flf%20SHAKTIPUMPS&sender=SHAKTl&route=2&unicode=0&country=91&DLT_TE_ID=1707168533187810409");
    }
    private void sendDatatoSap() {

        JSONArray ja_invc_data = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("cmpno",compNo);
            jsonObj.put("mobile",mobileNo);
            jsonObj.put("kunnr",userid);
            jsonObj.put("sc_status",status);

            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }
        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<AssignResponse> call = apiInterface.assignComplain(ja_invc_data);

        call.enqueue(new Callback<AssignResponse>() {
            @Override
            public void onResponse(Call<AssignResponse> call, Response<AssignResponse> response) {
                Log.e("DATA====>","hii  "+response.body().toString());
                Log.e("DATA",response.body().message);
                Log.e("DATA",response.body().status);

                assert response.body() != null;
                if (response.body().status.equalsIgnoreCase("true"))
                {
                    Toast.makeText(ForwardList.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ForwardList.this,  response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssignResponse> call, Throwable t) {
                call.cancel();
            }

        });

    }

    private void initView() {
        context = this;

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.forward));
        subordinateCheckList = new ArrayList<>();
        txtSubmitPhotoID =  findViewById(R.id.txtSubmitePhotoID);
        db = new DatabaseHelper(this);

        relySubordinateList = findViewById(R.id.subordinateList);
        relySubordinateList.setLayoutManager(new LinearLayoutManager(this));


        subordinateBeanArrayList = db.getSubordinateList();

        for (int i = 0; i < subordinateBeanArrayList.size(); i++) {
            subordinateCheckList.add(false);
        }

        adapter = new SubordinateForwardAdapter(context, subordinateBeanArrayList, subordinateCheckList);

        relySubordinateList.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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


}