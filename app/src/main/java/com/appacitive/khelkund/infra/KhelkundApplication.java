package com.appacitive.khelkund.infra;

import android.app.Application;
import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by sathley on 3/24/2015.
 */
public class KhelkundApplication extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IjYVjFetPBvINUGr5JxxbP99V";
    private static final String TWITTER_SECRET = "gnTvCVeDBp0GSPRYDtCDiAWeGoC1HEAMs6AOBZxeOcC5bURWpS";
    private static Context context;

    public void onCreate(){
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        KhelkundApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return KhelkundApplication.context;
    }
}
