package com.appacitive.khelkund.infra.runnables;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.appacitive.khelkund.activities.HomeActivity;
import com.appacitive.khelkund.activities.LoginActivity;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FetchMyPLayersIntentService extends IntentService {

    public FetchMyPLayersIntentService() {
        super("FetchMyPLayersIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Http http = new Http();
        final String userId = SharedPreferencesManager.ReadUserId();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
                {
                    return;
                }

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
}
