package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.List;

import adapter.VisitedAssignComplainListAdapter;
import bean.SubordinateVisitedComplainBean;
import database.DatabaseHelper;

public class VisitedComplainListActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView relyPendingComplainList;
    List<SubordinateVisitedComplainBean> subordinateBeanList ;
    VisitedAssignComplainListAdapter visitedComplainListAdapter;
    private Intent mmIntent;
    private ImageView imgBackID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_complain_list);

        Context mContext = this;
        mmIntent = getIntent();
        initView();
        db = new DatabaseHelper(mContext);
        // Database
       // subordinateBeanList = db.getSubordinateAssginComplainList();
        subordinateBeanList = db.getSubordinateVsitedComplainList();

        Log.e("SIZE====>", ""+subordinateBeanList.size());

        visitedComplainListAdapter = new VisitedAssignComplainListAdapter(mContext, subordinateBeanList);
        relyPendingComplainList.setAdapter(visitedComplainListAdapter);
    }

    private void initView() {

        String mHeaderTittle = mmIntent.getStringExtra("complaint");
        imgBackID = findViewById(R.id.imgBackID);
        TextView txtHeaderID = findViewById(R.id.txtHeaderID);
        relyPendingComplainList = findViewById(R.id.rclyPendingComplainList);
        relyPendingComplainList.setLayoutManager(new LinearLayoutManager(this));
        txtHeaderID.setText(mHeaderTittle);

        initClickEvent();
    }

    private void initClickEvent() {

        imgBackID.setOnClickListener(view -> finish());

    }


}