package com.appacitive.khelkund.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 3/24/2015.
 */

@RealmClass
public class Team extends RealmObject {

    public Team() {
        this.Players = new RealmList<Player>();
        this.TeamHistory = new RealmList<History>();
    }

    public Team(JSONObject json) {
        if (json == null)
            return;

        this.Balance = json.optInt("Balance");
        this.CaptainId = json.optString("CaptainId");
        this.Formation = json.optString("Formation");
        this.Id = json.optString("Id");
        this.ImageName = json.optString("ImageName");
        this.Name = json.optString("Name");
        this.PreviousMatch = json.optString("PrevMatch");
        this.Rank = json.optInt("Rank");
        this.TotalPoints = json.optInt("TotalPoints");
        this.TransfersRemaining = json.optInt("TransfersRemaining");

        JSONArray playersArray = json.optJSONArray("Players");
        this.Players = new RealmList<Player>();
        if (playersArray != null) {
            for (int i = 0; i < playersArray.length(); i++) {
                this.Players.add(new Player(playersArray.optJSONObject(i)));
            }
        }
        JSONObject historyObject = json.optJSONObject("TeamHistory");
        this.TeamHistory = new RealmList<History>();
        if (historyObject != null) {
            JSONArray pointsArray = historyObject.optJSONArray("Points");
            for (int i = 0; i < pointsArray.length(); i++) {
                this.TeamHistory.add(new History(pointsArray.optJSONObject(i).optString("Opposition"), pointsArray.optJSONObject(i).optInt("Points")));
            }
        }
    }

    @PrimaryKey
    private String UserId;

    private int Balance=0;
    private String CaptainId;
    private String Formation;
    private String Id;
    private String ImageName;
    private String Name;
    private RealmList<Player> Players;
    private String PreviousMatch;
    private int Rank;
    private int TotalPoints;
    private int TransfersRemaining;
    private RealmList<History> TeamHistory;

    public RealmList<Player> getPlayers() {
        return Players;
    }

    public RealmList<History> getTeamHistory() {
        return TeamHistory;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }

    public String getCaptainId() {
        return CaptainId;
    }

    public void setCaptainId(String captainId) {
        CaptainId = captainId;
    }

    public String getFormation() {
        return Formation;
    }

    public void setFormation(String formation) {
        Formation = formation;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getPreviousMatch() {
        return PreviousMatch;
    }

    public void setPreviousMatch(String previousMatch) {
        PreviousMatch = previousMatch;
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

    public int getTransfersRemaining() {
        return TransfersRemaining;
    }

    public void setTransfersRemaining(int transfersRemaining) {
        TransfersRemaining = transfersRemaining;
    }

    public void setPlayers(RealmList<Player> players) {
        Players = players;
    }

    public void setTeamHistory(RealmList<History> teamHistory) {
        TeamHistory = teamHistory;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
