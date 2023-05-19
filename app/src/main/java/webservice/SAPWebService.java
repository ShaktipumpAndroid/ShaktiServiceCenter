package webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import activity.CustomUtility;
import bean.BeanProduct;
import bean.LoginBean;
import database.DatabaseHelper;

/**
 * Created by shakti on 10/19/2016.
 */

public class SAPWebService {

    public static final int MODE_PRIVATE = 0;
    ArrayList<String> al;
    String matnr, kunnr, vkorg, vtweg, extwg, maktx, kbetr, mtart, konwa;
    //get user id of person
    String userid = LoginBean.getUseid();
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    /**************************** create attendance table ***********************************************/
    // Create DatabaseHelper instance
    public int getAttendanceData(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.ATTENDANCE_PAGE, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                JSONArray ja = jsonObj.getJSONArray("attendance");


             //   dataHelper.deleteAttendance();

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);
                    // login = jo.getString("LOGIN");


                    dataHelper.insertAttendance(userid,
                            jo.getString("begdat"),
                            jo.getString("indz"),
                            jo.getString("iodz"),
                            jo.getString("totdz"),
                            jo.getString("atn_status"),
                            jo.getString("leave_typ"));
                }


            }

            progressBarStatus = 20;

        } catch (Exception E) {
            progressBarStatus = 20;
        }

        return progressBarStatus;
    }


    /**************************** create route detail ***********************************************/
    // Create DatabaseHelper instance
    public int getRouteDetail(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.ROUTE_DETAIL, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);


                JSONArray ja = jsonObj.getJSONArray("route_detail");

                //  Log.d("route_ja",""+ja) ;




                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    dataHelper.insertRouteDetail(userid,
                            jo.getString("doc_date"),
                            jo.getString("route_code"),
                            jo.getString("route_name"),
                            jo.getString("kunnr"),
                            jo.getString("partner"),
                            jo.getString("partner_class"),
                            jo.getString("latitude"),
                            jo.getString("longitude"),
                            jo.getString("partner_name"),
                            jo.getString("land1"),
                            jo.getString("land_txt"),
                            jo.getString("state_code"),
                            jo.getString("state_txt"),
                            jo.getString("district_code"),
                            jo.getString("district_txt"),
                            jo.getString("taluka_code"),
                            jo.getString("taluka_txt"),
                            jo.getString("address"),
                            jo.getString("email"),
                            jo.getString("mob_no"),
                            jo.getString("tel_number"),
                            jo.getString("pincode"),
                            jo.getString("contact_person"),
                            jo.getString("distributor_code"),
                            jo.getString("distributor_name"),
                            jo.getString("phone_number"),
                            jo.getString("vkorg"),
                            jo.getString("vtweg"),
                            jo.getString("interested"),
                            jo.getString("ktokd")

                    );


                }

                /*********************************** parsing partner type & class json ************************/

                JSONArray partner_type_class = jsonObj.getJSONArray("partner_type_class");

                for (int i = 0; i < partner_type_class.length(); i++) {

                    JSONObject jo = partner_type_class.getJSONObject(i);

                    dataHelper.insertPartnerTypeClassHelp(
                            jo.getString("partner"),
                            jo.getString("partner_txt"),
                            jo.getString("partner_class"),
                            jo.getString("partner_class_txt"));
                }

/***********************************   parsing area distributor j son *************************/
                JSONArray area_distributor = jsonObj.getJSONArray("area_distributor");


             //   dataHelper.deleteAreaDistributor();
                for (int i = 0; i < area_distributor.length(); i++) {

                    JSONObject jo = area_distributor.getJSONObject(i);

                    dataHelper.insertAreaDistributor(
                            jo.getString("distributor_code"),
                            jo.getString("distributor_name"),
                            jo.getString("land_txt"),
                            jo.getString("state_txt"),
                            jo.getString("district_txt"),
                            jo.getString("taluka_txt")


                    );

                    //Log.d("bean2",""+jo.getString("distributor_name")+"--"+jo.getString("state_txt"));
                }

