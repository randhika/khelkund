package com.appacitive.khelkund.infra.runnables;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Team;

import org.json.JSONObject;

import java.util.HashMap;


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
                if (result.optJSONArray("Players") != null) {

                    Team myTeam = new Team(result);
                    myTeam.setUserId(userId);
                    StorageManager storageManager = new StorageManager();
                    storageManager.SaveTeam(myTeam);
                }
            }

            @Override
            public void failure(Exception e) {
            }
        });
    }
}
