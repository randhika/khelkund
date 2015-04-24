package com.appacitive.khelkund.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathley on 4/24/2015.
 */
public class MatchStatistic extends RealmObject {

    public MatchStatistic() {

    }

    public MatchStatistic(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.MatchId = jsonObject.optString("MatchId");
        JSONArray playerIdsArray = jsonObject.optJSONArray("UserPlayerId");


        if (playerIdsArray != null && playerIdsArray.length() > 0) {
            List<String> playerdIds = new ArrayList<String>();
            for (int i = 0; i < playerIdsArray.length(); i++)
                playerdIds.add(playerIdsArray.optString(i));

            this.UserPlayerIds = TextUtils.join(",", playerdIds);

        }

        this.PlayerStatistics = new RealmList<PlayerMatchStatistic>();
        JSONArray playerStatsArray = jsonObject.optJSONArray("PlayerStatistics");
        if (playerStatsArray != null && playerStatsArray.length() > 0) {
            for (int i = 0; i < playerStatsArray.length(); i++)
                this.PlayerStatistics.add(new PlayerMatchStatistic(playerStatsArray.optJSONObject(i)));
        }

    }

    @PrimaryKey
    private String MatchId;
    @Index
    private String UserId;
    private String UserPlayerIds;
    private RealmList<PlayerMatchStatistic> PlayerStatistics;

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserPlayerIds() {
        return UserPlayerIds;
    }

    public void setUserPlayerIds(String userPlayerIds) {
        UserPlayerIds = userPlayerIds;
    }

    public RealmList<PlayerMatchStatistic> getPlayerStatistics() {
        return PlayerStatistics;
    }

    public void setPlayerStatistics(RealmList<PlayerMatchStatistic> playerStatistics) {
        PlayerStatistics = playerStatistics;
    }
}