/***********************************   parsing adhoc order customer json *************************/
                JSONArray adhoc_order_customer = jsonObj.getJSONArray("adhoc_order_customer");

                //   Log.d("adhoc_order_customer",""+adhoc_order_customer  ) ;



                for (int i = 0; i < adhoc_order_customer.length(); i++) {

                    JSONObject jo = adhoc_order_customer.getJSONObject(i);

                    dataHelper.insertAdhocOrderCustomer(
                            jo.getString("pernr"),
                            jo.getString("phone_number"),
                            jo.getString("partner_name"),
                            jo.getString("partner_class"),
                            jo.getString("partner_type"),
                            jo.getString("country"),
                            jo.getString("district")

                    );
                }


                /*********************************** parsing partner view survey  ************************/

                JSONArray view_survey = jsonObj.getJSONArray("view_survey");



                for (int i = 0; i < view_survey.length(); i++) {

                    JSONObject jo = view_survey.getJSONObject(i);

                    dataHelper.insertViewSurvey(
                            jo.getString("ename"),
                            jo.getString("erdat"),
                            jo.getString("remark"),
                            jo.getString("outer_url"),
                            jo.getString("inner_url"),
                            jo.getString("other_url"),
                            jo.getString("owner_url"),
                            jo.getString("card_url"),
                            jo.getString("phone_number"),
                            "sap"
                    );
                }


            }

            progressBarStatus = 100;
        } catch (Exception E) {

            progressBarStatus = 100;
        }
        return progressBarStatus;
    }


    /**************************** create target table ***********************************************/

    // Create DatabaseHelper instance
    public int getTargetData(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));
       // param.add(new BasicNameValuePair("PERNR", "00003894"));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.TARGET_PAGE, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("target");



                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertTarget(jo.getString("begda"),
                            jo.getString("endda"),
                            jo.getString("fr_pernr"),
                            jo.getString("fr_ename"),
                            jo.getString("fr_department"),
                            jo.getString("fr_target"),
                            jo.getString("fr_net_sale"),
                            jo.getString("fr_position"));

                }

//           ***********   get activity target ***********

                JSONArray activity_ja = jsonObj.getJSONArray("activity_target");

                Log.d("activity_ja",""+activity_ja) ;



                for (int i = 0; i < activity_ja.length(); i++) {

                    JSONObject activity_jo = activity_ja.getJSONObject(i);

                    dataHelper.insertActivityTarget(activity_jo.getString("begda"),
                            activity_jo.getString("endda"),
                            activity_jo.getString("pernr"),
                            activity_jo.getString("ename"),
                            activity_jo.getString("activity"),
                            activity_jo.getString("vtext"),
                            activity_jo.getString("target_i"),
                            activity_jo.getString("ach_i"),
                            activity_jo.getString("target_h"),
                            activity_jo.getString("ach_h"));

                }


            }

            progressBarStatus = 60;

        } catch (Exception E) {
            progressBarStatus = 60;

        }

        return progressBarStatus;
    }

    /**************************** create visit history ***********************************************/

    // Create DatabaseHelper instance
    public int getVisitHistory(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.VISIT_HISTORY_PAGE, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                JSONArray ja = jsonObj.getJSONArray("visit_history");

                //  Log.d("sap",""+ ja);



                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);


                    dataHelper.insertvisitHistory(jo.getString("date"),
                            jo.getString("time"),
                            jo.getString("comment"),
                            jo.getString("person"),
                            jo.getString("phone_number"),
                            jo.getString("partner_name"),
                            jo.getString("visit"),
                            jo.getString("audio_record"));


                }


            }

            progressBarStatus = 60;

        } catch (Exception E) {
            progressBarStatus = 60;

        }

        return progressBarStatus;
    }

    /**************************** create search help table ***********************************************/

    // Create DatabaseHelper instance
    public int getSearchHelp(Context context) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.SEARCH_HELP_PAGE, param);

            // Log.d("search_help",""+ obj);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                JSONArray ja = jsonObj.getJSONArray("app_search_help");




                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);


                    dataHelper.insertDsrEntryHelp(
                            jo.getString("help_code"),
                            jo.getString("help_name"));

                }


                /*********************************** parsing video gallery json ************************/

                JSONArray video_gallery = jsonObj.getJSONArray("video_gallery");



                for (int i = 0; i < video_gallery.length(); i++) {

                    JSONObject jo = video_gallery.getJSONObject(i);

                    dataHelper.insertVideoGallery(
                            jo.getString("type"),
                            jo.getString("link_name"),
                            jo.getString("video_id"));
                }


                /*********************************** parsing chat app api ************************/

                JSONArray chat_app = jsonObj.getJSONArray("chat_app_api");


                pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
                editor = pref.edit();

                for (int i = 0; i < chat_app.length(); i++) {

                    JSONObject jo = chat_app.getJSONObject(i);
                    editor.putString("chat_app_username", jo.getString("username"));
                    editor.putString("chat_app_password", jo.getString("password"));
                    editor.putString("chat_app_api", jo.getString("api"));
                    editor.putString("chat_app_group_id", jo.getString("group_id"));

                    editor.commit(); //

                }


