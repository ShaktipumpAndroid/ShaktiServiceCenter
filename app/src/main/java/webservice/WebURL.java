package webservice;

import bean.LoginBean;

/**
 * Created by shakti on 10/6/2016.
 */


public class WebURL {
    public static  String mViewPhotoGraph = "";

// ************ Developments server *************

    public static  String STATUS_CHECK_FOR_COMPLAIN= "";
    public static  int EDITTEXT_SHOW_SIZE;
    public static final String BASE_URL_VK= "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/";

    public static final String LOGIN_VK_PAGE = BASE_URL_VK +"login.htm";
    public static final String PENDING_COMPLAIN_ALL_LIST_VK_PAGE = BASE_URL_VK +"get_header.htm";
    public static final String PENDING_COMPLAIN_ALL_DETAILS_VK_PAGE = BASE_URL_VK +"get_item.htm";
    public static final String PENDING_COMPLAIN_REMARK_SAPRATE_VK_PAGE = BASE_URL_VK +"save_action.htm";
    public static final String PENDING_COMPLAIN_PENDING_PHOTO_VK_PAGE = BASE_URL_VK +"save_pending_complaint.htm";
    public static final String PENDING_COMPLAIN_APPROVED_PHOTO_VK_PAGE = BASE_URL_VK +"save_approved_complaint.htm";

    /// All old service
    public static final String ATTENDANCE_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/attendance.htm";
    public static final String TARGET_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/sales_target.htm";
    public static final String VISIT_HISTORY_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/visit_history.htm";

    public static final String SEARCH_HELP_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/search_help.htm";
    public static final String ROUTE_DETAIL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_route_plan.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/sync_offline_data_new.htm";

    public static final String ORDER_MATERIAL_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_data.htm";
    public static final String RESEND_PASSWORD_PAGE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/Reset_Password.htm";
    public static final String IMAGE_DIRECTORY_NAME = "Shakti App Image";

    public static final String MATERIAL_ANALYSIS = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_analysis.htm";
    public static final String CUSTOMER_COMPLAINT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_customer_complaint.htm";

    public static final String CUSTOMER_COMPLAINT_SERIAL_NUMBER = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_serial_number_complaint.htm";
    public static final String SERVICE_CENTER_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_service_center.htm";
    public static final String LOCATION_HELP = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/location_data.htm";

    public static final String FORWARD_TO = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/forward_to.htm";
    public static final String COMPLAINT_ATTACHMENT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/attach_cmpln_image.htm";

     public static final String NORMS_MATERIAL_LIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/material_norms_stock.htm";
    public static final String NORM_DATA_ENTRY = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/norms_data_entry.htm";

    static String str = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/file_folder.htm" + "?id=" + LoginBean.userid;
    public static final String FILES_AND_FOLDER = str;
    static String dashboard_url = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zapp_page/registration.htm" + "?=" + LoginBean.userid;

    public static final String DASHBOARD = dashboard_url;
    public static final String LOCAL_CONVENIENVCE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zhr_emp_app_1/start_end_location1.htm";
    public static final String DEVICE_DETAILS = "https://solar10.shaktisolarrms.com/Home/SAPOnlineDeviceDetails" ;

    public static final String CMPLN_ATTACHMENT = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_sales_emp/get_cmp_attachment.htm";
    public static final String STATE_DATA = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar/state_data.htm";
    public static final String GET_SUORDINATE = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/get_subordinate.htm";
    public static final String SAVEEDITDETAIL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/subordinate_edit.htm";
    public static final String ASSGINCOMPLAINlIST = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/assign_complains_details.htm?";
}
