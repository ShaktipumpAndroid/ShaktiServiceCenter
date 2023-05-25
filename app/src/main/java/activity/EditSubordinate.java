package activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import bean.SubordinateBean;
import database.DatabaseHelper;

public class EditSubordinate extends AppCompatActivity {

    private SubordinateBean subordinateBean;

    Toolbar toolbar;
    Context context;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subordinate);

        Bundle bundle = getIntent().getExtras();
        subordinateBean = bundle.getParcelable("subordinateDetail");

        Log.e("SubList===>",subordinateBean.getName());
        Log.e("SubList===>",subordinateBean.getMobileNo());
    }
}