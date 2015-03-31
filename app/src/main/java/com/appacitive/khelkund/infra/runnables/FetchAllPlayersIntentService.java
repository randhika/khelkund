package com.appacitive.khelkund.infra.runnables;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FetchAllPlayersIntentService extends IntentService {

    public FetchAllPlayersIntentService() {
        super("FetchAllPlayersIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Http http = new Http();
        http.get(Urls.PlayerUrls.getAllPlayersUrl(), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                if (result.optJSONObject("Error") != null)
                    return;
                JSONArray playersJsonArray = result.optJSONArray("Players");
                List<Player> players = new ArrayList<Player>();
                for (int i = 0; i < playersJsonArray.length(); i++) {
                    players.add(new Player(playersJsonArray.optJSONObject(i)));
                }
                StorageManager storageManager = new StorageManager();
                storageManager.SavePlayers(players);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }
}
