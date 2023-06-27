package activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import activity.utility.Constant;
import database.DatabaseHelper;
import service.RetrieveFirestoreData;

public abstract class BaseActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(SwVersionConfigBroadcastReceiver, new IntentFilter(Constant.SwVersionConfig));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirestoreData();
    }

    private void getFirestoreData() {
        databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.getLogin() &&  CustomUtility.getSharedPreferences(getApplicationContext(), "CHECK_OTP_VERIFED").equals("Y"))  {

            if (!RetrieveFirestoreData.isServiceRunning) {
                Intent intent = new Intent(getApplicationContext(), RetrieveFirestoreData.class);
                startService(intent);
            }
        }

    }

    BroadcastReceiver SwVersionConfigBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
                alertDialog = null;
            }
            Log.e("broadcastReceive====>","true");
           Intent intent1 = new Intent(getApplicationContext(),SwVersionCheckActivity.class);
            startActivity(intent1);
            finish();

        }
    };
}
