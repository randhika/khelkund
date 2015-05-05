package com.appacitive.khelkund.activities.misc;

import android.app.ProgressDialog;
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
import com.appacitive.khelkund.infra.services.FetchAllPick5MatchesIntentService;
import com.appacitive.khelkund.infra.services.FetchAllPlayersIntentService;
import com.appacitive.khelkund.infra.services.FetchAllPrivateLeaguesIntentService;
import com.appacitive.khelkund.model.KhelkundUser;

import org.json.JSONObject;

import java.util.HashMap;


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

        Intent mPrivateLeagueIntent = new Intent(this, FetchAllPrivateLeaguesIntentService.class);
        startService(mPrivateLeagueIntent);
    }

    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String userId = SharedPreferencesManager.ReadUserId();

            if (userId == null) {
                Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
                finish();
            } else {
                KhelkundUser user = new StorageManager().GetUser(userId);
                if (user == null) {
                    getLoggedInUser(userId);
                } else {
                    Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
                    finish();
                }
            }

        }
    };

    private void getLoggedInUser(String userId)
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching user details");
        dialog.show();

        Http http = new Http();
        http.get(Urls.UserUrls.getUserUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                if (result.optJSONObject("Error") == null) {
                    Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
                    finish();
                    return;
                }
                KhelkundUser user = new KhelkundUser(result.optJSONObject("User"));
                new StorageManager().SaveUser(user);
                Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
                finish();
            }

            @Override
            public void failure(Exception e) {
                dialog.dismiss();
                Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.slide_in_right_fast, R.anim.slide_out_left_fast);
            }
        });
    }


}
