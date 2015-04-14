package com.appacitive.khelkund.infra.services;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.PrivateLeague;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FetchAllPrivateLeaguesIntentService extends IntentService {

    public FetchAllPrivateLeaguesIntentService() {
        super("FetchAllPrivateLeaguesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Http http = new Http();
        final String mUserId = SharedPreferencesManager.ReadUserId();
        if(mUserId == null)
            return;
        http.get(Urls.PrivateLeagueUrls.getPrivateLeaguesUrl(mUserId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null) {
                    return;
                }
                List<PrivateLeague> privateLeagues = new ArrayList<PrivateLeague>();
                JSONArray leaguesArray = result.optJSONArray("PrivateLeagues");
                if (leaguesArray != null) {
                    for (int i = 0; i < leaguesArray.length(); i++) {
                        PrivateLeague league = new PrivateLeague(leaguesArray.optJSONObject(i));
                        league.setUserId(mUserId);
                        privateLeagues.add(league);
                    }

                }
                StorageManager mManager = new StorageManager();
                mManager.SavePrivateLeagues(privateLeagues);
            }

            @Override
            public void failure(Exception e) {
            }
        });
    }

}