//           *********************** multiple group api **** ******************************
                JSONArray chat_group = jsonObj.getJSONArray("chat_app_group");





                for (int i = 0; i < chat_group.length(); i++) {

                    JSONObject jo = chat_group.getJSONObject(i);
//                    editor.putString("chat_app_username",jo.getString("username"));
//                    editor.putString("chat_app_password", jo.getString("password"));
//                    editor.putString("chat_app_api", jo.getString("api"));
//                    editor.putString("chat_app_group_id", jo.getString("group_id"));
//


                    //    Log.d("chat_app_inst1",""+i +"" + jo.getString("username") +"--" +  jo.getString("api") +"--"+ jo.getString("group_id"))    ;

                    dataHelper.insertChatAppGroup(
                            userid,
                            jo.getString("username"),
                            jo.getString("password"),
                            jo.getString("api"),
                            jo.getString("group_id")

                    );

                }


            }

            progressBarStatus = 80;

        } catch (Exception E) {
            progressBarStatus = 80;

        }

        return progressBarStatus;
    }


// get material detail

    public int getMaterialDetail(Context context) {

        //  Log.d("point","check point");
        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        //dataHelper.deleteAdhocOrder();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("pernr", userid));

        try {

            String obj = CustomHttpClient.executeHttpPost1(WebURL.ORDER_MATERIAL_DATA, param);

            JSONArray ja_matnr = new JSONArray(obj);

            // Log.d("json55",""+obj);

            for (int i = 0; i < ja_matnr.length(); i++) {

                JSONObject jo_matnr = ja_matnr.getJSONObject(i);


                matnr = jo_matnr.optString("matnr");
                kunnr = jo_matnr.optString("kunnr");
                vkorg = jo_matnr.optString("vkorg");
                vtweg = jo_matnr.optString("vtweg");
                extwg = jo_matnr.optString("extwg");
                maktx = jo_matnr.optString("maktx");
                kbetr = jo_matnr.optString("kbetr");
                konwa = jo_matnr.optString("konwa");
                mtart = jo_matnr.optString("mtart");



               /*     matnr = jo_matnr.getString("matnr");
                    kunnr = jo_matnr.getString("kunnr");
                    vkorg = jo_matnr.getString("vkorg");
                    vtweg = jo_matnr.getString("vtweg");
                    extwg = jo_matnr.getString("extwg");
                    maktx = jo_matnr.getString("maktx");
                    kbetr = jo_matnr.getString("kbetr");
                    konwa = jo_matnr.getString("konwa");
                    mtart = jo_matnr.getString("mtart");*/


                //  Log.d("json1",matnr+"--"+kunnr+"--"+vkorg+"--"+vtweg+"--"+extwg+"--"+maktx+"--"+kbetr+"--"+mtart+"--"+konwa);


                BeanProduct beanProduct = new BeanProduct(matnr, kunnr, vkorg, vtweg, extwg, maktx, kbetr, konwa, mtart);
                dataHelper.createProduct(beanProduct);

                // take order
                // new Capture_employee_gps_location(context,"7");


            }
            progressBarStatus = 40;

        } catch (Exception e) {
            progressBarStatus = 40;
            Log.d("msg", "" + e);
        }


        return progressBarStatus;
    }


    /**************************** get material analysis ***********************************************/

    // Create DatabaseHelper instance
    public void getMaterialAnalysis(Context context) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.MATERIAL_ANALYSIS, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                JSONArray ja = jsonObj.getJSONArray("material_analysis");

                //   Log.d("mat_ana",""+ja) ;




                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertMaterialAnalysis
                            (jo.getString("matnr"),
                                    jo.getString("maktx"),
                                    jo.getString("model"),
                                    jo.getString("plant"),
                                    jo.getString("indicator"),
                                    jo.getString("delivery_time"),
                                    jo.getString("kbetr"),
                                    jo.getString("konwa"));
                }


            }


        } catch (Exception E) {


        }


    }

    /**************************** get custome complaint ***********************************************/


    public void getCustomerComplaint(Context context) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));
       //param.add(new BasicNameValuePair("PERNR", "00001432"));
        param.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(context,"objs")));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CUSTOMER_COMPLAINT, param);

            Log.e("complaint","data"+ obj );

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("complain_header");



                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    Date date1;
                    Date date2;

                    SimpleDateFormat curdt = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    String CurrentDate= curdt.format(new Date());
                    String FinalDate1=formateDate(jo.getString("cmpdt"));
                    String dayDifference = null;

                    try {
                        date1 = curdt.parse(CurrentDate);
                        date2 = curdt.parse(FinalDate1);
                        long difference = 0;
                        if (date1 != null) {
                            if (date2 != null) {
                                difference = Math.abs(date1.getTime() - date2.getTime());
                            }
                        }
                        long differenceDates = difference / (24 * 60 * 60 * 1000);
                        dayDifference = Long.toString(differenceDates);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    dataHelper.insertZCMPLNHDR
                            (jo.getString("cmpno"),
                                    jo.getString("cmpdt"),
                                    jo.getString("caddress"),
                                    jo.getString("mblno"),
                                    jo.getString("mblno1"),
                                    jo.getString("cstname"),
                                    jo.getString("kunnr"),
                                    jo.getString("name1"),
                                    jo.getString("pernr"),
                                    jo.getString("ename"),
                                    jo.getString("catgry"),
                                    jo.getString("status"),
                                    jo.getString("edit"),
                                    jo.getString("epc"),
                                    dayDifference,
                                    jo.getString("cmp_pen_re"),
                                    jo.getString("fdate"),
                                    jo.getString("await_apr_pernr"),
                                    jo.getString("await_apr_pernr_nm"),
                                    jo.getString("pend_apr_pernr"),
                                    jo.getString("pend_apr_pernr_nm"),
                                    jo.getString("await_approval"),
                                    jo.getString("pend_approval"),
                                    jo.getString("await_apr_remark"),
                                    jo.getString("pend_apr_remark")

                            );
                }


