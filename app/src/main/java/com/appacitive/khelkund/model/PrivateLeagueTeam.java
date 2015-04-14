package com.appacitive.khelkund.model;

import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 4/14/2015.
 */
@RealmClass
public class PrivateLeagueTeam extends RealmObject {

    public PrivateLeagueTeam()
    {

    }

    public PrivateLeagueTeam(JSONObject json)
    {
        if (json == null)
            return;

        this.Username = json.optString("UserName");
        this.UserTeamName = json.optString("UserTeamName");
        this.UserTeamId = json.optString("UserTeamId");
        this.ImageName = json.optString("ImageName");
        this.Name = json.optString("Name");
        this.Rank = json.optInt("Rank");
        this.TotalPoints = json.optInt("Points");
    }

    private String Username;
    private String UserTeamId;
    private String UserTeamName;
    private String ImageName;
    private String Name;
    private int Rank=0;
    private int TotalPoints=0;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserTeamId() {
        return UserTeamId;
    }

    public void setUserTeamId(String userTeamId) {
        UserTeamId = userTeamId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public int getTotalPoints() {
        return TotalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        TotalPoints = totalPoints;
    }

    public String getUserTeamName() {
        return UserTeamName;
    }

    public void setUserTeamName(String userTeamName) {
        UserTeamName = userTeamName;
    }
}
