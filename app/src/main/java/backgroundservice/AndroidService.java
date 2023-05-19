package backgroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import com.shaktipumps.shakti.shaktiServiceCenter.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import activity.CustomUtility;
import activity.GPSTracker;
import bean.EmployeeGPSActivityBean;
import database.DatabaseHelper;
import syncdata.SyncDataToSAP_New;

/**
 * Created by shakti on 12/3/2016.
 * capture lat long on every 15 min
 */


public class AndroidService extends Service {
    // constant


    //public static final long NOTIFY_INTERVAL = 60 * 10 * 1000;    // 1000 = 1 second   15 min
    public static final long NOTIFY_INTERVAL = 60 * 1000;    // 1000 = 1 second   15 min

    ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = null;
    DatabaseHelper db = null;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new android.app.Notification());
        }
        // cancel if already existed
        if (mTimer != null) {
            // Toast.makeText(this, "already running", Toast.LENGTH_LONG).show();

            mTimer.cancel();
        } else {
            // recreate new
            //  Toast.makeText(this, "create new command", Toast.LENGTH_LONG).show();
            mTimer = new Timer();

            employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
            db = new DatabaseHelper(getApplicationContext());


        }
        // schedule task


        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        //  isRunning = false;
        mTimer.cancel();
        // Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle("Shakti Sales Employee")
                        .setContentText("Please Turn on GPS ! ");
//

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent notificationIntent = new Intent(this, Notification.class);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

//        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.shaktipumps.shakti.shaktisalesemployee";
        String channelName = "Shakti Sales Employee";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        android.app.Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(android.app.Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    getLatLong();

//                    Toast.makeText(getApplicationContext(), getLatLong(),
//                            Toast.LENGTH_SHORT).show();

                   // new Worker().execute();
                }

            });
        }

        private String getLatLong() {
            GPSTracker gps;
            gps = new GPSTracker(getApplicationContext());
            double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
            double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));





            String latlong;
            latlong = latitude + "," + longitude;
            Log.d("background", "" + latlong);

            if (latitude == 0.0) {
                addNotification();
            }

            return latlong;
        }

    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                // sync employee gps data to sap every 15 min
                if (CustomUtility.isOnline(getApplicationContext())) {

                    new SyncDataToSAP_New().SendDataToSap(getApplicationContext());
                }


            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
        }


    }

}