//  *******************   complaint detail ********************************************

                JSONArray ja_dtl = jsonObj.getJSONArray("complain_detail");



                for (int i = 0; i < ja_dtl.length(); i++) {
                    JSONObject jo = ja_dtl.getJSONObject(i);

                    dataHelper.insertZCMPLNDTL
                            (jo.getString("cmpno"),
                                    jo.getString("posnr"),
                                    jo.getString("matnr"),
                                    jo.getString("maktx"),
                                    jo.getString("reason"),
                                    jo.getString("warrantee"),
                                    jo.getString("sernr"),
                                    jo.getString("closer_reason"),
                                    jo.getString("defect"),
                                    jo.getString("extwg"),
                                    jo.getString("payment_by"),
                                    jo.getString("cusamt"),
                                    jo.getString("delamt"),
                                    jo.getString("comamt"),
                                    jo.getString("re_comp"),
                                    jo.getString("re_del"),
                                    jo.getString("pay_to_freelancer"),
                                    jo.getString("history"),
                                    jo.getString("vbeln"),
                                    jo.getString("fkdat"),
                                    jo.getString("insurance_txt"),
                                    jo.getString("warranty_condition"),
                                    jo.getString("cmpln_related_to"),
                                    jo.getString("war_date")

                            );
                }

// **********************  complaint category *******************************************

                JSONArray ja_category = jsonObj.getJSONArray("complain_category");



                for (int i = 0; i < ja_category.length(); i++) {
                    JSONObject jo = ja_category.getJSONObject(i);

                    dataHelper.insertZCMPLN_CATEGORY
                            (jo.getString("category"));
                }


                // ***************************  complaint defect type ********************************

                JSONArray ja_defect = jsonObj.getJSONArray("complain_defect");


                for (int i = 0; i < ja_defect.length(); i++) {
                    JSONObject jo = ja_defect.getJSONObject(i);

                    dataHelper.insertZCMPLN_DEFECT_TYPE
                            (jo.getString("defect"));
                }

                // ***************************  complaint related to ********************************

                JSONArray ja_cmp_relt_to = jsonObj.getJSONArray("complain_related_to");



                for (int i = 0; i < ja_cmp_relt_to.length(); i++) {
                    JSONObject jo = ja_cmp_relt_to.getJSONObject(i);

                    dataHelper.insertZCMPLN_RELT_TO(jo.getString("cmpln_related_to"));
                }

                // ***************************  complaint closer reason ********************************

                JSONArray ja_closer = jsonObj.getJSONArray("complain_closer");

                //Log.d("closer",""+ ja_closer );

                dataHelper.deleteCloser();

                for (int i = 0; i < ja_closer.length(); i++) {
                    JSONObject jo = ja_closer.getJSONObject(i);

                    dataHelper.insertZCMPLN_CLOSER
                            (jo.getString("extwg"),
                                    jo.getString("reason")
                            );
                }


