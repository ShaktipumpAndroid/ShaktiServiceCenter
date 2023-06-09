package activity.complainvk.Freelauncer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.util.List;

import adapter.PendingAssginComplainListAdapter;
import bean.SubordinateAssginComplainBean;
import database.DatabaseHelper;

public class PendingComplainListFreelancerActivity extends AppCompatActivity {

    DatabaseHelper db;
    RecyclerView relyPendingComplainList;
    List<SubordinateAssginComplainBean> subordinateBeanList ;
    PendingAssginComplainListAdapter assignComplainListAdapter;
    private Intent mmIntent;
    private ImageView imgBackID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complain_freelauncer_list);

        Context mContext = this;
        mmIntent = getIntent();
        initView();
        db = new DatabaseHelper(mContext);
        // Database
        subordinateBeanList = db.getSubordinateAssginComplainList();

        assignComplainListAdapter = new PendingAssginComplainListAdapter(mContext, subordinateBeanList);
        relyPendingComplainList.setAdapter(assignComplainListAdapter);

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