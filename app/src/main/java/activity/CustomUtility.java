package activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by shakti on 11/21/2016.
 */
@SuppressWarnings("deprecation")
public class CustomUtility {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static boolean connected;

    public static SimpleDateFormat simpleDateFormat = null;

    public static Context context;
    private static final String PREFERENCE = "DealLizard";

    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static boolean isDateTimeAutoUpdate(Context mContext) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            } else {
                if (android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            }


        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("Date & Time Settings");
        // Setting Dialog Message
        alertDialog.setMessage("Please enable automatic date and time setting");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
            mContext.startActivity(intent);
        });
        // on pressing cancel button

        // Showing Alert Message
        alertDialog.show();

    }


    public static boolean isOnline(Context mContext) {

        try {

            ConnectivityManager

                    connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            Log.v("network", String.valueOf(connected));

//
//                Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                int returnVal = p1.waitFor();
//
//                Log.v("ping",   String.valueOf(  returnVal ) );
//
//                connected = (returnVal == 0);
//
//
//
//                return connected;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        } catch (Exception e) {
//            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;

    }

    public static boolean CheckGPS(Context mContext) {

        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            double latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
            double longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

            if (latitude == 0.0 && longitude == 0.0) {

                CustomUtility.ShowToast("GPS Co-ordinates not received yet. Please Wait for some time", mContext);
                return false;
            }
        } else {
            gps.showSettingsAlert();
            return false;
        }


        return true;
    }
//    TelephonyManager telephonyManager = (TelephonyManager) activity
//            .getSystemService(Context.TELEPHONY_SERVICE);
//    return telephonyManager.getDeviceId();
//}


    public static boolean checkPermission(final Context context) {


        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String getCurrentDate() {
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String current_date = simpleDateFormat.format(new Date());
        return current_date.trim();
    }

    // for username string preferences
    public static void setSharedPreference(Context mContext, String name,
                                           String value) {
        context = mContext;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }


    public static String formatDate(String date) {
        String formattedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy",Locale.US);
            Date mDate = format.parse(date);
            SimpleDateFormat appFormat = new SimpleDateFormat("yyyyMMdd",Locale.US);
            assert mDate != null;
            formattedDate = appFormat.format(mDate);
            Log.i("Result", "mDate " + formattedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String formatTime(String time) {
        String formattedDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
            Date mDate = format.parse(time);
            SimpleDateFormat appFormat = new SimpleDateFormat("HHmmss",Locale.getDefault());
            assert mDate != null;
            formattedDate = appFormat.format(mDate);
            Log.i("Result", "mDate " + formattedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    public static String getCurrentTime() {

        String current_time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

       Log.e("TIME123","&&&&"+ current_time.trim());
        return current_time.trim();

    }
    
    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    public static String formateDate1(String parseDare) {
        String inputPattern = "dd.MM.yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(parseDare);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String formatTime1(String parseDare) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date ;
        String str = null;

        try {
            date = inputFormat.parse(parseDare);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void removeFromSharedPreference(Context context,String name){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        settings.edit().remove(name).apply();
    }

    public static boolean isInternetOn(Context mContext) {

        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;

    }

    public static void clearSharedPreference(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        settings.edit().clear().apply();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        return capitalize(manufacturer) + "--" + model;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + "--" + model;
//        }

    }


    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