// **********************  complaint action  *******************************************

                JSONArray ja_action = jsonObj.getJSONArray("complain_action");



                for (int i = 0; i < ja_action.length(); i++) {
                    JSONObject jo_action = ja_action.getJSONObject(i);

                    dataHelper.insertComplaintAction
                            (jo_action.getString("pernr"),
                                    jo_action.getString("ename"),
                                    jo_action.getString("cmpno"),
                                    jo_action.getString("fdate"),
                                    jo_action.getString("action"),
                                    jo_action.getString("aedtm"),
                                    jo_action.getString("chtm"),
                                    jo_action.getString("status")
                            );
                }


// **********************  complaint image  *******************************************

                JSONArray ja_image = jsonObj.getJSONArray("complain_image");

                Log.d("ja_image", "" + ja_image);



                for (int i = 0; i < ja_image.length(); i++) {
                    JSONObject jo_image = ja_image.getJSONObject(i);

                    dataHelper.insertComplaintImageName
                            (jo_image.getString("catgry"),
                                    jo_image.getString("item"),
                                    jo_image.getString("name")
                            );
                }

                // **********************  complaint pending reason  *******************************************

                JSONArray ja_pend_reason = jsonObj.getJSONArray("pending_reason");

                Log.d("ja_pend_reason", "" + ja_pend_reason);
                Log.e("ja_pend_reason", "" + ja_pend_reason);



                for (int i = 0; i < ja_pend_reason.length(); i++) {
                    JSONObject jo_pdf = ja_pend_reason.getJSONObject(i);

                    dataHelper.insertpendingreason
                            (jo_pdf.getString("cmp_pen_re"),jo_pdf.getString("name"));
                }


