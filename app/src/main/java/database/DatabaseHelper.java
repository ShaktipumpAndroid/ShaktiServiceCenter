package database;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import activity.BeanVk.ComplainServicePhotoResponse;
import activity.CustomUtility;
import bean.AttendanceBean;
import bean.BeanProduct;
import bean.BeanProductFinal;
import bean.CmpReviewImageBean;
import bean.ImageModel;
import bean.LocalConvenienceBean;
import bean.LoginBean;
import bean.SubordinateAssginComplainBean;
import bean.SubordinateBean;
import bean.SubordinateVisitedComplainBean;
import bean.WayPoints;

/**
 * Created by shakti on 10/19/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database Name
    public static final String DATABASE_NAME = "db_sales_employee";
    // Database Version
    public static final int DATABASE_VERSION = 27;
    // Table Names
    public static final String TABLE_LOGIN = "tbl_login";
    public static final String TABLE_ATTENDANCE = "tbl_attendance_report";
    public static final String TABLE_TARGET = "tbl_target";
    public static final String TABLE_ACTIVITY_TARGET = "tbl_activity_target";
    public static final String TABLE_SEARCH_HELP = "tbl_search_help";
    public static final String TABLE_DSR_ENTRY = "tbl_dsr_entry";
    public static final String TABLE_FRWD_APP_CMP = "tbl_frwd_app_cmp";
    public static final String TABLE_PEND_APP_CMP = "tbl_pend_app_cmp";
    public static final String TABLE_ROUTE_DETAIL = "tbl_route_detail";
    public static final String TABLE_NEW_ADDED_CUSTOMER = "tbl_new_added_customer";
    public static final String TABLE_CHECK_IN_OUT = "tbl_check_in_out";
    public static final String TABLE_CMP_ATTACHMENT = "tbl_cmpln_attachment";
    public static final String TABLE_PARTNER_TYPE_CLASS_HELP = "tbl_partner_type_class";
    public static final String TABLE_AREA_DISTRIBUTOR = "tbl_area_distributor";
    public static final String TABLE_MARK_ATTENDANCE = "tbl_mark_attendance";
    public static final String TABLE_NO_ORDER = "tbl_no_order";
    public static final String TABLE_ADHOC_ORDER_CUSTOMER = "tbl_adhoc_order_customer";
    public static final String TABLE_ADHOC_FINAL = "tbl_adhocorder_final";
    public static final String TABLE_EMPLOYEE_GPS_ACTIVITY = "tbl_employee_activity";
    public static final String TABLE_VISIT_HISTORY = "tbl_visit_history";
    public static final String TABLE_LOCAL_CONVENIENCE = "tbl_local_convenience";
    public static final String TABLE_SURVEY = "tbl_survey";
    public static final String TABLE_VIEW_SURVEY = "tbl_view_survey";
    public static final String TABLE_MATERIAL_ANALYSIS = "tbl_material_analysis";
    public static final String TABLE_VIDEO_GALLERY = "tbl_video_gallery";
    public static final String TABLE_ZCMPLNHDR = "tbl_zcmplnhdr";
    public static final String TABLE_ZCMPLNHDTL = "tbl_zcmplndtl";
    public static final String TABLE_SERAIL_NUMBER_ZCMPLNHDR = "tbl_serail_number_zcmplnhdr";
    public static final String TABLE_SERAIL_NUMBER_ZCMPLNHDTL = "tbl_serail_number_zcmplndtl";
    public static final String TABLE_COMPLAINT_AUDIO = "tbl_complaint_audio";
    public static final String TABLE_ZCMPLN_CATEGORY = "tbl_zcmpln_category";
    public static final String TABLE_ZCMPLN_DEFECT = "tbl_zcmpln_defect";
    public static final String TABLE_ZCMPLN_RELT_TO = "tbl_zcmpln_relt_to";
    public static final String TABLE_ZCMPLN_CLOSER = "tbl_zcmpln_closer";
    public static final String TABLE_ZINPROCESS_COMPLAINT = "tbl_zinprocess_complaint";
    public static final String TABLE_COMPLAINT_ACTION = "tbl_complaint_action";
    public static final String TABLE_PENDING_REASON = "tbl_complaint_pending_reason";
    public static final String TABLE_COMPLAINT_IMAGE_NAME = "tbl_complaint_image_name";
    //public static final String TABLE_COMPLAINT_PDF_NAME = "tbl_complaint_pdf_name";
    public static final String TABLE_CLOSE_COMPLAINT = "tbl_close_complaint";
    public static final String TABLE_SERVICE_CENTER = "tbl_service_center";
    public static final String TABLE_COUNTRY = "tbl_country";
    public static final String TABLE_STATE = "tbl_sate";
    public static final String TABLE_CITY = "tbl_city";
    public static final String TABLE_TEHSIL = "tbl_tehsil";
    public static final String TABLE_ZCMPLN_IMAGE = "tbl_zcmpln_image";
    public static final String TABLE_CALL_LOG = "tbl_call_log";
    public static final String TABLE_CHAT_APP = "tbl_chat_app";
    public static final String TABLE_DATA_SYNC_CHAT_APP = "tbl_data_sync_chat_app";
    public static final String TABLE_COMPLAINT_DISTANCE = "tbl_complaint_distance";
    public static final String TABLE_REVIEW_COMPLAINT_IMAGES = "tbl_review_complaint_images";
    public static final String TABLE_SUBORDINATE = "tbl_subordinate";
    public static final String TABLE_SERVICE_PHOTO_COMPLAIN  = "tbl_complain_service_photo";
    public static final String TABLE_ASSGIN_COMPLAIN_SUBORDINATE = "tbl_assgin_complain_suborginate";
    public static final String TABLE_VISIT_COMPLAIN_SUBORDINATE = "tbl_visited_complain_suborginate";
    public static final String TABLE_IMAGES = "tbl_images";
    private static final String TABLE_WayPoints = "wayPoints";

    private static final String KEY_PUMPSET_LIFTING = "lifting";
    private static final String KEY_PUMPSET_LOWERING = "lowering";
    private static final String KEY_MATERIAL_LOAD = "loading";
    private static final String KEY_MATERIAL_UNLOAD = "unloading";


    public static final String KEY_IMAGES_ID = "imagesId",KEY_IMAGES_NAME = "imagesName",KEY_IMAGES_PATH = "imagesPath",KEY_IMAGE_SELECTED = "imagesSelected",KEY_IMAGES_BILL_NO= "imagesBillNo";


    public static final String KEY_FR_DATE = "from_date";
    public static final String KEY_TO_DATE = "to_date";
    // Common column names
    public static final String KEY_ID = "id";
    // attendance Table - column name
    public static final String KEY_PERNR = "pernr";
    public static final String KEY_ENAME = "ename";
    public static final String KEY_FOLLOW_UP_DATE = "follow_up_date";
    public static final String KEY_CONVERSION_STATUS = "conversion_status";
    public static final String KEY_SAVE_BY = "save_by";
    public static final String KEY_FR_PERNR = "fr_pernr";
    public static final String KEY_TO_PERNR = "to_pernr";
    public static final String KEY_HELP_NAME = "help_name";
    public static final String KEY_BUDAT = "budat";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_AUDIO_RECORD = "audio_record";
    public static final String KEY_IMAGE = "cmp_image";
    public static final String KEY_PDF1 = "cmp_pdf1";
    public static final String KEY_PDF2 = "cmp_pdf2";
    public static final String KEY_ROUTE_NAME = "route_name";
    public static final String KEY_PARTNER_NAME = "partner_name";
    public static final String KEY_LAND1 = "land1";
    public static final String KEY_LAND_TXT = "land_txt";
    public static final String KEY_STATE_CODE = "state_code";
    public static final String KEY_STATE_TXT = "state_txt";
    public static final String KEY_DISTRICT_CODE = "district_code";
    public static final String KEY_DISTRICT_TXT = "district_txt";
    public static final String KEY_TALUKA_CODE = "taluka_code";
    public static final String KEY_TALUKA_TXT = "taluka_txt";
    public static final String KEY_DISTRIBUTOR_NAME = "distributor_name";
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_CUSTOMER_CATAGORY = "customer_category"; // new = add by mobile app
    public static final String KEY_AADHAR_CARD = "aadhar_card";
    public static final String KEY_PAN_CARD = "pan_card";
    public static final String KEY_TIN_NO = "tin_no";
    public static final String KEY_MARKET_PLACE = "market_place";
    public static final String KEY_DOB = "dob";
    public static final String KEY_INTRESTED = "intrested";
    public static final String KEY_ADDED_AT_LATLONG = "added_at_latlong";
    public static final String KEY_VISIT = "visit";
    public static final String KEY_KTOKD = "ktokd";
    public static final String KEY_LAT_LONG = "lat_long";
    // check in check out table
    public static final String KEY_DATE_IN = "date_in";
    public static final String KEY_TIME_IN = "time_in";
    public static final String KEY_DATE_OUT = "date_out";
    public static final String KEY_TIME_OUT = "time_out";
    public static final String KEY_CHECK_OUT_LATITUDE = "check_out_latitude";
    public static final String KEY_CHECK_OUT_LONGITUDE = "check_out_longitude";
    public static final String KEY_PARTNER_TEXT = "partner_text";
    public static final String TYPE = "TYPE";


    public static final String KEY_BILL_NO = "vbeln";
    public static final String KEY_BILL_DATE = "fkdat";
    public static final String KEY_INSU_TXT = "insurance_txt";
    public static final String KEY_WARR_COND = "warranty_condition";
    public static final String KEY_WAR_DAT = "war_dat";


    // route plan table
    public static final String PERNR = "PERNR";
    public static final String BEGDA = "BEGDA";
    public static final String SERVER_DATE_IN = "SERVER_DATE_IN";
    public static final String SERVER_TIME_IN = "SERVER_TIME_IN";
    public static final String SERVER_DATE_OUT = "SERVER_DATE_OUT";
    public static final String SERVER_TIME_OUT = "SERVER_TIME_OUT";
    public static final String IN_ADDRESS = "IN_ADDRESS";
    public static final String OUT_ADDRESS = "OUT_ADDRESS";
    public static final String IN_TIME = "IN_TIME";
    public static final String OUT_TIME = "OUT_TIME";
    public static final String WORKING_HOURS = "WORKING_HOURS";
    public static final String IMAGE_DATA = "IMAGE_DATA";
    public static final String CURRENT_MILLIS = "CURRENT_MILLIS";
    public static final String IN_LAT_LONG = "IN_LAT_LONG";
    public static final String OUT_LAT_LONG = "OUT_LAT_LONG";
    public static final String IN_FILE_NAME = "IN_FILE_NAME";
    public static final String IN_FILE_LENGTH = "IN_FILE_LENGTH";
    public static final String IN_FILE_VALUE = "IN_FILE_VALUE";
    public static final String OUT_FILE_NAME = "OUT_FILE_NAME";
    public static final String OUT_FILE_LENGTH = "OUT_FILE_LENGTH";
    public static final String OUT_FILE_VALUE = "OUT_FILE_VALUE";
    public static final String IN_STATUS = "IN_STATUS";
    public static final String OUT_STATUS = "OUT_STATUS";
    public static final String IN_IMAGE = "IN_IMAGE";
    public static final String OUT_IMAGE = "OUT_IMAGE";
    public static final String KEY_EXTWG = "extwg";
    public static final String KEY_PERSON = "person";
    public static final String KEY_CR_TIME = "cr_time";
    public static final String KEY_OUTER_VIEW = "outer_view";
    public static final String KEY_INNER_VIEW = "inner_view";
    public static final String KEY_OTHER_VIEW = "other_view";
    public static final String KEY_OWNER_VIEW = "owner_view";
    public static final String KEY_CARD_VIEW = "card_view";
    public static final String KEY_PLANT = "plant";
    public static final String KEY_INDICATOR = "indicator";
    public static final String KEY_DELIVERY_TIME = "delivery_time";
    public static final String KEY_EXTRA1 = "extra1";
    public static final String KEY_EXTRA2 = "extra2";
    public static final String KEY_EXTRA3 = "extra3";
    public static final String KEY_SYNC = "sync";
    public static final String KEY_CHAT_APP = "chat_app";
    // employee activity field
    public static final String KEY_EVENT = "event";
    public static final String KEY_VIDEO_TYPE = "video_type";
    public static final String KEY_VIDEO_NAME = "video_name";
    public static final String KEY_VIDEO_LINK = "video_link";
    public static final String KEY_INSERT = "insert";
    public static final String KEY_UPDATE = "update";


    // attendance table field
    public static final String KEY_CMPNO = "cmpno";
    public static final String KEY_WARRANTY_DURATION = "w_waranty";
    public static final String KEY_DEALER_NAME = "delname";
    public static final String KEY_CUSTOMER_NAME = "cstname";
    public static final String KEY_ENGG_NAME = "engg_name";
    public static final String KEY_MOBILE_NO = "cmblno";
    public static final String KEY_ADDRESS ="caddress";
    public static final String KEY_STATUS = "status";
    public static final String KEY_REMARK = "remark";
    public static final String KEY_CMPDT = "cmpdt";
    public static final String KEY_POSNR = "posnr";
    public static final String KEY_REASON = "reason";
    public static final String KEY_DATE = "cmpdt";
    public static final String KEY_PEND_NO= "cmp_pen_re";
    public static final String KEY_NAME = "name";
    public static final String KEY_CLOSER_RESON = "closer_reason";
    public static final String KEY_DEFECT = "defect";
    public static final String KEY_RELT_TO = "relt_to";
    public static final String KEY_WARRANTY = "warranty";
    public static final String KEY_SERNR = "sernr";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_ITEM = "image_item";
    public static final String KEY_PAY_FREELANCER = "pay_freelancer";
    public static final String KEY_PAY_COM = "re_company";
    public static final String KEY_PAY_DEA = "re_dealer";
    public static final String KEY_FOC_AMT = "focamt";
    public static final String KEY_PEN_DAY = "penday";
    public static final String KEY_PEN_RES = "pen_res";
    public static final String KEY_FLW_DT = "fdate";
    public static final String KEY_AWT_PERNR = "awtpernr";
    public static final String KEY_AWT_PERNR_NM = "awtpernrnm";
    public static final String KEY_PEND_PERNR = "pendpernr";
    public static final String KEY_PEND_PERNR_NM = "pendpernrnm";
    public static final String KEY_AWT_APR = "awtapr";
    public static final String KEY_PEND_APR = "pendapr";
    public static final String KEY_AWT_APR_RMK = "awtrmrk";
    public static final String KEY_PEND_APR_RMK = "pendrmrk";
    public static final String KEY_IMAGE1 = "image1";
    public static final String KEY_IMAGE2 = "image2";
    public static final String KEY_IMAGE3 = "image3";
    public static final String KEY_IMAGE4 = "image4";
    public static final String KEY_IMAGE5 = "image5";
    public static final String KEY_IMAGE6 = "image6";
    public static final String KEY_IMAGE7 = "image7";
    public static final String KEY_IMAGE8 = "image8";
    public static final String KEY_IMAGE9 = "image9";
    public static final String KEY_IMAGE10 = "image10";
    public static final String KEY_IMAGE11 = "image11";
    public static final String KEY_IMAGE12 = "image12";
    public static final String KEY_IMAGE13 = "image13";
    public static final String KEY_IMAGE14 = "image14";
    public static final String KEY_IMAGE15 = "image15";
    public static final String KEY_PAYMENT_BY = "payment_by";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_DEALER = "dealer";
    public static final String KEY_COMPANY = "company";
    public static final String KEY_CMPLN_STATUS = "cmpln_status";
    public static final String KEY_EDIT = "edit";
    public static final String KEY_EPC = "epc";
    public static final String KEY_DOWNLAOD_FROM = "download_from";
    public static final String KEY_CALL_TYPE = "call_type";
    public static final String KEY_CALL_DURATION = "call_duration";
    public static final String KEY_HISTORY = "sernr_history";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_API = "api";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_TABLE_NAME = "table_name";
    public static final String KEY_SYNC_ID = "sync_id";
    public static final String KEY_CELL_ID = "cell_id";
    public static final String KEY_LOCATION_CODE = "location_code";
    public static final String KEY_MOBILE_COUNTRY_CODE = "mobile_country_code";
    public static final String KEY_MOBILE_NETWORK_CODE = "mobile_network_code";

    private static final String TABLE_ADHOC = "tbl_adhocorder";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_BEGDAT = "begdat";
    private static final String KEY_INDZ = "indz";
    private static final String KEY_IODZ = "iodz";
    private static final String KEY_TOTDZ = "totdz";
    private static final String KEY_ATN_STATUS = "atn_status";
    private static final String KEY_LEAVE_TYP = "leave_typ";
    private static final String KEY_SRV_CNT_BORD_IMG = "srv_cnt_bord_img";
    private static final String KEY_SRV_CNT_TRN_LTR_IMG = "srv_cnt_img";
    private static final String KEY_CERTIFICATE_IMG = "certificate_img";
    private static final String KEY_SLFY_SERV_PER = "slfy_serv_per";
    private static final String KEY_SPR_PRT_STK_IMG = "spr_prt_stk_img";
    private static final String KEY_PRD_TRN_IMG = "prd_trn_img";
    private static final String KEY_OTHR_IMG = "othr_img";
    // Target Table - column name
    private static final String KEY_BEGDA = "begda";
    private static final String KEY_ENDDA = "endda";
    private static final String KEY_FR_ENAME = "fr_ename";
    private static final String KEY_FR_DEPARTMENT = "fr_department";
    private static final String KEY_FR_TARGET = "fr_target";
    private static final String KEY_FR_NET_SALE = "fr_net_sale";
    private static final String KEY_FR_POSITION = "fr_position";
    private static final String KEY_INDV_ACT_TARGET = "indv_act_target";
    private static final String KEY_INDV_ACT_ACHIEVEMENT = "indv_act_achievement";
    private static final String KEY_HRCY_ACT_TARGET = "hrcy_act_target";
    private static final String KEY_HRCY_ACT_ACHIEVEMENT = "hrcy_act_achievement";
    private static final String KEY_ACTIVITY_CODE = "activity_code";
    private static final String KEY_ACTIVITY_NAME = "activity_name";
    // dsr entry Table - column name
    private static final String KEY_BTRTL = "btrtl";
    private static final String KEY_HELP_CODE = "help_code";

    // review complaint images
    private static final String KEY_DATABASE_STATUS = "db_status";
    private static final String KEY_ROUTE_CODE = "route_code";
    private static final String KEY_KUNNR = "kunnr";
    private static final String KEY_PARTNER = "partner";
    private static final String KEY_PARTNER_CLASS = "partner_class";
    private static final String KEY_ADDRESS_ = "address";
    private static final String KEY_EMAIL = "email";
    public  static final String KEY_MOB_NO = "mob_no";
    private static final String KEY_ALT_MOB_NO = "alt_mob_no";
    private static final String KEY_TEL_NUMBER = "tel_number";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CONTACT_PERSON = "contact_person";
    private static final String KEY_CONTACT_PERSON_PHONE = "contact_person_phone";
    private static final String KEY_DISTRIBUTOR_CODE = "distributor_code";
    private static final String KEY_CHECK_IN_LATITUDE = "check_in_latitude";
    private static final String KEY_CHECK_IN_LONGITUDE = "check_in_longitude";
    private static final String KEY_PARTNER_CLASS_TEXT = "partner_class_text";
    // Adhoc order table Common column names
    private static final String KEY_MATNR = "matnr";
    private static final String KEY_VKORG = "vkorg";
    private static final String KEY_VTWEG = "vtweg";
    private static final String KEY_MAKTX = "maktx";
    private static final String KEY_KBETR = "kbetr";
    private static final String KEY_MTART = "mtart";
    private static final String KEY_KONWA = "konwa";
    private static final String KEY_MENGE = "menge";
    private static final String KEY_TOT_KBETR = "tot_kbetr";
    private static final String KEY_CUSTO_NAME = "customer_name";
    private static final String KEY_CR_DATE = "cr_date";
    private static final String IMAGE = "IMAGE";
    private static final String IMAGE2 = "IMAGE1";
    private static final String KEY_AGENDA = "agenda";
    private static final String KEY_OUTCOMES = "outcomes";

    public static final String KEY_WayPoints = "wayPoints";
    public static final String KEY_FROM_TIME = "start_time";
    public static final String KEY_TO_TIME = "end_time";
    private static final String KEY_FROM_LAT = "start_lat";
    private static final String KEY_TO_LAT = "end_lat";
    private static final String KEY_FROM_LNG = "start_long";
    private static final String KEY_TO_LNG = "end_long";
    private static final String KEY_START_LOC = "start_location";
    private static final String KEY_END_LOC= "end_location";
    private static final String KEY_DISTANCE = "distance";
    public static final String KEY_PHOTO1 = "photo1";
    public static final String KEY_PHOTO2 = "photo2";
    public static final String KEY_PHOTO3 = "photo3";
    public static final String KEY_PHOTO4 = "photo4";
    public static final String KEY_PHOTO5 = "photo5";
    public static final String KEY_PHOTO6 = "photo6";
    public static final String KEY_PHOTO7 = "photo7";
    public static final String KEY_PHOTO8 = "photo8";
    public static final String KEY_PHOTO9 = "photo9";
    public static final String KEY_PHOTO10 = "photo10";
    public static final String KEY_PHOTO11 = "photo11";
    public static final String KEY_PHOTO12 = "photo12";
    public static final String KEY_PHOTO13 = "photo13";

    public static final String TABLE_STATE_SEARCH = "tbl_state_detail";

    public static final String KEY_ADD1 = "add1";
    public static final String KEY_ADD2 = "add2";
    public static final String KEY_ADD3 = "add3";
    public static final String KEY_ADD4 = "add4";
    public static final String KEY_ADD5 = "add5";
    public static final String KEY_ADD6 = "add6";
    public static final String KEY_ADD7 = "add7";
    public static final String KEY_ADD8 = "add8";
    public static final String KEY_ADD9 = "add9";
    public static final String KEY_ADD10 = "add10";
    public static final String KEY_STATE = "state";
    public static final String KEY_STATE_TEXT = "state_text";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_DISTRICT_TEXT = "district_text";
    public static final String KEY_TEHSIL = "tehsil";
    public static final String KEY_TEHSIL_TEXT = "tehsil_text";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_COUNTRY_TEXT = "country_text";

    private static final String KEY_TASK_DATE_TO = "date_to";

    //  partner type & class search help  table create statement
    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN + "(" + KEY_PERNR + " PRIMARY KEY ,"
            + KEY_ENAME + " TEXT)";
    // Attendance table create statement

    private static final String CREATE_TABLE_SUBORDINATE = "CREATE TABLE "
            + TABLE_SUBORDINATE + "("
            + KEY_AADHAR_CARD + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_FR_DATE + " TEXT,"
            + KEY_TO_DATE + " TEXT)";

    // Review Images table create statement
    private static final String CREATE_TABLE_REVIEW_CMP_IMAGES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_REVIEW_COMPLAINT_IMAGES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_IMAGE1 + " TEXT,"
            + KEY_IMAGE2 + " TEXT,"
            + KEY_IMAGE3 + " TEXT,"
            + KEY_IMAGE4 + " TEXT,"
            + KEY_IMAGE5 + " TEXT,"
            + KEY_IMAGE6 + " TEXT,"
            + KEY_IMAGE7 + " TEXT,"
            + KEY_IMAGE8 + " TEXT,"
            + KEY_IMAGE9 + " TEXT,"
            + KEY_IMAGE10 + " TEXT,"
            + KEY_IMAGE11 + " TEXT,"
            + KEY_IMAGE12 + " TEXT,"
            + KEY_IMAGE13 + " TEXT,"
            + KEY_IMAGE14 + " TEXT,"
            + KEY_IMAGE15 + " TEXT)";


    private static final String CREATE_TABLE_COMPLENE_SERVICE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_SERVICE_PHOTO_COMPLAIN + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_PHOTO1 + " TEXT,"
            + KEY_PHOTO2 + " TEXT,"
            + KEY_PHOTO3 + " TEXT,"
            + KEY_PHOTO4 + " TEXT,"
            + KEY_PHOTO5 + " TEXT,"
            + KEY_PHOTO6 + " TEXT,"
            + KEY_PHOTO7 + " TEXT,"
            + KEY_PHOTO8 + " TEXT,"
            + KEY_PHOTO9 + " TEXT,"
            + KEY_PHOTO10 + " TEXT,"
            + KEY_PHOTO11 + " TEXT,"
            + KEY_PHOTO12 + " TEXT)";

    private static final String CREATE_TABLE_SITE_AUDIT_IMAGES = "CREATE TABLE "
            + TABLE_IMAGES + "("  + KEY_IMAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + KEY_IMAGES_NAME + " TEXT,"
            + KEY_IMAGES_PATH + " TEXT,"
            + KEY_IMAGE_SELECTED + " BOOLEAN,"
            + KEY_PUMPSET_LIFTING + " BOOLEAN,"
            + KEY_PUMPSET_LOWERING + " BOOLEAN,"
            + KEY_MATERIAL_LOAD + " BOOLEAN,"
            + KEY_MATERIAL_UNLOAD + " BOOLEAN,"
            + KEY_IMAGES_BILL_NO + " TEXT)";

    private static final String CREATE_TABLE_WayPoints = "CREATE TABLE IF NOT EXISTS "
            + TABLE_WayPoints + "(" + KEY_ID + " PRIMARY KEY ,"
            + KEY_PERNR + " TEXT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_FROM_TIME + " TEXT,"
            + KEY_TO_TIME + " TEXT,"
            + KEY_WayPoints + " TEXT)";


    private static final String CREATE_TABLE_ASSGIN_COMPLAIN_SUBORDINATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ASSGIN_COMPLAIN_SUBORDINATE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_DEALER_NAME + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_ENGG_NAME + " TEXT,"
            + KEY_MOBILE_NO + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_MATNR + " TEXT,"
            + KEY_WARRANTY + " TEXT,"
            + KEY_MAKTX + " TEXT,"
            + KEY_SERNR + " TEXT,"
            + KEY_REASON + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_WARRANTY_DURATION + " TEXT)";

    private static final String CREATE_TABLE_VISIT_COMPLAIN_SUBORDINATE = "CREATE TABLE IF NOT EXISTS "
            +  TABLE_VISIT_COMPLAIN_SUBORDINATE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CMPNO + " TEXT,"
            + KEY_DEALER_NAME + " TEXT,"
            + KEY_CUSTOMER_NAME + " TEXT,"
            + KEY_MOB_NO + " TEXT,"
            + KEY_DATE + " TEXT)";



    private static final String CREATE_TABLE_LOCAL_CONVENIENCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOCAL_CONVENIENCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_FROM_TIME + " TEXT,"
            + KEY_TO_TIME + " TEXT,"
            + KEY_FROM_LAT + " TEXT,"
            + KEY_FROM_LNG + " TEXT,"
            + KEY_TO_LAT + " TEXT,"
            + KEY_TO_LNG + " TEXT,"
            + KEY_START_LOC + " TEXT,"
            + KEY_END_LOC + " TEXT,"
            + KEY_DISTANCE + " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_TASK_DATE_TO + " TEXT)";

    private static final String CREATE_TABLE_STATE_SEARCH = "CREATE TABLE "
            + TABLE_STATE_SEARCH + "("
            + KEY_COUNTRY + " TEXT,"
            + KEY_COUNTRY_TEXT + " TEXT,"
            + KEY_STATE + " TEXT,"
            + KEY_STATE_TEXT + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_DISTRICT_TEXT + " TEXT,"
            + KEY_TEHSIL + " TEXT,"
            + KEY_ADD1 + " TEXT,"
            + KEY_ADD2 + " TEXT,"
            + KEY_ADD3 + " TEXT,"
            + KEY_ADD4 + " TEXT,"
            + KEY_ADD5 + " TEXT,"
            + KEY_ADD6 + " TEXT,"
            + KEY_ADD7 + " TEXT,"
            + KEY_ADD8 + " TEXT,"
            + KEY_ADD9 + " TEXT,"
            + KEY_ADD10 + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT)";


    public void insertComaplinPhotoData(ComplainServicePhotoResponse mComplainServicePhotoResponse) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try
        {
            values = new ContentValues();
            values.put(KEY_BILL_NO,     mComplainServicePhotoResponse.getComplainNumber());

            values.put(KEY_PHOTO1,     mComplainServicePhotoResponse.getMPhotoValue1());
            values.put(KEY_PHOTO2,     mComplainServicePhotoResponse.getMPhotoValue2());
            values.put(KEY_PHOTO3,     mComplainServicePhotoResponse.getMPhotoValue3());
            values.put(KEY_PHOTO4,     mComplainServicePhotoResponse.getMPhotoValue4());
            values.put(KEY_PHOTO5,     mComplainServicePhotoResponse.getMPhotoValue5());
            values.put(KEY_PHOTO6,     mComplainServicePhotoResponse.getMPhotoValue6());
            values.put(KEY_PHOTO7,     mComplainServicePhotoResponse.getMPhotoValue7());
            values.put(KEY_PHOTO8,     mComplainServicePhotoResponse.getMPhotoValue8());
            values.put(KEY_PHOTO9,     mComplainServicePhotoResponse.getMPhotoValue9());

            // Insert Row
            long i = db.insert(TABLE_SERVICE_PHOTO_COMPLAIN , null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch ( SQLiteException e)
        {

            e.printStackTrace();

        }

        finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    @SuppressLint("Range")
    public CmpReviewImageBean getReviewCmpImage(String cmpno) {

        CmpReviewImageBean cmpReviewImageBean = new CmpReviewImageBean();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            //selectQuery = "SELECT * FROM " + TABLE_ADHOC_FINAL;

            selectQuery = "SELECT * FROM " + TABLE_REVIEW_COMPLAINT_IMAGES + " WHERE " + KEY_CMPNO + " = '" + cmpno + "'";

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    cmpReviewImageBean = new CmpReviewImageBean();

                    cmpReviewImageBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                    cmpReviewImageBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_IMAGE1)));
                    cmpReviewImageBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_IMAGE2)));
                    cmpReviewImageBean.setPhoto3(cursor.getString(cursor.getColumnIndex(KEY_IMAGE3)));
                    cmpReviewImageBean.setPhoto4(cursor.getString(cursor.getColumnIndex(KEY_IMAGE4)));
                    cmpReviewImageBean.setPhoto5(cursor.getString(cursor.getColumnIndex(KEY_IMAGE5)));
                    cmpReviewImageBean.setPhoto6(cursor.getString(cursor.getColumnIndex(KEY_IMAGE6)));
                    cmpReviewImageBean.setPhoto7(cursor.getString(cursor.getColumnIndex(KEY_IMAGE7)));
                    cmpReviewImageBean.setPhoto8(cursor.getString(cursor.getColumnIndex(KEY_IMAGE8)));
                    cmpReviewImageBean.setPhoto9(cursor.getString(cursor.getColumnIndex(KEY_IMAGE9)));
                    /*cmpReviewImageBean.setPhoto10(cursor.getString(cursor.getColumnIndex(KEY_IMAGE10)));
                    cmpReviewImageBean.setPhoto11(cursor.getString(cursor.getColumnIndex(KEY_IMAGE11)));
                    cmpReviewImageBean.setPhoto12(cursor.getString(cursor.getColumnIndex(KEY_IMAGE12)));
                    cmpReviewImageBean.setPhoto13(cursor.getString(cursor.getColumnIndex(KEY_IMAGE13)));

                    cmpReviewImageBean.setPhoto14(cursor.getString(cursor.getColumnIndex(KEY_IMAGE14)));
                    cmpReviewImageBean.setPhoto15(cursor.getString(cursor.getColumnIndex(KEY_IMAGE15)));*/

                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();
        }
        return cmpReviewImageBean;
    }

    public void updateComaplinPhotoData(ComplainServicePhotoResponse mComplainServicePhotoResponse) {
        // Open the database for writing
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        String where = " ";

        try
        {
            values = new ContentValues();
            values.put(KEY_BILL_NO,     mComplainServicePhotoResponse.getComplainNumber());

            values.put(KEY_PHOTO1,     mComplainServicePhotoResponse.getMPhotoValue1());
            values.put(KEY_PHOTO2,     mComplainServicePhotoResponse.getMPhotoValue2());
            values.put(KEY_PHOTO3,     mComplainServicePhotoResponse.getMPhotoValue3());
            values.put(KEY_PHOTO4,     mComplainServicePhotoResponse.getMPhotoValue4());
            values.put(KEY_PHOTO5,     mComplainServicePhotoResponse.getMPhotoValue5());
            values.put(KEY_PHOTO6,     mComplainServicePhotoResponse.getMPhotoValue6());
            values.put(KEY_PHOTO7,     mComplainServicePhotoResponse.getMPhotoValue7());
            values.put(KEY_PHOTO8,     mComplainServicePhotoResponse.getMPhotoValue8());
            values.put(KEY_PHOTO9,     mComplainServicePhotoResponse.getMPhotoValue9());

            // Insert Row
            where = KEY_BILL_NO + "='" + mComplainServicePhotoResponse.getComplainNumber() + "'";
            i = db.update(TABLE_SERVICE_PHOTO_COMPLAIN, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch ( SQLiteException e)
        {

            e.printStackTrace();

        }

        finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }



    int mcc;
    int mnc;
    int cid;
    int lac;
    TelephonyManager telephonyManager;
    GsmCellLocation cellLocation;
    String latlong;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_REVIEW_CMP_IMAGES);
        db.execSQL(CREATE_TABLE_COMPLENE_SERVICE);
        db.execSQL(CREATE_TABLE_STATE_SEARCH);
        db.execSQL(CREATE_TABLE_LOCAL_CONVENIENCE);
        db.execSQL(CREATE_TABLE_SUBORDINATE);
        db.execSQL(CREATE_TABLE_ASSGIN_COMPLAIN_SUBORDINATE);
        db.execSQL(CREATE_TABLE_VISIT_COMPLAIN_SUBORDINATE);
        db.execSQL(CREATE_TABLE_SITE_AUDIT_IMAGES);
        db.execSQL(CREATE_TABLE_WayPoints);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        // add in app version 1.5
        if (newVersion > oldVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);///vikas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_PHOTO_COMPLAIN);///vikas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW_COMPLAINT_IMAGES);//////vikas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_SEARCH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CONVENIENCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBORDINATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSGIN_COMPLAIN_SUBORDINATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISIT_COMPLAIN_SUBORDINATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WayPoints);


            Log.d("newDatabaseVersion123", "" + newVersion);
            onCreate(db);

        }



    }


    public void deleteReviewCmpImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW_COMPLAINT_IMAGES, null, null);


    }

    public void deleteReviewCmpImages(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REVIEW_COMPLAINT_IMAGES, KEY_CMPNO + " = '" + cmpno + "'", null);


    }

    public void deleteComplainPhotoData(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";

        where = KEY_BILL_NO + "='" + value + "'";


        db.delete(TABLE_SERVICE_PHOTO_COMPLAIN, where, null);

    }


    public void deleteDataSyncToChatApp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DATA_SYNC_CHAT_APP, null, null);


    }

    public void deletePendcmpdata(String cmpno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEND_APP_CMP, KEY_CMPNO + " = '" + cmpno + "'", null);


    }






    public void deleteCloser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_CLOSER, null, null);

    }

    public void deleteImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);

    }

    public void deleteImage2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, null, null);

    }


    public void deleteImage1(String cmp_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ZCMPLN_IMAGE, KEY_CMPNO + " ='" + cmp_no + "'" + " AND " + KEY_CATEGORY + " != 'others'", null);

    }








    public void deleteComplaintImage() {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_ZCMPLN_IMAGE, KEY_SYNC + " = 'YES'" + " AND " + KEY_CR_DATE + " != '" + new CustomUtility().getCurrentDate() + "'", null);

    }














    /**********************  insert survey ************************************/
    public void insertSurvey(


            String pernr,
            String budat,
            String time,
            String comment,
            String latitude,
            String longitude,
            String outer_view,
            String inner_view,
            String other_view,
            String owner_view,
            String card_view,
            String phone_number
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        //db.beginTransactionNonExclusive();();
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_OUTER_VIEW, outer_view);
            values.put(KEY_INNER_VIEW, inner_view);
            values.put(KEY_OTHER_VIEW, other_view);
            values.put(KEY_OWNER_VIEW, owner_view);
            values.put(KEY_CARD_VIEW, card_view);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_SURVEY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert survey ************************************/
    public void insertReviewCmpImage(String cmpno,
                                     String image1,
                                     String image2,
                                     String image3,
                                     String image4,
                                     String image5,
                                     String image6,
                                     String image7,
                                     String image8,
                                     String image9,
                                     String image10,
                                     String image11,
                                     String image12,
                                     String image13,
                                     String image14,
                                     String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            // Insert Row
            long i = db.insert(TABLE_REVIEW_COMPLAINT_IMAGES, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    public void updateReviewCmpImage(String cmpno,
                                     String image1,
                                     String image2,
                                     String image3,
                                     String image4,
                                     String image5,
                                     String image6,
                                     String image7,
                                     String image8,
                                     String image9,
                                     String image10,
                                     String image11,
                                     String image12,
                                     String image13,
                                     String image14,
                                     String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;
        String where = "";

        try {

            values = new ContentValues();
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            where = KEY_CMPNO + "='" + cmpno + "'";

            // Insert Row
            long i = db.update(TABLE_REVIEW_COMPLAINT_IMAGES, values, where, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /**********************  insert view survey ************************************/
    public void insertViewSurvey(

            String ename,
            String budat,
            String comment,
            String outer_view,
            String inner_view,
            String other_view,
            String owner_view,
            String card_view,
            String phone_number,
            String download_from

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();

            values.put(KEY_BUDAT, budat);
            values.put(KEY_ENAME, ename);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_OUTER_VIEW, outer_view);
            values.put(KEY_INNER_VIEW, inner_view);
            values.put(KEY_OTHER_VIEW, other_view);
            values.put(KEY_OWNER_VIEW, owner_view);
            values.put(KEY_CARD_VIEW, card_view);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_DOWNLAOD_FROM, download_from);


            // Insert Row
            long i = db.insert(TABLE_VIEW_SURVEY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert login detail ************************************/
    public void insertLoginData(
            String lv_pernr,
            String lv_ename
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, lv_pernr);
            values.put(KEY_ENAME, lv_ename);

            // Insert Row
            long i = db.insert(TABLE_LOGIN, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert area distributor ************************************/
    public void insertAreaDistributor(
            String distributor_code,
            String distributor_name,
            String land_txt,
            String state_txt,
            String district_txt,
            String taluka_txt) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_TXT, taluka_txt);


            // Insert Row
            long i = db.insert(TABLE_AREA_DISTRIBUTOR, null, values);

            //  Log.d("route",""+ i);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert customer for adhoc order ************************************/
    public void insertAdhocOrderCustomer(
            String pernr,
            String phone_number,
            String partner_name,
            String partner_class,
            String partner_type,
            String country,
            String district) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_PARTNER, partner_type);
            values.put(KEY_LAND_TXT, country);
            values.put(KEY_DISTRICT_TXT, district);


            // Insert Row
            long i = db.insert(TABLE_ADHOC_ORDER_CUSTOMER, null, values);

            // Log.d("route",""+ i);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************  insert visit history ************************************/
    public void insertvisitHistory(

            String budat,
            String time,
            String comment,
            String ename,
            String phone_number,
            String partner_name,
            String visit,
            String audio_record) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_ENAME, ename);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_VISIT, visit);
            values.put(KEY_AUDIO_RECORD, audio_record);


            // Insert Row
            long i = db.insert(TABLE_VISIT_HISTORY, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    /**********************  insert visit history ************************************/
    public void insertComplaintAudio(

            String pernr,
            String cmpno,
            String budat,
            String time,
            String audio_record,

            String chat_dealer,
            String chat_customer,
            String chat_address,
            String action
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_CR_TIME, time);
            values.put(KEY_CMPNO, cmpno);

            values.put(KEY_AUDIO_RECORD, audio_record);


            values.put(KEY_CLOSER_RESON, action);
            values.put(KEY_PARTNER_NAME, chat_customer);
            values.put(KEY_DISTRIBUTOR_NAME, chat_dealer);
            values.put(KEY_DISTRICT_TXT, chat_address);


            values.put(KEY_SYNC, "NOT");
            values.put(KEY_CHAT_APP, "NOT");


            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_AUDIO, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }



    public void insertLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());
            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());


            // Insert Row
            long i = db.insert(TABLE_LOCAL_CONVENIENCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void updateLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());

            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());



            // update Row
            long i = db.update(TABLE_LOCAL_CONVENIENCE, values, null, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    /**********************  insert Partner Type Class help ************************************/
    public void insertPartnerTypeClassHelp(
            String partner,
            String partner_text,
            String partner_class,
            String partner_class_text
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_TEXT, partner_text);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_PARTNER_CLASS_TEXT, partner_class_text);
            // Insert Row
            long i = db.insert(TABLE_PARTNER_TYPE_CLASS_HELP, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /********************************  insert  route detail   *************************************/
    public void insertRouteDetail(
            String pernr,
            String budat,
            String route_code,
            String route_name,
            String kunnr,
            String partner,
            String partner_class,
            String latitude,
            String longitude,
            String partner_name,
            String land1,
            String land_txt,
            String state_code,
            String state_txt,
            String district_code,
            String district_txt,
            String taluka_code,
            String taluka_txt,
            String address,
            String email,
            String mob_no,
            String tel_number,
            String pincode,
            String contact_person,
            String distributor_code,
            String distributor_name,
            String phone_number,
            String vkorg,
            String vtweg,
            String interested,
            String ktokd
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_ROUTE_NAME, route_name);
            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_CLASS, partner_class);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, state_code);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_CODE, district_code);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_CODE, taluka_code);
            values.put(KEY_TALUKA_TXT, taluka_txt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_EMAIL, email);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_TEL_NUMBER, tel_number);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_VKORG, vkorg);
            values.put(KEY_VTWEG, vtweg);
            values.put(KEY_INTRESTED, interested);
            values.put(KEY_KTOKD, ktokd);


            // Insert Row
            long i = db.insert(TABLE_ROUTE_DETAIL, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /********************************  insert  new added customer   *************************************/
    public void insertNewAddedCustomer(
            String pernr,

            String route_code,
            String route_name,
            String partner,
            String partner_class,
            String partner_name,
            String land1,
            String land_txt,
            String state_code,
            String state_txt,
            String district_code,
            String district_txt,
            String taluka_code,
            String taluka_txt,
            String address,
            String email,
            String mob_no,
            String tel_number,
            String aadhar_card,
            String pan_card,
            String tin_no,
            String market_place,
            String dob,
            String pincode,
            String contact_person,
            String contact_person_phone,
            String distributor_code,
            String distributor_name,
            String intrested,
            String budat,
            String time,
            String added_at_latlong,
            String vkorg,
            String vtweg

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);

            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_ROUTE_NAME, route_name);

            values.put(KEY_PARTNER, partner);
            values.put(KEY_PARTNER_CLASS, partner_class);

            values.put(KEY_PARTNER_NAME, partner_name);
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, state_code);
            values.put(KEY_STATE_TXT, state_txt);
            values.put(KEY_DISTRICT_CODE, district_code);
            values.put(KEY_DISTRICT_TXT, district_txt);
            values.put(KEY_TALUKA_CODE, taluka_code);
            values.put(KEY_TALUKA_TXT, taluka_txt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_EMAIL, email);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_TEL_NUMBER, tel_number);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_CONTACT_PERSON_PHONE, contact_person_phone);

            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PHONE_NUMBER, mob_no);
            values.put(KEY_AADHAR_CARD, aadhar_card);
            values.put(KEY_PAN_CARD, pan_card);
            values.put(KEY_TIN_NO, tin_no);
            values.put(KEY_MARKET_PLACE, market_place);
            values.put(KEY_DOB, dob);
            values.put(KEY_CUSTOMER_CATAGORY, "NEW");  // new customer addred by mobile app
            values.put(KEY_INTRESTED, intrested);
            values.put(KEY_ADDED_AT_LATLONG, added_at_latlong);
            values.put(KEY_VKORG, vkorg);
            values.put(KEY_VTWEG, vtweg);
            values.put(KEY_LATITUDE, "0.0");
            values.put(KEY_LONGITUDE, "0.0");
            values.put(KEY_KTOKD, "9999");
            values.put(KEY_SYNC, "NOT");
            // Insert Row
            // long i = db.insert(TABLE_ROUTE_DETAIL, null, values);
            long i = db.insert(TABLE_NEW_ADDED_CUSTOMER, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /********************************  insert  attendance ************************************************/
    public void insertAttendance(String pernr,
                                 String begdat,
                                 String indz,
                                 String iodz,
                                 String totdz,
                                 String atn_status,
                                 String leave_typ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pernr);
            values.put(KEY_BEGDAT, begdat);
            values.put(KEY_INDZ, indz);
            values.put(KEY_IODZ, iodz);
            values.put(KEY_TOTDZ, totdz);
            values.put(KEY_ATN_STATUS, atn_status);
            values.put(KEY_LEAVE_TYP, leave_typ);


            // db.delete(TABLE_ATTENDANCE,null,null);

            // Insert Row
            long i = db.insert(TABLE_ATTENDANCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /********************************  insert  attendance ************************************************/
    public void insertCmpattach(String cmpno,
                                 String image1,
                                 String image2,
                                 String image3,
                                 String image4,
                                 String image5,
                                 String image6,
                                 String image7,
                                 String image8,
                                 String image9,
                                 String image10,
                                 String image11,
                                 String image12,
                                 String image13,
                                 String image14,
                                 String image15) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_IMAGE1, image1);
            values.put(KEY_IMAGE2, image2);
            values.put(KEY_IMAGE3, image3);
            values.put(KEY_IMAGE4, image4);
            values.put(KEY_IMAGE5, image5);
            values.put(KEY_IMAGE6, image6);
            values.put(KEY_IMAGE7, image7);
            values.put(KEY_IMAGE8, image8);
            values.put(KEY_IMAGE9, image9);
            values.put(KEY_IMAGE10, image10);
            values.put(KEY_IMAGE11, image11);
            values.put(KEY_IMAGE12, image12);
            values.put(KEY_IMAGE13, image13);
            values.put(KEY_IMAGE14, image14);
            values.put(KEY_IMAGE15, image15);

            // Insert Row
            long i = db.insert(TABLE_CMP_ATTACHMENT, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }



    /******************************* Insert Target **********************************************/
    public void insertTarget(String begda,
                             String endda,
                             String fr_pernr,
                             String fr_ename,
                             String fr_department,
                             String fr_target,
                             String fr_net_sale,
                             String fr_position) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_BEGDA, begda);
            values.put(KEY_ENDDA, endda);
            values.put(KEY_FR_PERNR, fr_pernr);
            values.put(KEY_FR_ENAME, fr_ename);
            values.put(KEY_FR_DEPARTMENT, fr_department);
            values.put(KEY_FR_TARGET, fr_target);
            values.put(KEY_FR_NET_SALE, fr_net_sale);
            values.put(KEY_FR_POSITION, fr_position);
            values.put(KEY_TO_PERNR, LoginBean.getUseid());

            // Insert Row
            long i = db.insert(TABLE_TARGET, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /******************************* insert activity target **********************************************/
    public void insertActivityTarget(
            String begda,
            String endda,
            String pernr,
            String ename,
            String activity_code,
            String activity_name,
            String indv_target,
            String indv_achv,
            String hrcy_target,
            String hrcy_achv
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_BEGDA, begda);
            values.put(KEY_ENDDA, endda);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_ACTIVITY_CODE, activity_code);
            values.put(KEY_ACTIVITY_NAME, activity_name);
            values.put(KEY_INDV_ACT_TARGET, indv_target);
            values.put(KEY_INDV_ACT_ACHIEVEMENT, indv_achv);
            values.put(KEY_HRCY_ACT_TARGET, hrcy_target);
            values.put(KEY_HRCY_ACT_ACHIEVEMENT, hrcy_achv);

            // Insert Row
            long i = db.insert(TABLE_ACTIVITY_TARGET, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert DSR_entry_help data **********************************************/

    public void insertDsrEntryHelp(
            String help_code,
            String help_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_HELP_CODE, help_code);
            values.put(KEY_HELP_NAME, help_name);

            // Insert Row
            long i = db.insert(TABLE_SEARCH_HELP, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert Complaint inprocess data for offline record **********************************************/

    public void insertInprocessComplaint(String pernr,
                                         String ename,
                                         String cmpno,
                                         String follow_up_date,
                                         String reason,
                                         String reasonid,
                                         String cr_date,
                                         String cr_time,
                                         String latitude,
                                         String longitude,
                                         String status
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);

            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_REASON, reason);
            values.put(KEY_NAME, reasonid);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_CMPLN_STATUS, status);


            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_ZINPROCESS_COMPLAINT, null, values);

            //Log.d("audio_action111",""+i+"--"+pernr+"---"+cmpno+"--"+audio_record);


            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert closure request for offline record **********************************************/

    public void insertClosureRequestComplaint(
            String pernr,
            String cmpno,
            String posnr,

            String category,
            /* String  payment_by ,*/
            String dealer,
            String customer,
            String company,
            String freelance,
            String dea,
            String com,
            String closer_reson,
            String defect,
            String relt_to,
            String focamt,

            String cr_date,
            String cr_time,
            String latitude,
            String longitude

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_CATEGORY, category);
            /* values.put( KEY_PAYMENT_BY ,payment_by);*/
            values.put(KEY_DEALER, dealer);
            values.put(KEY_CUSTOMER, customer);
            values.put(KEY_COMPANY, company);
            values.put(KEY_CLOSER_RESON, closer_reson);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_RELT_TO, relt_to);
            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PAY_FREELANCER, freelance);
            values.put(KEY_PAY_COM, com);
            values.put(KEY_PAY_DEA, dea);
            values.put(KEY_FOC_AMT, focamt);

            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_CLOSE_COMPLAINT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert complaint image  **********************************************/

    public void insertComplaintImage(

            String cmpno,
            String posnr,
            String category,
            String image


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_IMAGE, image);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CR_DATE, new CustomUtility().getCurrentDate());

            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_IMAGE, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert complaint distance **********************************************/

    public void insertComplaintDistance(
            String cmpno,
            String pernr,
            String lat_long
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_LAT_LONG, lat_long);
            values.put(KEY_CR_DATE, new CustomUtility().getCurrentDate());
            values.put(KEY_CR_TIME, new CustomUtility().getCurrentTime());
            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_DISTANCE, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert frwd app data for offline record **********************************************/

    public void insertfrwdappcmp(
                               String awaitpernr,
                               String cmpno,
                               String posnr,
                               String category,
                               String dealer,
                               String customer,
                               String company,
                               String freelance,
                               String dea,
                               String com,
                               String defect,
                               String relt_to,
                               String focamt,
                               String cr_date,
                               String cr_time,
                               String latitude,
                               String longitude,
                               String pendpernr,
                               String awaitrmrk) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_AWT_PERNR, awaitpernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_DEALER, dealer);
            values.put(KEY_CUSTOMER, customer);
            values.put(KEY_COMPANY, company);
            values.put(KEY_PAY_FREELANCER, freelance);
            values.put(KEY_PAY_DEA, dea);
            values.put(KEY_PAY_COM, com);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_RELT_TO, relt_to);
            values.put(KEY_FOC_AMT, focamt);
            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PEND_PERNR, pendpernr);
            values.put(KEY_AWT_APR_RMK, awaitrmrk);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_FRWD_APP_CMP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert pend app data for offline record **********************************************/

    public void insertpendappcmp(String cmpno, String status, String remark,
                                 String pendpernr) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pendpernr);
            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_STATUS, status);
            values.put(KEY_REMARK, remark);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_PEND_APP_CMP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert DSR entry data for offline record **********************************************/

    public void insertDsrEntry(String pernr,
                               String budat,
                               String time,
                               String help_name,
                               String agenda,
                               String outcomes,
                               String latitude,
                               String longitude) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);

            values.put(KEY_TIME_IN, time);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_AGENDA, agenda);
            values.put(KEY_COMMENT, outcomes);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_SYNC, "NOT");


            // Insert Row
            long i = db.insert(TABLE_DSR_ENTRY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert no order data for offline record **********************************************/

    public void insertNoOrderData(String pernr,
                                  String budat,
                                  String time,
                                  String help_name,
                                  String comment,
                                  String latitude,
                                  String longitude,
                                  String phone_number,
                                  String route_code,
                                  String no_order_image) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);

            values.put(KEY_TIME_IN, time);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_COMMENT, comment);

            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(IN_IMAGE, no_order_image);

            values.put(KEY_SYNC, "NOT");
            // Insert Row
            long i = db.insert(TABLE_NO_ORDER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert check in chek out data for offline record **********************************************/

    public void insertCheckInOut(String pernr,
                                 String date_in,
                                 String time_in,
                                 String check_in_latitude,
                                 String check_in_longitude,
                                 String route_code,
                                 String phone_number
    ) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, pernr);
            values.put(KEY_DATE_IN, date_in);
            values.put(KEY_TIME_IN, time_in);
            values.put(KEY_CHECK_IN_LATITUDE, check_in_latitude);
            values.put(KEY_CHECK_IN_LONGITUDE, check_in_longitude);
            values.put(KEY_ROUTE_CODE, route_code);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");

            // Insert Row
            long i = db.insert(TABLE_CHECK_IN_OUT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void updateServiceCenterSurvey(
            String kunnr,
            String lat_long
    ) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;


        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_LAT_LONG, lat_long);


            String updateQuery = "UPDATE "
                    + TABLE_SERVICE_CENTER +
                    " SET lat_long = '" + lat_long +

                    "' WHERE kunnr = '" + kunnr + "'";

            c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();
            c.moveToFirst();
