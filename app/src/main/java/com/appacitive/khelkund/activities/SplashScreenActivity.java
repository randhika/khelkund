package com.appacitive.khelkund.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.services.FetchAllPick5MatchesIntentService;
import com.appacitive.khelkund.infra.services.FetchAllPlayersIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class SplashScreenActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectionManager.checkNetworkConnectivity(this);
        startBackgroundIntentServices();




        Message msg = new Message();
        splashHandler.sendMessageDelayed(msg, 3000);
    }

    private void startBackgroundIntentServices() {
        Intent mServiceIntent = new Intent(this, FetchAllPlayersIntentService.class);
        startService(mServiceIntent);

        Intent mPick5ServiceIntent = new Intent(this, FetchAllPick5MatchesIntentService.class);
        startService(mPick5ServiceIntent);
    }

    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String userId = SharedPreferencesManager.ReadUserId();
            if (userId == null) {
                Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            } else {
                Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
            finish();
        }
    };


}
