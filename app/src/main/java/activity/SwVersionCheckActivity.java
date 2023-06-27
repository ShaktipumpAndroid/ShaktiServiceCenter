package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktiServiceCenter.R;

import activity.utility.Constant;

public class SwVersionCheckActivity extends BaseActivity {
    TextView okbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sw_version_check);

        okbtn = findViewById(R.id.okbtn);

        okbtn.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.APPURL))));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CustomUtility.getSharedPreferences(getApplicationContext(), Constant.APPURL))));
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}