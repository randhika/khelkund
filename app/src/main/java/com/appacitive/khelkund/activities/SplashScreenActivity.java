package com.appacitive.khelkund.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.runnables.FetchAllPlayersIntentService;
import com.appacitive.khelkund.infra.runnables.FetchTeamRunnable;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SplashScreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectionManager.checkNetworkConnectivity(this);

        Intent mServiceIntent = new Intent(this, FetchAllPlayersIntentService.class);
        startService(mServiceIntent);

        Message msg = new Message();
        splashHandler.sendMessageDelayed(msg, 1000);
    }

    private void fetchMyTeam(final String userId) {
        Http http = new Http();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
                    return;
                if (result.optJSONArray("Players") == null)
                {

                }                    //  You are new here
                Team myTeam = new Team(result);
                myTeam.setUserId(userId);
                StorageManager storageManager = new StorageManager();
                storageManager.Save(myTeam);
                Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }

    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String userId = SharedPreferencesManager.ReadUserId();
            if (userId == null) {
                Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            } else {
                fetchMyTeam(userId);
            }
        }
    };
}
