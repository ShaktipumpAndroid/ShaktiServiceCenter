package activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import activity.retrofit.APIInterface;
import activity.retrofit.ApiClient;
import activity.retrofit.Model.PendingSiteComplain.PendingSiteComplainResponse;
import adapter.VisitedSiteAdapter;
import bean.VisitedSiteBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedSiteActivity extends AppCompatActivity {

    private EditText start_date, end_date;
    private TextView save;
    Context context;
    private String  mStart, mEnd;
    APIInterface apiInterface;

    ListView visited_list;
    VisitedSiteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_site_acitivity);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.visitedSite));
        context = this;
        start_date = findViewById(R.id.start_date);
        end_date = findViewById(R.id.end_date);
        save = findViewById(R.id.save);

        visited_list = findViewById(R.id.visited_list);

        start_date.setFocusable(false);
        end_date.setFocusable(false);

        start_date.setOnClickListener(view -> {
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

        // Date help for leave to
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

        save.setOnClickListener(v -> getVisitedData());
    }

    private void getVisitedData()  {

        String kunnr = CustomUtility.getSharedPreferences(context, "username");
        Log.e("Dates:====>", mEnd + " , " + mStart + " , " + kunnr);
        PendingSiteComplainResponse pendingSiteComplainResponse = new PendingSiteComplainResponse();
        ArrayList<VisitedSiteBean> visitedSiteBeanArrayList = new ArrayList<>();
        ProgressDialog progress;


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait!!");
        progress.setMessage("Wait!!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        Call<PendingSiteComplainResponse> call = apiInterface.getVisitedComplain(kunnr, mStart, mEnd);

        Log.e("URL====>", call.request().url().toString());
        call.enqueue(new Callback<PendingSiteComplainResponse>() {
            @Override
            public void onResponse(@NotNull Call<PendingSiteComplainResponse> call, @NotNull Response<PendingSiteComplainResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;

                    pendingSiteComplainResponse.response = response.body().response;

                    for (int i = 0 ; i < pendingSiteComplainResponse.response.size(); i++ ){

                        VisitedSiteBean visitedSiteBean = new VisitedSiteBean();

                        visitedSiteBean.setCmpDate(pendingSiteComplainResponse.response.get(i).cmpdt);
                        visitedSiteBean.setCmpNo(pendingSiteComplainResponse.response.get(i).cmpno);
                        visitedSiteBean.setSubordinate(pendingSiteComplainResponse.response.get(i).sub_ordinat);
                        visitedSiteBean.setSubordinateName(pendingSiteComplainResponse.response.get(i).sub_name);

                        visitedSiteBeanArrayList.add(visitedSiteBean);
                    }

                    adapter = new VisitedSiteAdapter(context, visitedSiteBeanArrayList);
                    visited_list.setAdapter(adapter);

                    progress.dismiss();
                    Toast.makeText(VisitedSiteActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
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