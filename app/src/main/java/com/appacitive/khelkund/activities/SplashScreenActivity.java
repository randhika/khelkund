package com.appacitive.khelkund.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.ConnectionManager;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SplashScreenActivity extends ActionBarActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        this.context = this;
        ConnectionManager.checkNetworkConnectivity(this);

        fetchAllPlayers();
        Runnable endSplash = new Runnable() {
            @Override
            public void run() {
                String userId = SharedPreferencesManager.ReadUserId();
                if (userId == null) {
                    Intent loginIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    fetchMyTeam(userId);
                    Intent homeIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
            }
        };
        new Handler().postDelayed(endSplash, 5000L);


    }

    private void fetchMyTeam(final String userId) {
        Http http = new Http(context.getApplicationContext());
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optString("Error") == null)
                    return;
                if(result.optJSONArray("Players") == null)
                    //  You are new here
                    return;
                Team myTeam = new Team(result);
                myTeam.setUserId(userId);
                StorageManager storageManager = new StorageManager();
                storageManager.Save(myTeam);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }

    private void fetchAllPlayers() {
        Http http = new Http(context.getApplicationContext());
        http.get(Urls.PlayerUrls.getAllPlayersUrl(), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optString("Error") == null)
                    return;
                JSONArray playersJsonArray = result.optJSONArray("Players");
                List<Player> players = new ArrayList<Player>();
                for (int i = 0; i < playersJsonArray.length(); i++) {
                    players.add(new Player(playersJsonArray.optJSONObject(i)));
                }
                StorageManager storageManager = new StorageManager();
                storageManager.Save(players);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }
}
