package com.appacitive.khelkund.infra.runnables;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Team;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sathley on 3/29/2015.
 */
public class FetchTeamRunnable implements Runnable {
    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        final String userId = SharedPreferencesManager.ReadUserId();
        Http http = new Http();
        http.get(Urls.TeamUrls.getMyTeamUrl(userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
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
}
