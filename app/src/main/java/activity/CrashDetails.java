package activity;

//**
 //* Created by shakti on 12/19/2016.
// */


import android.app.Application;
import android.content.Context;


public class CrashDetails extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
//        ACRA.init(this);
    }
}