//           *********************** multiple chat app group api **** ******************************
                JSONArray chat_group = jsonObj.getJSONArray("chat_app_group");





                for (int i = 0; i < chat_group.length(); i++) {

                    JSONObject jo = chat_group.getJSONObject(i);

                    dataHelper.insertChatAppGroup(
                            userid,
                            jo.getString("username"),
                            jo.getString("password"),
                            jo.getString("api"),
                            jo.getString("group_id")

                    );
                }


            }


        } catch (Exception E) {
          E.printStackTrace();

        }


    }


    // get serial no history
    public String getComplaintSerailNoHistory(Context context, String cmp_no, String cmp_sernr, String cmp_matnr) {

        String lv_cmpno = "null";
        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));
        param.add(new BasicNameValuePair("OBJS", CustomUtility.getSharedPreferences(context,"objs")));
        param.add(new BasicNameValuePair("CMPNO", cmp_no));
        param.add(new BasicNameValuePair("SERNR", cmp_sernr));
        param.add(new BasicNameValuePair("MATNR", cmp_matnr));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CUSTOMER_COMPLAINT_SERIAL_NUMBER, param);


            // Log.d("ser_complaint",""+ obj );
            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("serial_no_complain_header");

                //  Log.d("ser_complaint1",""+ ja );




                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    lv_cmpno = jo.getString("cmpno");

                    dataHelper.insertSerial_Number_ZCMPLNHDR
                            (jo.getString("cmpno"),
                                    jo.getString("cmpdt"),
                                    jo.getString("caddress"),
                                    jo.getString("mblno"),
                                    jo.getString("cstname"),
                                    jo.getString("kunnr"),
                                    jo.getString("name1"),
                                    jo.getString("pernr"),
                                    jo.getString("ename"),
                                    jo.getString("catgry"),
                                    jo.getString("status"),
                                    jo.getString("edit")

                            );
                }


//  *******************   complaint detail ********************************************

                JSONArray ja_dtl = jsonObj.getJSONArray("serial_no_complain_detail");



                for (int i = 0; i < ja_dtl.length(); i++) {
                    JSONObject jo = ja_dtl.getJSONObject(i);

                    dataHelper.insertSerial_Number_ZCMPLNDTL
                            (jo.getString("cmpno"),
                                    jo.getString("posnr"),
                                    jo.getString("matnr"),
                                    jo.getString("maktx"),
                                    jo.getString("reason"),
                                    jo.getString("warrantee"),
                                    jo.getString("sernr"),
                                    jo.getString("closer_reason"),
                                    jo.getString("defect"),
                                    jo.getString("extwg"),
                                    jo.getString("payment_by"),
                                    jo.getString("cusamt"),
                                    jo.getString("delamt"),
                                    jo.getString("comamt"),
                                    jo.getString("re_comp"),
                                    jo.getString("re_del")

                            );
                }
            }
        } catch (Exception E) {
        }


        return lv_cmpno;
    }


    /**************************** get service center list ***********************************************/


    public void getServiceCenterList(Context context) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.SERVICE_CENTER_LIST, param);

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                JSONArray ja = jsonObj.getJSONArray("service_center");




                for (int i = 0; i < ja.length(); i++) {


                    JSONObject jo = ja.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertServiceCenter
                            (
                                    jo.getString("pernr"),
                                    jo.getString("ename"),
                                    jo.getString("kunnr"),
                                    jo.getString("name1"),
                                    jo.getString("land"),
                                    jo.getString("land_txt"),
                                    jo.getString("regio"),
                                    jo.getString("state_txt"),
                                    jo.getString("city"),
                                    jo.getString("district_txt"),
                                    jo.getString("taluka"),
                                    jo.getString("taluka_txt"),
                                    jo.getString("mobno"),
                                    jo.getString("telf2"),
                                    jo.getString("contact_person"),
                                    jo.getString("address"),
                                    jo.getString("pincode"),
                                    jo.getString("email"),
                                    jo.getString("lat_long")
                            );
                }


            }


        } catch (Exception E) {


        }


    }

    /************************************ country ***********************************************/


    public void getLocationData(Context context) {


        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("PERNR", userid));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.LOCATION_HELP, param);


            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);


                JSONArray ja_country = jsonObj.getJSONArray("help_country");



                for (int i = 0; i < ja_country.length(); i++) {
                    JSONObject jo_country = ja_country.getJSONObject(i);

                    dataHelper.insertCountry
                            (
                                    jo_country.getString("land1"),
                                    jo_country.getString("landx")
                            );
                }

