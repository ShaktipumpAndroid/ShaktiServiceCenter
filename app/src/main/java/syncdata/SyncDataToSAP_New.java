package syncdata;

import android.content.Context;
import android.content.SharedPreferences;


import org.apache.http.NameValuePair;
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import database.DatabaseHelper;
import webservice.SAPWebService;

/**
 * Created by shakti on 11/21/2016.
 */
public class  SyncDataToSAP_New {

    final ArrayList<NameValuePair> param2 = new ArrayList<NameValuePair>();
    final int MODE_PRIVATE = 0;
    DatabaseHelper dh;
    String sync_data_name = null;
    String sync_data_value = null;
    String sync_key_id = null;
    Context mContext = null;
    JSONArray ja_noOrder = null;
    JSONArray ja_takeOrder = null;
    JSONArray ja_empGPS = null;
    JSONArray ja_callLog = null;
    JSONArray ja_dsr_entry = null;
    JSONArray ja_check_in = null;
    JSONArray ja_new_customer = null;
    JSONArray ja_markAttendance = null;
    JSONArray ja_survey = null;
    JSONArray ja_inprocessComplaints = null;
    JSONArray ja_clouserComplaints = null;
    JSONArray ja_frwd_app_cmp = null;
    JSONArray ja_pend_app_cmp = null;
    JSONArray ja_complaintImage = null;
    JSONArray ja_complaintPdf = null;
    JSONArray ja_complaintAudio = null;
    JSONArray ja_complaintStart = null;
    public static final String GALLERY_DIRECTORY_NAME = "Sales Photo";
    SharedPreferences pref;
    SAPWebService con = null;
    SharedPreferences.Editor editor;

   /* android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };*/

    public void SendDataToSap(Context context) {
        this.mContext = context;











/*********************************************************************************************/
/*                      end  send data to server
/*********************************************************************************************/


/**** chat app data send *********************/


//************************  send data to chat app ***************************************

//        param2.add(new BasicNameValuePair("username",pref.getString("chat_app_username","userid") ));
//        param2.add(new BasicNameValuePair("password",pref.getString("chat_app_password","password")));
//
//       param2.add(new BasicNameValuePair("group_id",pref.getString("chat_app_group_id","group_id")));
//
//       // param2.add(new BasicNameValuePair("group_id","99999001"));
//
//        param2.add(new BasicNameValuePair("from_id",pref.getString("key_username","userid") ));
//        param2.add(new BasicNameValuePair("from_type","E" ));


        //getCheckInOutAudio();

//          getComplaintAudio();


        //getChatAppGroup();


    }



    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }



}


