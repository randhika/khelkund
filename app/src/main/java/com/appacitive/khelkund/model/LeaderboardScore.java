package com.appacitive.khelkund.model;

import org.json.JSONObject;

/**
 * Created by sathley on 4/2/2015.
 */
public class LeaderboardScore {
    private int Points;
    private int Rank;
    private String UserId;
    private String UserName;
    private String TeamName;
    private String UserTeamImage;

    public LeaderboardScore(JSONObject json) {
        if (json == null)
            return;

        this.Points = json.optInt("Points");
        this.Rank = json.optInt("Rank");
        this.UserId = json.optString("UserId");
        this.UserName = json.optString("UserName");
        this.TeamName = json.optString("UserTeamName");
        this.UserTeamImage = json.optString("UserTeamImage");
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String userTeamName) {
        TeamName = userTeamName;
    }

    public String getUserTeamImage() {
        return UserTeamImage;
    }

    public void setUserTeamImage(String userTeamImage) {
        UserTeamImage = userTeamImage;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
