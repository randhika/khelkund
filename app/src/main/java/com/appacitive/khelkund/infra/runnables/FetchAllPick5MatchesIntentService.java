package com.appacitive.khelkund.infra.runnables;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Match;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FetchAllPick5MatchesIntentService extends IntentService {

    public FetchAllPick5MatchesIntentService() {
        super("FetchAllPick5MatchesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Http http = new Http();
        http.get(Urls.Pick5Urls.getAllMatchesUrl(), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if(result.optJSONObject("Error") != null)
                {
                    return;
                }
                JSONArray matchesArray = result.optJSONArray("Matches");
                List<Match> matchList = new ArrayList<Match>();
                for(int i = 0;i<matchesArray.length(); i++)
                {
                    matchList.add(new Match(matchesArray.optJSONObject(i)));
                }
                StorageManager manager = new StorageManager();
                manager.SaveMatches(matchList);
            }

            @Override
            public void failure(Exception e) {
            }
        });
    }
}
