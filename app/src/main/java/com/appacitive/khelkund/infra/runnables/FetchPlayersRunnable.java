package com.appacitive.khelkund.infra.runnables;

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

/**
 * Created by sathley on 3/29/2015.
 */
public class FetchPlayersRunnable implements Runnable {
    @Override
    public void run() {
//        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
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
                storageManager.Save(players);
            }

            @Override
            public void failure(Exception e) {

            }
        });
    }
}
