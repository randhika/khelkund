package com.appacitive.khelkund.infra.services;

import android.app.IntentService;
import android.content.Intent;

import com.appacitive.core.AppacitiveObject;
import com.appacitive.core.model.Callback;
import com.appacitive.core.model.ConnectedObject;
import com.appacitive.core.model.ConnectedObjectsResponse;
import com.appacitive.core.query.AppacitiveQuery;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.model.Player;
import com.crashlytics.android.Crashlytics;

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

        //  Fetch total teams count from server
        Http http = new Http();
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

        //  Fetch all players from server
        AppacitiveQuery query = new AppacitiveQuery();
        query.pageSize = 300;
        query.pageNumber = 1;
        AppacitiveObject.getConnectedObjectsInBackground("series_player", "series", 88663391933170156l, query, null, new Callback<ConnectedObjectsResponse>() {
            @Override
            public void success(ConnectedObjectsResponse result) {
                List<Player> players = new ArrayList<Player>();
                for (ConnectedObject connectedObject : result.results) {
                    AppacitiveObject apPlayer = connectedObject.object;
                    Player player = new Player();
                    player.setFirstName(apPlayer.getPropertyAsString("firstname"));
                    player.setLastName(apPlayer.getPropertyAsString("lastname"));
                    player.setType(apPlayer.getPropertyAsString("playertype"));
                    player.setPoints(apPlayer.getPropertyAsInt("points"));
                    player.setShortTeamName(apPlayer.getPropertyAsString("shortteamname"));
                    player.setPrice(apPlayer.getPropertyAsInt("price"));
                    player.setImageUrl(apPlayer.getPropertyAsString("image_url"));
                    player.setDisplayName(apPlayer.getPropertyAsString("displayname"));
                    player.setId(String.valueOf(apPlayer.getId()));
                    Map<String, String> popularityAggregate = apPlayer.getAggregate("popularity_count");
                    if (popularityAggregate != null && popularityAggregate.keySet().contains("all"))
                        player.setPopularity((int) Double.parseDouble(popularityAggregate.get("all")));

                    players.add(player);

                }
                StorageManager storageManager = new StorageManager();
                storageManager.SavePlayers(players);
            }

            @Override
            public void failure(ConnectedObjectsResponse result, Exception e) {

            }
        });
    }
}
