package activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import activity.retrofit.APIInterface;
import activity.retrofit.ApiClient;
import activity.retrofit.Model.PendingSiteComplain.PendingSiteComplainResponse;
import adapter.PendingSiteAdapter;
import bean.PendingSiteBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingSiteActivity extends AppCompatActivity {

    APIInterface apiInterface;
    private EditText start_date, end_date;
    Context context;
    private String mStart, mEnd;
    TextView save;
    ListView pending_list;
    PendingSiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_site);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.pendingSite));

        context = this;
        /*start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        save = findViewById(R.id.save);*/
        pending_list = findViewById(R.id.pending_list);

  /*      start_date.setFocusable(false);
        end_date.setFocusable(false);*/

        getPendingData();

   /*     start_date.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                start_date.setText(i2 + "/" + i1 + "/" + i);
                mStart = start_date.getText().toString().trim();
                parseDateToddMMyyyy1(mStart);
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle(getResources().getString(R.string.start));
            datePickerDialog.show();
        });

        end_date.setOnClickListener(view -> {
            Calendar currentDate;
            int mDay, mMonth, mYear;
            currentDate = Calendar.getInstance();

            mDay = currentDate.get(Calendar.DAY_OF_MONTH);
            mMonth = currentDate.get(Calendar.MONTH);
            mYear = currentDate.get(Calendar.YEAR);
            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
                i1 = i1 + 1;
                end_date.setText(i2 + "/" + i1 + "/" + i);
                mEnd = end_date.getText().toString().trim();
                parseDateToddMMyyyy2(mEnd);
            }, mYear, mMonth, mDay);
            datePickerDialog.setTitle(getResources().getString(R.string.end));
            datePickerDialog.show();
        });

        save.setOnClickListener(v -> getPendingData());
*/

    }

    private void getPendingData() {

        String kunnr = CustomUtility.getSharedPreferences(context, "username");
        Log.e("Dates:====>", mEnd + " , " + mStart + " , " + kunnr);
        PendingSiteComplainResponse pendingSiteComplainResponse = new PendingSiteComplainResponse();
        ArrayList<PendingSiteBean> pendingSiteBeanArrayList = new ArrayList<>();
        ProgressDialog progress;


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<PendingSiteComplainResponse> call = apiInterface.getPendingVisiting(kunnr, "", "");

        Log.e("URL====>", call.request().url().toString());
        call.enqueue(new Callback<PendingSiteComplainResponse>() {
            @Override
            public void onResponse(@NotNull Call<PendingSiteComplainResponse> call, @NotNull Response<PendingSiteComplainResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    pendingSiteComplainResponse.response = response.body().response;

                    for (int i = 0 ; i < pendingSiteComplainResponse.response.size(); i++ ){

                        PendingSiteBean pendingSiteBean = new PendingSiteBean();

                        pendingSiteBean.setCmpDate(pendingSiteComplainResponse.response.get(i).cmpdt);
                        pendingSiteBean.setCmpNo(pendingSiteComplainResponse.response.get(i).cmpno);
                        pendingSiteBean.setSubordinate(pendingSiteComplainResponse.response.get(i).sub_ordinat);
                        pendingSiteBean.setSubordinateName(pendingSiteComplainResponse.response.get(i).sub_name);

                        pendingSiteBeanArrayList.add(pendingSiteBean);
                    }

                    adapter = new PendingSiteAdapter(context, pendingSiteBeanArrayList);
                    pending_list.setAdapter(adapter);

                    progress.dismiss();
                    Toast.makeText(PendingSiteActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<PendingSiteComplainResponse> call, @NotNull Throwable t) {
                progress.dismiss();
                call.cancel();
            }

        });
    }


    public void parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        try {
            Date date = inputFormat.parse(time);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}