//            c.close();
//            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            //  db.setTransactionSuccessful();
            //    c.moveToFirst();
            db.endTransaction();
            if (c != null) {
                c.close();
            }
            db.close();


        }
    }


    /****************************** update check out chek out data for offline record **********************************************/

    public void updateCheckInOut(String pernr,
                                 String date_out,
                                 String time_out,
                                 String check_out_latitude,
                                 String check_out_longitude,
                                 String phone_number,
                                 String comment,
                                 String help_name,
                                 String audio_record,
                                 String customer_name,
                                 String city_name,
                                 String follow_up_date,
                                 String conversion_status,
                                 String srv_cnt_brd_img,
                                 String srv_cnt_trn_ltr_img,
                                 String certificate_img,
                                 String slfy_serv_per,
                                 String spr_prt_stk_img,
                                 String prd_trn_img,
                                 String other_img
    ) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            String where = " ";

            values.put(KEY_PERNR, pernr);
            values.put(KEY_DATE_OUT, date_out);
            values.put(KEY_TIME_OUT, time_out);
            values.put(KEY_CHECK_OUT_LATITUDE, check_out_latitude);
            values.put(KEY_CHECK_OUT_LONGITUDE, check_out_longitude);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_HELP_NAME, help_name);
            values.put(KEY_AUDIO_RECORD, audio_record);
            values.put(KEY_COMMENT, comment);
            values.put(KEY_PARTNER_NAME, customer_name);
            values.put(KEY_DISTRICT_TXT, city_name);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);
            values.put(KEY_CONVERSION_STATUS, conversion_status);
            values.put(KEY_SRV_CNT_BORD_IMG, srv_cnt_brd_img);
            values.put(KEY_SRV_CNT_TRN_LTR_IMG, srv_cnt_trn_ltr_img);
            values.put(KEY_CERTIFICATE_IMG, certificate_img);
            values.put(KEY_SLFY_SERV_PER, slfy_serv_per);
            values.put(KEY_SPR_PRT_STK_IMG, spr_prt_stk_img);
            values.put(KEY_PRD_TRN_IMG, prd_trn_img);
            values.put(KEY_OTHR_IMG, other_img);
            values.put(KEY_SYNC, "NOT");
            values.put(KEY_CHAT_APP, "NOT");

            where  = KEY_PERNR + "='" + pernr + "'" + " AND " +
                    KEY_DATE_IN + "='" + date_out + "'" + " AND " +
                    KEY_PHONE_NUMBER + "='" + phone_number + "'";

            long t = db.update(TABLE_CHECK_IN_OUT, values, where, null);

     /*       String updateQuery = "UPDATE "
                    + TABLE_CHECK_IN_OUT +
                    " SET date_out = '" + date_out +
                    "', time_out = '" + time_out +

                    "', check_out_latitude = '" + check_out_latitude +
                    "', check_out_longitude = '" + check_out_longitude +
                    "', comment = '" + comment +
                    "', help_name = '" + help_name +
                    "', audio_record = '" + audio_record +

                    "', sync = '" + "NOT" +

                    "', partner_name = '" + customer_name +
                    "', district_txt = '" + city_name +

                    "', follow_up_date = '" + follow_up_date +

                    "', conversion_status = '" + conversion_status +
                    "', srv_cnt_bord_img = '" + srv_cnt_brd_img +
                    "', srv_cnt_img = '" + srv_cnt_trn_ltr_img +
                    "', certificate_img = '" + certificate_img +
                    "', slfy_serv_per = '" + slfy_serv_per +
                    "', spr_prt_stk_img = '" + spr_prt_stk_img +
                    "', prd_trn_img = '" + prd_trn_img +
                    "', othr_img = '" + other_img +


                    "', chat_app = '" + "NOT" +


                    "' WHERE pernr = '" + pernr +
                    "' AND date_in = '" + date_out +
                    "' AND phone_number = '" + phone_number + "'";*/

           // c = db.rawQuery(updateQuery, null);


            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();


        }
    }


    /***********************************  insert mark attendance ******************************/
    public long insertMarkAttendance(AttendanceBean attendanceBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        long t = 0;

        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(TYPE, attendanceBean.TYPE);
            contentValues.put(PERNR, attendanceBean.PERNR);
            contentValues.put(BEGDA, attendanceBean.BEGDA);
            contentValues.put(SERVER_DATE_IN, attendanceBean.SERVER_DATE_IN);
            contentValues.put(SERVER_TIME_IN, attendanceBean.SERVER_TIME_IN);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            contentValues.put(IN_ADDRESS, attendanceBean.IN_ADDRESS);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            contentValues.put(IN_TIME, attendanceBean.IN_TIME);
            contentValues.put(OUT_TIME, attendanceBean.OUT_TIME);
            contentValues.put(WORKING_HOURS, attendanceBean.WORKING_HOURS);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            contentValues.put(IN_LAT_LONG, attendanceBean.IN_LAT_LONG);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            contentValues.put(IN_FILE_NAME, attendanceBean.IN_FILE_NAME);
            contentValues.put(IN_FILE_LENGTH, attendanceBean.IN_FILE_LENGTH);
            contentValues.put(IN_FILE_VALUE, attendanceBean.IN_FILE_VALUE);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);
            contentValues.put(IN_STATUS, attendanceBean.IN_STATUS);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);

            contentValues.put(IN_IMAGE, attendanceBean.IN_IMAGE);

            contentValues.put(KEY_SYNC, "NOT");

            t = db.insert(TABLE_MARK_ATTENDANCE, null, contentValues);

            //System.out.println(t);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            // db.endTransaction();

            db.close();
        }

        return t;
    }


    /***************** update unsync data *********************













     /***************** end update unsync data ********************/


    public void insertUnsyncDataChatApp(String table_name, String key_id) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;


        try {
            values = new ContentValues();


            values.put(KEY_TABLE_NAME, table_name);
            values.put(KEY_SYNC_ID, key_id);
            long i = db.insert(TABLE_DATA_SYNC_CHAT_APP, null, values);


            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }

    }


    public void updateAudioChatApp(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateDataSendToChatApp(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            Log.d("sync_id", "" + key_id + "" + table_name);


            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateComplaintAudio(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            db = this.getWritableDatabase();
            contentValues.put(KEY_CHAT_APP, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    public void updateUnsyncData(String table_name, String key_id) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();

        try {

            db = this.getWritableDatabase();
            contentValues.put(KEY_SYNC, "YES");
            long t = db.update(table_name, contentValues, KEY_ID + "='" + key_id + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

    }


    /**************************update mark atendnace ********************************************/

    public boolean updateMarkAttendance(AttendanceBean attendanceBean) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        try {

            db = this.getWritableDatabase();

            // contentValues.put(TYPE, attendanceBean.TYPE);
            contentValues.put(PERNR, attendanceBean.PERNR);

            // contentValues.put(BEGDA, attendanceBean.BEGDA);

            //  contentValues.put(SERVER_DATE_IN, attendanceBean.SERVER_DATE_IN);
            //  contentValues.put(SERVER_TIME_IN, attendanceBean.SERVER_TIME_IN);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            // contentValues.put(IN_ADDRESS, attendanceBean.IN_ADDRESS);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            // contentValues.put(IN_TIME, attendanceBean.IN_TIME);
            contentValues.put(OUT_TIME, attendanceBean.SERVER_TIME_OUT);
            //  contentValues.put(WORKING_HOURS, attendanceBean.WORKING_HOURS);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            // contentValues.put(IN_LAT_LONG, attendanceBean.IN_LAT_LONG);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            // contentValues.put(IN_FILE_NAME, attendanceBean.IN_FILE_NAME);
            // contentValues.put(IN_FILE_LENGTH, attendanceBean.IN_FILE_LENGTH);
            // contentValues.put(IN_FILE_VALUE, attendanceBean.IN_FILE_VALUE);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);

            contentValues.put(OUT_IMAGE, attendanceBean.OUT_IMAGE);

            // contentValues.put(IN_STATUS, attendanceBean.IN_STATUS);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);

            contentValues.put(KEY_SYNC, "NOT");


            //   long t = db.update(AttendanceDBFields.ATTENDANCE_TABLE_NAME, null, contentValues);
            //  long t = db.update(TABLE_MARK_ATTENDANCE, contentValues, BEGDA + "='" + attendanceBean.BEGDA+"'", null);

            long t = db.update(TABLE_MARK_ATTENDANCE, contentValues, BEGDA + "='" + attendanceBean.SERVER_DATE_OUT + "'" + " AND " +
                    PERNR + "='" + LoginBean.getUseid() + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            db.close();
        }

        return true;
    }


    /************ select mark attendance****************************************************/






    /*   get all attendance */




    /*********************************** select check in check out data ****************************/




    /*********************************** select new added customer data****************************/


    public void createProduct(BeanProduct beanproduct) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MATNR, beanproduct.getMatnr());
        values.put(KEY_KUNNR, beanproduct.getKunnr());
        values.put(KEY_VKORG, beanproduct.getVkorg());
        values.put(KEY_VTWEG, beanproduct.getVtweg());
        values.put(KEY_EXTWG, beanproduct.getExtwg());
        values.put(KEY_MAKTX, beanproduct.getMaktx());
        values.put(KEY_KBETR, beanproduct.getKbetr());
        values.put(KEY_KONWA, beanproduct.getKonwa());
        values.put(KEY_MTART, beanproduct.getMtart());


        // insert row
        Long result = db.insert(TABLE_ADHOC, null, values);


    }


    public void createProductFinal(BeanProductFinal beanproductfinal) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

//        Log.d("phone", "--"+beanproductfinal.getPhone_number()+"--"+ beanproductfinal.getCustomer_name() +"--"
//                + beanproductfinal.getPerson()+"--"+beanproductfinal.getCr_date()+"--"+beanproductfinal.getCr_time());

        values.put(KEY_PHONE_NUMBER, beanproductfinal.getPhone_number());
        values.put(KEY_MATNR, beanproductfinal.getMatnr());
        values.put(KEY_EXTWG, beanproductfinal.getExtwg());
        values.put(KEY_MAKTX, beanproductfinal.getMaktx());
        values.put(KEY_KBETR, beanproductfinal.getKbetr());
        values.put(KEY_MENGE, beanproductfinal.getMenge());
        values.put(KEY_TOT_KBETR, beanproductfinal.getTot_kbetr());
        values.put(KEY_CUSTOMER_NAME, beanproductfinal.getCustomer_name());
        values.put(KEY_PERSON, beanproductfinal.getPerson());
        values.put(KEY_CR_DATE, beanproductfinal.getCr_date());
        values.put(KEY_CR_TIME, beanproductfinal.getCr_time());
        values.put(KEY_LATITUDE, beanproductfinal.getLatitude());
        values.put(KEY_LONGITUDE, beanproductfinal.getLongitude());

        values.put(KEY_ROUTE_CODE, beanproductfinal.getRoute_code());
        values.put(KEY_PARTNER, beanproductfinal.getPartner_type());

        values.put(KEY_SYNC, "NOT");

        // insert row
        db.insert(TABLE_ADHOC_FINAL, null, values);

    }





    /******************************* Insert Material Analysis **********************************************/
    public void insertMaterialAnalysis(
            String matnr,
            String maktx,
            String extwg,
            String plant,
            String indicator,
            String delivery_time,
            String kbetr,
            String konwa) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_EXTWG, extwg);
            values.put(KEY_PLANT, plant);
            values.put(KEY_INDICATOR, indicator);
            values.put(KEY_DELIVERY_TIME, delivery_time);
            values.put(KEY_KBETR, kbetr);
            values.put(KEY_KONWA, konwa);


            // Insert Row
            long i = db.insert(TABLE_MATERIAL_ANALYSIS, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert Video gallery  ******************************/

    public void insertVideoGallery(
            String type,
            String name,
            String link
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_VIDEO_TYPE, type);
            values.put(KEY_VIDEO_NAME, name);
            values.put(KEY_VIDEO_LINK, link);


            // Insert Row
            long i = db.insert(TABLE_VIDEO_GALLERY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert chat app group  ******************************/

    public void insertChatAppGroup(
            String pernr,
            String username,
            String password,
            String api,
            String group_id

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;


        //   Log.d("chat_app_inst2",   username +"--" + group_id +"--"+ api)    ;
        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);
            values.put(KEY_API, api);
            values.put(KEY_GROUP_ID, group_id);

            // Log.d("chat_app_inst3",  username +"--" + group_id +"--"+ api)    ;


            // Insert Row
            long i = db.insert(TABLE_CHAT_APP, null, values);


            // Log.d("chat_app_inst",""+i +"" + username +"--" + group_id +"--"+ api)    ;

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert customer complaint  ******************************/

    public void insertZCMPLNHDR(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String mob_no1,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit,
            String epc,
            String penday,
            String pen_res,
            String fdate,
            String awaitaprpernr,
            String awaitaprpernrnm,
            String pendaprpernr,
            String pendaprpernrnm,
            String awaitapproval,
            String pendapproval,
            String awaitaprremark,
            String pendaprremark


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_ALT_MOB_NO, mob_no1);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);
            values.put(KEY_EPC, epc);
            values.put(KEY_PEN_DAY, penday);
            values.put(KEY_PEN_RES, pen_res);
            values.put(KEY_FLW_DT, fdate);
            values.put(KEY_AWT_PERNR, awaitaprpernr);
            values.put(KEY_AWT_PERNR_NM, awaitaprpernrnm);
            values.put(KEY_PEND_PERNR, pendaprpernr);
            values.put(KEY_PEND_PERNR_NM, pendaprpernrnm);
            values.put(KEY_AWT_APR, awaitapproval);
            values.put(KEY_PEND_APR, pendapproval);
            values.put(KEY_AWT_APR_RMK, awaitaprremark);
            values.put(KEY_PEND_APR_RMK, pendaprremark);


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDR, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertSerial_Number_ZCMPLNHDR(
            String cmpno,
            String cmpdt,
            String address,
            String mob_no,
            String customer_name,
            String distributor_code,
            String distributor_name,
            String pernr,
            String ename,
            String category,
            String cmpln_status,
            String edit


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_CMPDT, cmpdt);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_MOB_NO, mob_no);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_DISTRIBUTOR_CODE, distributor_code);
            values.put(KEY_DISTRIBUTOR_NAME, distributor_name);
            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_CATEGORY, category);
            values.put(KEY_CMPLN_STATUS, cmpln_status);
            values.put(KEY_EDIT, edit);


            // Insert Row
            long i = db.insert(TABLE_SERAIL_NUMBER_ZCMPLNHDR, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert customer complaint detail  ******************************/

    public void insertZCMPLNDTL(
            String cmpno,
            String posnr,
            String matnr,
            String maktx,
            String reason,
            String warranty,
            String sernr,
            String closer_reason,
            String defect,
            String extwg,
            String payment_by,
            String customer,
            String dealer,
            String company,
            String re_comp,
            String re_del,
            String pay_freelancer,
            String history,
            String billno,
            String billdt,
            String insutxt,
            String warrncond,
            String cmpln_relt_to,
            String wardat


    ) {


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_REASON, reason);
            values.put(KEY_WARRANTY, warranty);
            values.put(KEY_SERNR, sernr);
            values.put(KEY_CLOSER_RESON, closer_reason);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_EXTWG, extwg);
            values.put(KEY_HISTORY, history);
            values.put(KEY_PAY_FREELANCER, pay_freelancer);
            values.put(KEY_BILL_DATE, billdt);
            values.put(KEY_BILL_NO, billno);
            values.put(KEY_INSU_TXT, insutxt);
            values.put(KEY_WARR_COND, warrncond);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_WAR_DAT, wardat);
            values.put(KEY_RELT_TO, cmpln_relt_to);


            if (payment_by.equalsIgnoreCase("payment by")) {

                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);

            }

            if (payment_by.equalsIgnoreCase("Return By")) {
                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
                values.put(KEY_PAY_COM, re_comp);
                values.put(KEY_PAY_DEA, re_del);
            }


            // Insert Row
            long i = db.insert(TABLE_ZCMPLNHDTL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    public void insertSerial_Number_ZCMPLNDTL(
            String cmpno,
            String posnr,
            String matnr,
            String maktx,
            String reason,
            String warranty,
            String sernr,
            String closer_reason,
            String defect,
            String extwg,
            String payment_by,
            String customer,
            String dealer,
            String company,
            String re_comp,
            String re_del


    ) {


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_POSNR, posnr);
            values.put(KEY_MATNR, matnr);
            values.put(KEY_MAKTX, maktx);
            values.put(KEY_REASON, reason);
            values.put(KEY_WARRANTY, warranty);
            values.put(KEY_SERNR, sernr);
            values.put(KEY_CLOSER_RESON, closer_reason);
            values.put(KEY_DEFECT, defect);
            values.put(KEY_EXTWG, extwg);


            if (payment_by.equalsIgnoreCase("payment by")) {

                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
            }

            if (payment_by.equalsIgnoreCase("Return By")) {
                values.put(KEY_PAYMENT_BY, payment_by);
                values.put(KEY_CUSTOMER, customer);
                values.put(KEY_DEALER, dealer);
                values.put(KEY_COMPANY, company);
            }


            // Insert Row
            long i = db.insert(TABLE_SERAIL_NUMBER_ZCMPLNHDTL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert customer complaint category  ******************************/

    public void insertZCMPLN_CATEGORY(
            String category
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, category);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CATEGORY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert customer complaint defect type  ******************************/

    public void insertZCMPLN_DEFECT_TYPE(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_DEFECT, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_DEFECT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }

    /****************************** insert customer complaint related to  ******************************/

    public void insertZCMPLN_RELT_TO(
            String defect
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_RELT_TO, defect);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_RELT_TO, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert customer complaint defect type  ******************************/

    public void insertZCMPLN_CLOSER(
            String extwg,
            String reason

    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_EXTWG, extwg);
            values.put(KEY_CLOSER_RESON, reason);

            // Insert Row
            long i = db.insert(TABLE_ZCMPLN_CLOSER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();

        }
    }


    /****************************** insert service center  *****************************************/

    public void insertServiceCenter(
            String pernr,
            String ename,
            String kunnr,
            String customer_name,
            String land,
            String land_txt,
            String regio,
            String state_txt,
            String city,
            String district_txt,
            String taluka,
            String taluka_txt,
            String mobno,
            String telf2,
            String contact_person,
            String address,
            String pincode,
            String email,
            String lat_long


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);
            values.put(KEY_KUNNR, kunnr);
            values.put(KEY_CUSTOMER_NAME, customer_name);
            values.put(KEY_LAND1, land);
            values.put(KEY_LAND_TXT, land_txt);
            values.put(KEY_STATE_CODE, regio);
            values.put(KEY_STATE_TXT, state_txt);

            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_DISTRICT_TXT, district_txt);

            values.put(KEY_TALUKA_CODE, taluka);
            values.put(KEY_TALUKA_TXT, taluka_txt);

            values.put(KEY_MOB_NO, mobno);
            values.put(KEY_TEL_NUMBER, telf2);
            values.put(KEY_CONTACT_PERSON, contact_person);
            values.put(KEY_ADDRESS, address);
            values.put(KEY_PINCODE, pincode);
            values.put(KEY_EMAIL, email);
            values.put(KEY_LAT_LONG, lat_long);


            // Insert Row
            long i = db.insert(TABLE_SERVICE_CENTER, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert country  *****************************************/

    public void insertCountry(
            String land1,
            String land_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_LAND_TXT, land_txt);
            // Insert Row
            long i = db.insert(TABLE_COUNTRY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /****************************** insert state  *****************************************/

    public void insertState(
            String land1,
            String state,
            String state_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_STATE_TXT, state_txt);
            // Insert Row
            long i = db.insert(TABLE_STATE, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert city   *****************************************/

    public void insertCity(
            String land1,
            String state,
            String city,
            String city_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_DISTRICT_TXT, city_txt);
            // Insert Row
            long i = db.insert(TABLE_CITY, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert tehsil   *****************************************/

    public void insertTehsil(
            String land1,
            String state,
            String city,
            String tehsil,
            String tehsil_txt
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LAND1, land1);
            values.put(KEY_STATE_CODE, state);
            values.put(KEY_DISTRICT_CODE, city);
            values.put(KEY_TALUKA_CODE, tehsil);
            values.put(KEY_TALUKA_TXT, tehsil_txt);
            // Insert Row
            long i = db.insert(TABLE_TEHSIL, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /****************************** insert Complaint inprocess data for offline record **********************************************/

    public void insertComplaintAction(String pernr,
                                      String ename,
                                      String cmpno,
                                      String follow_up_date,
                                      String reason,
                                      String cr_date,
                                      String cr_time,
                                      String status


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PERNR, pernr);
            values.put(KEY_ENAME, ename);

            values.put(KEY_CMPNO, cmpno);
            values.put(KEY_FOLLOW_UP_DATE, follow_up_date);

            values.put(KEY_CR_DATE, cr_date);
            values.put(KEY_CR_TIME, cr_time);
            values.put(KEY_REASON, reason);
            values.put(KEY_CMPLN_STATUS, status);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_ACTION, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }

    /****************************** insert Pending Reason **********************************************/

    public void insertpendingreason(String pen_rea_no,
                                    String name


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_PEND_NO, pen_rea_no);
            values.put(KEY_NAME, name);

            // Insert Row
            long i = db.insert(TABLE_PENDING_REASON, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    /****************************** insert Complaint Image name **********************************************/

    public void insertComplaintImageName(String catgry,
                                         String item,
                                         String name


    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();


            values.put(KEY_CATEGORY, catgry);
            values.put(KEY_ITEM, item);
            values.put(KEY_IMAGE, name);

            // Insert Row
            long i = db.insert(TABLE_COMPLAINT_IMAGE_NAME, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }



    //********************************* get complaint distaince
    public boolean getTableComplaintDistance(String cmp_no) {

        String selectQuery = null;
        boolean complaint_no = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cmp_distance_cursor = null;


        try {


            selectQuery = "SELECT * FROM " + TABLE_COMPLAINT_DISTANCE

                    + " WHERE " + KEY_CMPNO + " = '" + cmp_no + "'";
//


            cmp_distance_cursor = db.rawQuery(selectQuery, null);

//            Log.d("cmp_distance",""+cmp_distance_cursor.getCount());

            if (cmp_distance_cursor.getCount() > 0) {

                while (cmp_distance_cursor.moveToNext()) {

                    complaint_no = true;


                }
                // db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            //   db.endTransaction();
            // End the transaction.
            if (cmp_distance_cursor != null) {
                cmp_distance_cursor.close();
            }

//            db.endTransaction();
            db.close();
            // Close database
        }

        return complaint_no;
    }


    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();

        String Query = "SELECT * FROM " + tablename + " WHERE " + field + " = '" + fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    cursor.moveToNext();
                }
            }
        }

        cursor.close();
        return true;
    }


    public boolean isEmpty(String TableName) {

        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database, TableName);

        if (NoOfRows == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteStateSearchHelpData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_STATE_SEARCH)) {
            db.delete(TABLE_STATE_SEARCH, null, null);
        }
    }





    public void insertStateData(String country, String country_text, String state, String state_text, String district, String district_text, String tehsil, String tehsil_text) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_COUNTRY, country);
            values.put(KEY_COUNTRY_TEXT, country_text);
            values.put(KEY_STATE, state);
            values.put(KEY_STATE_TEXT, state_text);
            values.put(KEY_DISTRICT, district);
            values.put(KEY_DISTRICT_TEXT, district_text);
            values.put(KEY_TEHSIL, tehsil);
            values.put(KEY_TEHSIL_TEXT, tehsil_text);


            // Insert Row
            long i = db.insert(TABLE_STATE_SEARCH, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getStateDistrictList(String key, String text) {

        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();


        list.clear();

        db.beginTransaction();
        try {

            String selectQuery = null;

            switch (key) {

                case KEY_STATE_TEXT:
                    selectQuery = "SELECT  DISTINCT " + KEY_STATE_TEXT + " FROM " + TABLE_STATE_SEARCH;
                    list.add("Select State");
                    break;
                case KEY_DISTRICT_TEXT:

                    selectQuery = "SELECT  DISTINCT " + KEY_DISTRICT_TEXT + " FROM " + TABLE_STATE_SEARCH
                            + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";
                    list.add("Select District");
                    break;

            }

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        switch (key) {

                            case KEY_STATE_TEXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_STATE_TEXT)));
                                break;
                            case KEY_DISTRICT_TEXT:
                                list.add(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT_TEXT)));
                                break;
                        }
                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }

    @SuppressLint("Range")
    public String getStateDistrictValue(String key, String text) {
        String result = null;

        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            switch (key) {

                case KEY_STATE:
                    selectQuery = "SELECT  * FROM " + TABLE_STATE_SEARCH + " WHERE " + KEY_STATE_TEXT + " = '" + text + "'";
                    break;
                case KEY_DISTRICT:
                    selectQuery = "SELECT  * FROM " + TABLE_STATE_SEARCH + " WHERE " + KEY_DISTRICT_TEXT + " = '" + text + "'";
                    break;

            }
            c = db.rawQuery(selectQuery, null);

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    switch (key) {

                        case KEY_STATE:
                            result = c.getString(c.getColumnIndex(KEY_STATE));
                            break;
                        case KEY_DISTRICT:
                            result = c.getString(c.getColumnIndex(KEY_DISTRICT));
                            break;

                    }
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<SubordinateBean> getSubordinateList() {

        ArrayList<SubordinateBean> subordinateBeanList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_SUBORDINATE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        SubordinateBean subordinateBean = new SubordinateBean();
                        subordinateBean.setAadharNo(cursor.getString(cursor.getColumnIndex(KEY_AADHAR_CARD)));
                        subordinateBean.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                        subordinateBean.setMobileNo(cursor.getString(cursor.getColumnIndex(KEY_MOB_NO)));
                        subordinateBean.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
                        subordinateBean.setDistrict(cursor.getString(cursor.getColumnIndex(KEY_DISTRICT)));
                        subordinateBean.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
                        subordinateBean.setFromDate(cursor.getString(cursor.getColumnIndex(KEY_FR_DATE)));
                        subordinateBean.setToDate(cursor.getString(cursor.getColumnIndex(KEY_TO_DATE)));

                        subordinateBeanList.add(subordinateBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return subordinateBeanList;
    }

    @SuppressLint("Range")
    public ArrayList<SubordinateAssginComplainBean> getSubordinateAssginComplainList() {

        ArrayList<SubordinateAssginComplainBean> subordinateBeanList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSGIN_COMPLAIN_SUBORDINATE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        SubordinateAssginComplainBean subordinateBean = new SubordinateAssginComplainBean();
                        subordinateBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                        subordinateBean.setDelname(cursor.getString(cursor.getColumnIndex(KEY_DEALER_NAME)));
                        subordinateBean.setCstname(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                        subordinateBean.setCaddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        subordinateBean.setEngg_name(cursor.getString(cursor.getColumnIndex(KEY_ENGG_NAME)));
                        subordinateBean.setCmblno(cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NO)));
                        subordinateBean.setMatnr(cursor.getString(cursor.getColumnIndex(KEY_MATNR)));
                        subordinateBean.setWarranty(cursor.getString(cursor.getColumnIndex(KEY_WARRANTY)));
                        subordinateBean.setMaktx(cursor.getString(cursor.getColumnIndex(KEY_MAKTX)));
                        subordinateBean.setSernr(cursor.getString(cursor.getColumnIndex(KEY_SERNR)));
                        subordinateBean.setReason(cursor.getString(cursor.getColumnIndex(KEY_REASON)));
                        subordinateBean.setW_waranty(cursor.getString(cursor.getColumnIndex(KEY_WARRANTY_DURATION)));
                        subordinateBean.setCmpdt(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                        subordinateBeanList.add(subordinateBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return subordinateBeanList;
    }

    @SuppressLint("Range")
    public ArrayList<SubordinateAssginComplainBean> getSubordinateAssginComplainNo(String compNo) {

        ArrayList<SubordinateAssginComplainBean> subordinateBeanList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_ASSGIN_COMPLAIN_SUBORDINATE + " WHERE " + KEY_CMPNO+ " = '" + compNo + "'";
            Log.e("selectQuery===>", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        SubordinateAssginComplainBean subordinateBean = new SubordinateAssginComplainBean();
                        subordinateBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                        subordinateBean.setDelname(cursor.getString(cursor.getColumnIndex(KEY_DEALER_NAME)));
                        subordinateBean.setCstname(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                        subordinateBean.setCaddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                        subordinateBean.setEngg_name(cursor.getString(cursor.getColumnIndex(KEY_ENGG_NAME)));
                        subordinateBean.setCmblno(cursor.getString(cursor.getColumnIndex(KEY_MOBILE_NO)));
                        subordinateBean.setMatnr(cursor.getString(cursor.getColumnIndex(KEY_MATNR)));
                        subordinateBean.setWarranty(cursor.getString(cursor.getColumnIndex(KEY_WARRANTY)));
                        subordinateBean.setMaktx(cursor.getString(cursor.getColumnIndex(KEY_MAKTX)));
                        subordinateBean.setSernr(cursor.getString(cursor.getColumnIndex(KEY_SERNR)));
                        subordinateBean.setReason(cursor.getString(cursor.getColumnIndex(KEY_REASON)));
                        subordinateBean.setW_waranty(cursor.getString(cursor.getColumnIndex(KEY_WARRANTY_DURATION)));
                        subordinateBean.setCmpdt(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                        subordinateBeanList.add(subordinateBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return subordinateBeanList;
    }

    public void updateSuboridnateData(String key, SubordinateBean subordinateBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;


        try {
            values = new ContentValues();
            String where = " ";
            switch (key) {
                case KEY_INSERT:

                    values.put(KEY_MOB_NO, subordinateBean.getMobileNo());
                    values.put(KEY_AADHAR_CARD, subordinateBean.getAadharNo());
                    values.put(KEY_STATE, subordinateBean.getState());
                    values.put(KEY_DISTRICT, subordinateBean.getDistrict());
                    values.put(KEY_NAME, subordinateBean.getName());
                    values.put(KEY_FR_DATE, subordinateBean.getFromDate());
                    values.put(KEY_TO_DATE, subordinateBean.getToDate());
                    values.put(KEY_PASSWORD, subordinateBean.getPassword());

                    i = db.insert(TABLE_SUBORDINATE, null, values);
                    break;

                case KEY_UPDATE:

                    values.put(KEY_AADHAR_CARD, subordinateBean.getAadharNo());
                    values.put(KEY_STATE, subordinateBean.getState());
                    values.put(KEY_DISTRICT, subordinateBean.getDistrict());
                    values.put(KEY_NAME, subordinateBean.getName());
                    values.put(KEY_FR_DATE, subordinateBean.getFromDate());
                    values.put(KEY_TO_DATE, subordinateBean.getToDate());
                    values.put(KEY_PASSWORD, subordinateBean.getPassword());

                    where = KEY_MOB_NO + "='" + subordinateBean.getMobileNo() + "'";

                    i = db.update(TABLE_SUBORDINATE, values, where, null);

            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateAssginComplainData(String key, SubordinateAssginComplainBean subordinateBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            String where = " ";
            switch (key) {
                case KEY_INSERT:

                    values.put(KEY_CMPNO, subordinateBean.getCmpno());
                    values.put(KEY_DEALER_NAME, subordinateBean.getDelname());
                    values.put(KEY_CUSTOMER_NAME, subordinateBean.getCstname());
                    values.put(KEY_ENGG_NAME, subordinateBean.getEngg_name());
                    values.put(KEY_MOBILE_NO, subordinateBean.getCmblno());
                    values.put(KEY_ADDRESS, subordinateBean.getCaddress());
                    values.put(KEY_MATNR, subordinateBean.getMatnr());
                    values.put(KEY_WARRANTY, subordinateBean.getWarranty());
                    values.put(KEY_MAKTX, subordinateBean.getMaktx());
                    values.put(KEY_SERNR, subordinateBean.getSernr());
                    values.put(KEY_REASON, subordinateBean.getReason());
                    values.put(KEY_WARRANTY_DURATION, subordinateBean.getW_waranty());
                    values.put(KEY_DATE, subordinateBean.getCmpdt());

                     db.insert(TABLE_ASSGIN_COMPLAIN_SUBORDINATE, null, values);
                    break;

                case KEY_UPDATE:

                    values.put(KEY_CMPNO, subordinateBean.getCmpno());
                    values.put(KEY_DEALER_NAME, subordinateBean.getDelname());
                    values.put(KEY_CUSTOMER_NAME, subordinateBean.getCstname());
                    values.put(KEY_ENGG_NAME, subordinateBean.getEngg_name());
                    values.put(KEY_MOBILE_NO, subordinateBean.getCmblno());
                    values.put(KEY_ADDRESS, subordinateBean.getCaddress());
                    values.put(KEY_MATNR, subordinateBean.getMatnr());
                    values.put(KEY_WARRANTY, subordinateBean.getWarranty());
                    values.put(KEY_MAKTX, subordinateBean.getMaktx());
                    values.put(KEY_SERNR, subordinateBean.getSernr());
                    values.put(KEY_REASON, subordinateBean.getReason());
                    values.put(KEY_WARRANTY_DURATION, subordinateBean.getW_waranty());
                    values.put(KEY_DATE, subordinateBean.getCmpdt());

                    where = KEY_MOBILE_NO + "='" + subordinateBean.getCmpno() + "'";

                    i = db.update(TABLE_ASSGIN_COMPLAIN_SUBORDINATE, values, where, null);

            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**********************  insert Employee GPS Activity ************************************/
    public void insertEmployeeGPSActivity(
            String pernr,
            String budat,
            String time,
            String event,
            String latitude,
            String longitude,
            Context context,
            String phone_number


    ) {


//*************  get mobile tower location *******************************

        String cell_id = "0",
                location_code = "0",
                mobile_country_code = "0",
                mobile_network_code = "0";


        try {

            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

            if (cellLocation != null) {
                cid = cellLocation.getCid();
                lac = cellLocation.getLac();
            }

            String networkOperator = telephonyManager.getNetworkOperator();

            if (!TextUtils.isEmpty(networkOperator)) {
                mcc = Integer.parseInt(networkOperator.substring(0, 3));
                mnc = Integer.parseInt(networkOperator.substring(3));
            }


//        String loc = "cell id: " + String.valueOf(cid) + "location area code:" + String.valueOf(lac) +
//                "mcc: " + String.valueOf(mcc) +    "mnc: " + String.valueOf(mnc);


//            String loc = "ci=: " + String.valueOf(cid) + "lac=:" + String.valueOf(lac) +
//                    "mcc=: " + String.valueOf(mcc) +    "mnc=: " + String.valueOf(mnc);


            cell_id = String.valueOf(cid);
            location_code = String.valueOf(lac);

            mobile_country_code = String.valueOf(mcc);
            mobile_network_code = String.valueOf(mnc);


        } catch (SQLiteException e) {
            e.printStackTrace();
        }

//*************  end mobile tower location *******************************


        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_EVENT, event);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PHONE_NUMBER, phone_number);
            values.put(KEY_SYNC, "NOT");
            values.put(KEY_CELL_ID, cell_id);
            values.put(KEY_LOCATION_CODE, location_code);
            values.put(KEY_MOBILE_COUNTRY_CODE, mobile_country_code);
            values.put(KEY_MOBILE_NETWORK_CODE, mobile_network_code);


            // Insert Row
            long i = db.insert(TABLE_EMPLOYEE_GPS_ACTIVITY, null, values);


            //Toast.makeText(context,String.valueOf( "mayank"+ cell_id +"--"+latitude) , Toast.LENGTH_SHORT).show();


            //Log.d("sync", cell_id +"--"+latitude);


            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE;

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }

    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData(String endat, String endtm) {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + " = '" + endat + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'";

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }

    public void deleteLocalconvenienceDetail(String strdt, String strtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (CustomUtility.doesTableExist(db, TABLE_LOCAL_CONVENIENCE)) {
            db.execSQL("DELETE FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_BEGDA + "='" + strdt + "'" + " AND " + KEY_FROM_TIME + " = '" + strtm + "'");
        }
    }

    public void deleteTableData(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, null, null);
    }

    public void deleteSiteAuditImages() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_IMAGES)) {
            db.delete(TABLE_IMAGES, null, null);
        }
    }


    public List<ImageModel> getAllImages()  {
        ArrayList<ImageModel> siteAuditImages = new ArrayList<ImageModel>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_IMAGES)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_IMAGES, null);
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(3)));
                    imageModel.setBoerwellLiffiting(Boolean.parseBoolean(mcursor.getString(4)));
                    imageModel.setBorewellLowering(Boolean.parseBoolean(mcursor.getString(5)));
                    imageModel.setTransportLoading(Boolean.parseBoolean(mcursor.getString(6)));
                    imageModel.setTransportUnLoading(Boolean.parseBoolean(mcursor.getString(7)));
                    imageModel.setBillNo(mcursor.getString(8));

                    siteAuditImages.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return siteAuditImages;
    }

    public void insertImageRecord(String name, String path, boolean isSelected, String billno, Boolean boerwellLiffiting, Boolean borewellLowering, Boolean transportLoading, Boolean transportUnLoading) {
        SQLiteDatabase  database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGES_NAME, name);
        contentValues.put(KEY_IMAGES_PATH, path);
        contentValues.put(KEY_IMAGE_SELECTED, isSelected);
        contentValues.put(KEY_IMAGES_BILL_NO, billno);
        contentValues.put(KEY_PUMPSET_LIFTING, boerwellLiffiting);
        contentValues.put(KEY_PUMPSET_LOWERING,borewellLowering);
        contentValues.put(KEY_MATERIAL_LOAD,transportLoading);
        contentValues.put(KEY_MATERIAL_UNLOAD,transportUnLoading);

        database.insert(TABLE_IMAGES, null, contentValues);
        database.close();
    }

    public void updateImageRecord(String name, String path, boolean isSelected, String billno, Boolean boerwellLiffiting, Boolean borewellLowering, Boolean transportLoading, Boolean transportUnLoading) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGES_NAME, name);
        values.put(KEY_IMAGES_PATH, path);
        values.put(KEY_IMAGE_SELECTED, isSelected);
        values.put(KEY_IMAGES_BILL_NO, billno);
        values.put(KEY_PUMPSET_LIFTING, boerwellLiffiting);
        values.put(KEY_PUMPSET_LOWERING,borewellLowering);
        values.put(KEY_MATERIAL_LOAD,transportLoading);
        values.put(KEY_MATERIAL_UNLOAD,transportUnLoading);

        // update Row
        db.update(TABLE_IMAGES,values,"imagesName = '"+name+"'",null);
        db.close();
    }


    public List<ImageModel> getAllImagesData() {
        ArrayList<ImageModel> Images = new ArrayList<>();
        SQLiteDatabase  database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_IMAGES)) {
            Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_IMAGES, null);
            ImageModel imageModel;

            if (mcursor.getCount() > 0) {
                for (int i = 0; i < mcursor.getCount(); i++) {
                    mcursor.moveToNext();

                    imageModel = new ImageModel();
                    imageModel.setID(mcursor.getString(0));
                    imageModel.setName(mcursor.getString(1));
                    imageModel.setImagePath(mcursor.getString(2));
                    imageModel.setBillNo(mcursor.getString(3));
                    imageModel.setTransportLoading(Boolean.parseBoolean(mcursor.getString(6)));
                    imageModel.setTransportUnLoading(Boolean.parseBoolean(mcursor.getString(7)));
                    imageModel.setBoerwellLiffiting(Boolean.parseBoolean(mcursor.getString(4)));
                    imageModel.setBorewellLowering(Boolean.parseBoolean(mcursor.getString(5)));
                    imageModel.setImageSelected(Boolean.parseBoolean(mcursor.getString(8)));
                    Images.add(imageModel);
                }
            }
            mcursor.close();
            database.close();
        }
        return Images;
    }

    public void deleteWayPointsDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WayPoints);
    }

    public void deleteWayPointsDetail1(String enddt,String endtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WayPoints + " WHERE " + KEY_ENDDA + "='" + enddt + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'");

    }


    public void insertWayPointsData(WayPoints wayPoints) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        ContentValues values;
        Log.e("wayPoint2",wayPoints.getWayPoints());
        try {
            values = new ContentValues();
            values.put(KEY_PERNR, wayPoints.getPernr());
            values.put(KEY_BEGDA, wayPoints.getBegda());
            values.put(KEY_ENDDA, wayPoints.getEndda());
            values.put(KEY_FROM_TIME, wayPoints.getFrom_time());
            values.put(KEY_TO_TIME, wayPoints.getTo_time());
            values.put(KEY_WayPoints, wayPoints.getWayPoints());
            db.insert(TABLE_WayPoints, null, values);

            db.setTransactionSuccessful();
            Log.e("wayPoint3",wayPoints.getWayPoints());
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateWayPointData(WayPoints wayPoints) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_WayPoints, wayPoints.getWayPoints());

            where = KEY_PERNR + "='" + wayPoints.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + wayPoints.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + wayPoints.getFrom_time() + "'";

            // update Row
            long i = db.update(TABLE_WayPoints, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    public void updateWayPointData1(WayPoints wayPoints) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, wayPoints.getPernr());
            values.put(KEY_BEGDA, wayPoints.getBegda());
            values.put(KEY_ENDDA, wayPoints.getEndda());
            values.put(KEY_FROM_TIME, wayPoints.getFrom_time());
            values.put(KEY_TO_TIME, wayPoints.getTo_time());
            values.put(KEY_WayPoints, wayPoints.getWayPoints());

            where = KEY_PERNR + "='" + wayPoints.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + wayPoints.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + wayPoints.getFrom_time() + "'";

            // update Row
            db.update(TABLE_WayPoints, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    @SuppressLint("Range")
    public WayPoints getWayPointsData(String begda, String from_time) {

        WayPoints wayPoints = new WayPoints();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {


            selectQuery = "SELECT * FROM " + TABLE_WayPoints + " WHERE " + KEY_BEGDA + " = '" + begda + "'" + " AND " + KEY_FROM_TIME + " = '" + from_time + "'";


            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        wayPoints = new WayPoints();
                        wayPoints.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        wayPoints.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        wayPoints.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        wayPoints.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        wayPoints.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        wayPoints.setWayPoints(cursordom.getString(cursordom.getColumnIndex(KEY_WayPoints)));

                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return wayPoints;
    }

    public void deleteLocalconvenienceDetail1(String enddt,String endtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + "='" + enddt + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'");
    }

    @SuppressLint("Range")
    public ArrayList<LocalConvenienceBean> getLocalConveyance() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        ArrayList<LocalConvenienceBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_LOCAL_CONVENIENCE;


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursor.getString(cursor.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursor.getString(cursor.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursor.getString(cursor.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursor.getString(cursor.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursor.getString(cursor.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursor.getString(cursor.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursor.getString(cursor.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursor.getString(cursor.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursor.getString(cursor.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursor.getString(cursor.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));

                        list_document.add(localConvenienceBean);


                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }

    public void updateVistComplainData(String keyUpdate, SubordinateVisitedComplainBean subordinateBean) {
        long i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            String where = " ";
            switch (keyUpdate) {
                case KEY_INSERT:

                    values.put(KEY_CMPNO, subordinateBean.getCmpno());
                    values.put(KEY_DEALER_NAME, subordinateBean.getDelname());
                    values.put(KEY_CUSTOMER_NAME, subordinateBean.getCstname());
                    values.put(KEY_DATE, subordinateBean.getCmpdt());
                    values.put(KEY_MOB_NO,subordinateBean.getMblno1());

                    db.insert(TABLE_VISIT_COMPLAIN_SUBORDINATE, null, values);
                    break;

                case KEY_UPDATE:
                    Log.e("WELCOME","KEY_UPDATE");
                    values.put(KEY_CMPNO, subordinateBean.getCmpno());
                    values.put(KEY_DEALER_NAME, subordinateBean.getDelname());
                    values.put(KEY_CUSTOMER_NAME, subordinateBean.getCstname());
                    values.put(KEY_DATE, subordinateBean.getCmpdt());
                    values.put(KEY_MOB_NO,subordinateBean.getMblno1());

                    where = KEY_CMPNO + "='" + subordinateBean.getCmpno() + "'";

                    i = db.update(TABLE_VISIT_COMPLAIN_SUBORDINATE, values, where, null);

            }

            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @SuppressLint("Range")
    public List<SubordinateVisitedComplainBean> getSubordinateVsitedComplainList()  {

        ArrayList<SubordinateVisitedComplainBean> subordinateBeanList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        try {
            String selectQuery = "";
            selectQuery = "SELECT * FROM " + TABLE_VISIT_COMPLAIN_SUBORDINATE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "&&&" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        SubordinateVisitedComplainBean subordinateBean = new SubordinateVisitedComplainBean();
                        subordinateBean.setCmpno(cursor.getString(cursor.getColumnIndex(KEY_CMPNO)));
                        subordinateBean.setDelname(cursor.getString(cursor.getColumnIndex(KEY_DEALER_NAME)));
                        subordinateBean.setCstname(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                        subordinateBean.setMblno1(cursor.getString(cursor.getColumnIndex(KEY_MOB_NO)));
                        subordinateBean.setCmpdt(cursor.getString(cursor.getColumnIndex(KEY_DATE)));

                        subordinateBeanList.add(subordinateBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return subordinateBeanList;
    }
}
