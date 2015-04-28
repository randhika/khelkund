package com.appacitive.khelkund.infra;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.appacitive.android.AppacitiveContext;
import com.appacitive.core.model.Environment;
import com.appacitive.khelkund.BuildConfig;
import com.appacitive.khelkund.R;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

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
        KhelkundApplication.context = getApplicationContext();
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        SharedPreferences sharedPreferences = KhelkundApplication.getAppContext().getSharedPreferences("khelkund", Context.MODE_PRIVATE);
        boolean migrationDone = sharedPreferences.getBoolean("migration_done_" + version, false);
        if(migrationDone == false){
            Realm.deleteRealmFile(context);
            Realm.deleteRealmFile(context, "db-v2.realm");
            Realm.deleteRealmFile(context, "db-v3.realm");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("migration_done_" + version , true);
            editor.commit();
        }

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.TWITTER_KEY), getResources().getString(R.string.TWITTER_SECRET));
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(this, crashlytics, new Twitter(authConfig));
        AppacitiveContext.initialize("+HfTOp2nF8TnkyZVBblkTBLm6Cz6zIfKYdXBhV6Aag4=", Environment.live, getAppContext());
        FacebookSdk.sdkInitialize(context);

        String userId = SharedPreferencesManager.ReadUserId();
        if(userId != null)
            Crashlytics.setUserIdentifier(userId);
        else Crashlytics.setUserIdentifier("not logged in yet");
    }

    public static Context getAppContext() {
        return KhelkundApplication.context;
    }
}
