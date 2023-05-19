package activity;

/**
 * Created by shakti on 12/19/2016.
 */


import android.app.Application;
import android.content.Context;



/*import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;*/
//
//@ReportsCrashes(mailTo = "info.salesmanapp@shaktipumps.com;mobileapp@shaktipumps.com;mayank.singh@shaktipumps.com",
////@ReportsCrashes(mailTo = "mayank.singh@shaktipumps.com",
//        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.crash_toast_text) //you get to define resToastText


public class CrashDetails extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
//        ACRA.init(this);
    }
}


