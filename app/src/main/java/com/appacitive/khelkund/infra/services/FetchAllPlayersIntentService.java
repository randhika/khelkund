package com.appacitive.khelkund.infra.services;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FetchAllPlayersIntentService extends IntentService {

    public FetchAllPlayersIntentService() {
        super("FetchAllPlayersIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Http http = new Http();
        http.get(Urls.AppacitiveUrls.getAllPlayersUrl(), Urls.AppacitiveUrls.getHeaders(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                List<Player> players = new ArrayList<Player>();
                if(result.optJSONObject("status").optString("code").equals("200") == false)
                    return;
                JSONArray playersArray = result.optJSONArray("nodes");
                if(playersArray == null)
                    return;
                for(int i  = 0; i < playersArray.length() ; i++)
                {
                    JSONObject playerObject = playersArray.optJSONObject(i);
                    if(playerObject == null)
                        continue;
                    Player player = new Player();
                    player.setFirstName(playerObject.optString("firstname"));
                    player.setLastName(playerObject.optString("lastname"));
                    player.setType(playerObject.optString("playertype"));
                    player.setPoints(Integer.valueOf(playerObject.optString("points")));
                    player.setShortTeamName(playerObject.optString("shortteamname"));
                    player.setPrice(Integer.valueOf(playerObject.optString("price")));
                    player.setImageUrl(playerObject.optString("image_url"));
                    player.setDisplayName(playerObject.optString("displayname"));
                    player.setId(playerObject.optString("__id"));
                    JSONObject popularityCount = playerObject.optJSONObject("$popularity_count");
                    if(popularityCount != null)
                    {
                        player.setPopularity((int)Double.parseDouble(popularityCount.optString("all")));
                    }
                    players.add(player);
                }
                StorageManager storageManager = new StorageManager();
                storageManager.SavePlayers(players);
            }

            @Override
            public void failure(Exception e) {

            }
        });

        http.get(Urls.TeamUrls.getAllTeamsCountUrl(), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                int count = result.optInt("Count");
                SharedPreferencesManager.WriteTotalTeamsCount(count);
            }

            @Override
            public void failure(Exception e) {

            }
        });





//        Http http = new Http();
//        http.get(Urls.PlayerUrls.getAllPlayersUrl(), new HashMap<String, String>(), new APCallback() {
//            @Override
//            public void success(JSONObject result) {
//                if (result.optJSONObject("Error") != null)
//                    return;
//                JSONArray playersJsonArray = result.optJSONArray("Players");
//                List<Player> players = new ArrayList<Player>();
//                for (int i = 0; i < playersJsonArray.length(); i++) {
//                    players.add(new Player(playersJsonArray.optJSONObject(i)));
//                }
//                StorageManager storageManager = new StorageManager();
//                storageManager.SavePlayers(players);
//            }
//
//            @Override
//            public void failure(Exception e) {
//
//            }
//        });
    }
}
