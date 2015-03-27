package com.appacitive.khelkund.infra;

import android.app.Application;
import android.content.Context;

/**
 * Created by sathley on 3/24/2015.
 */
public class KhelkundApplication extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        KhelkundApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return KhelkundApplication.context;
    }
}