// ** state
                JSONArray ja_state = jsonObj.getJSONArray("help_state");

                for (int i = 0; i < ja_state.length(); i++) {
                    JSONObject jo_state = ja_state.getJSONObject(i);
                    //   Log.d("state",""+ jo_state  );
                    dataHelper.insertState
                            (
                                    jo_state.getString("land1"),
                                    jo_state.getString("bland"),
                                    jo_state.getString("bezei")
                            );
                }

//  ** city
                JSONArray ja_city = jsonObj.getJSONArray("help_city");

                for (int i = 0; i < ja_city.length(); i++) {
                    JSONObject jo_city = ja_city.getJSONObject(i);
                    //    Log.d("city",""+ jo_city   );
                    dataHelper.insertCity
                            (
                                    jo_city.getString("land1"),
                                    jo_city.getString("regio"),
                                    jo_city.getString("cityc"),
                                    jo_city.getString("bezei")
                            );
                }

//
//  ** tehsil
                JSONArray ja_tehsil = jsonObj.getJSONArray("help_tehsil");

                for (int i = 0; i < ja_tehsil.length(); i++) {
                    JSONObject jo_tehsil = ja_tehsil.getJSONObject(i);
                    //   Log.d("city",""+ jo_tehsil   );
                    dataHelper.insertTehsil
                            (
                                    jo_tehsil.getString("land1"),
                                    jo_tehsil.getString("regio"),
                                    jo_tehsil.getString("district"),
                                    jo_tehsil.getString("tehsil"),
                                    jo_tehsil.getString("tehsil_text")
                            );
                }


            }

        } catch (Exception E) {
            E.printStackTrace();

        }
    }

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("dd.MM.yyyy");
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = newworkorder SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("MM/dd/yyyy");

            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    /**************************** create attendance table ***********************************************/
    // Create DatabaseHelper instance
    public int getCmpAttachData(Context context, String cmpno) {

        int progressBarStatus = 0;

        DatabaseHelper dataHelper = new DatabaseHelper(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        param.add(new BasicNameValuePair("CMPNO", cmpno));
        param.add(new BasicNameValuePair("POSNR", "000001"));


        try {
            String obj = CustomHttpClient.executeHttpPost1(WebURL.CMPLN_ATTACHMENT, param);

            if (obj != null) {

                String Image1 = null,Image2 = null,Image3 = null,Image4 = null,Image5 = null,Image6 = null,Image7 = null,Image8 = null,Image9 = null,
                        Image10 = null,Image11 = null,Image12 = null,Image13 = null, Image14 = null,Image15 = null;

                JSONObject jsonObj = new JSONObject(obj);

                JSONArray ja = jsonObj.getJSONArray("complaint_attachment");

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);
                    Log.e("DATA","&&&&"+jo.length());

                    if(!jo.getString("image_1").equalsIgnoreCase(""))
                    {
                         Image1 = resizeBase64Image(jo.getString("image_1"));
                    }
                    else{
                        Image1 = "";
                    }
                    if(!jo.getString("image_2").equalsIgnoreCase("")) {
                         Image2 = resizeBase64Image(jo.getString("image_2"));
                    }
                    else{
                        Image2 = "";
                    }
                    if(!jo.getString("image_3").equalsIgnoreCase("")) {
                         Image3 = resizeBase64Image(jo.getString("image_3"));
                    }
                    else{
                        Image3 = "";
                    }
                    if(!jo.getString("image_4").equalsIgnoreCase("")) {
                         Image4 = resizeBase64Image(jo.getString("image_4"));
                    }
                    else{
                        Image4 = "";
                    }
                    if(!jo.getString("image_5").equalsIgnoreCase("")) {
                         Image5 = resizeBase64Image(jo.getString("image_5"));
                    }
                    else{
                        Image5 = "";
                    }
                    if(!jo.getString("image_6").equalsIgnoreCase("")) {
                         Image6 = resizeBase64Image(jo.getString("image_6"));
                    }
                    else{
                        Image6 = "";
                    }
                    if(!jo.getString("image_7").equalsIgnoreCase("")) {
                         Image7 = resizeBase64Image(jo.getString("image_7"));
                    }
                    else{
                        Image7 = "";
                    }
                    if(!jo.getString("image_8").equalsIgnoreCase("")) {
                         Image8 = resizeBase64Image(jo.getString("image_8"));
                    }
                    else{
                        Image8 = "";
                    }
                    if(!jo.getString("image_9").equalsIgnoreCase("")) {
                        Image9 = resizeBase64Image(jo.getString("image_9"));
                    }
                    else{
                        Image9 = "";
                    }
                    if(!jo.getString("image_10").equalsIgnoreCase("")) {
                         Image10 = resizeBase64Image(jo.getString("image_10"));
                    }
                    else{
                        Image10 = "";
                    }
                    if(!jo.getString("image_11").equalsIgnoreCase("")) {
                         Image11 = resizeBase64Image(jo.getString("image_11"));
                    }
                    else{
                        Image11 = "";
                    }
                    if(!jo.getString("image_12").equalsIgnoreCase("")) {
                         Image12 = resizeBase64Image(jo.getString("image_12"));
                    }
                    else{
                        Image12 = "";
                    }
                    if(!jo.getString("image_13").equalsIgnoreCase("")) {
                         Image13 = resizeBase64Image(jo.getString("image_13"));
                    }
                    else{
                        Image13 = "";
                    }
                    if(!jo.getString("image_14").equalsIgnoreCase("")) {
                         Image14 = resizeBase64Image(jo.getString("image_14"));
                    }
                    else{
                        Image14 = "";
                    }
                    if(!jo.getString("image_15").equalsIgnoreCase("")) {
                         Image15 = resizeBase64Image(jo.getString("image_15"));
                    }
                    else{
                        Image15 = "";
                    }

                        dataHelper.insertCmpattach(cmpno,
                                Image1,
                                Image2,
                                Image3,
                                Image4,
                                Image5,
                                Image6,
                                Image7,
                                Image8,
                                Image9,
                                Image10,
                                Image11,
                                Image12,
                                Image13,
                                Image14,
                                Image15

                        );
                    }
            }

            progressBarStatus = 100;

        } catch (Exception E) {
            E.printStackTrace();
            progressBarStatus = 100;
        }

        return progressBarStatus;
    }

    public String resizeBase64Image(String base64image) {

        Log.e("String","&&&"+base64image);

            byte[] encodeByte = Base64.decode(base64image, Base64.DEFAULT);
       /* BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;*/
            Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            Log.e("Image","&&&"+image);
            if (image.getHeight() <= 400 && image.getWidth() <= 400) {
                return base64image;
            }
            image = Bitmap.createScaledBitmap(image, 720, 900, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);


            byte[] b = baos.toByteArray();
            System.gc();
            return Base64.encodeToString(b, Base64.NO_WRAP);

}



}



