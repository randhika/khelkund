package com.appacitive.khelkund.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sathley on 4/7/2015.
 */
public class Pick5MatchDetails {

    public Pick5MatchDetails(JSONObject json)
    {
        if(json == null)
            return;
        this.MatchDetails = new Match(json.optJSONObject("MatchDetails"));
        this.Result = json.optInt("Result");
        this.TeamPoints = json.optInt("TeamPoints");
        this.AppTeamPoints = json.optInt("AppTeamPoints");

        this.AppPlayers = new ArrayList<Player>();
        JSONArray appPlayersArray = json.optJSONArray("AppPlayers");
        if(appPlayersArray != null)
        {
            for(int i=0; i< appPlayersArray.length(); i++)
                this.AppPlayers.add(new Player(appPlayersArray.optJSONObject(i)));
        }

        this.Players = new ArrayList<Player>();
        JSONArray playersArray = json.optJSONArray("Players");
        if(playersArray != null)
        {
            for(int i=0; i< playersArray.length(); i++)
                this.Players.add(new Player(playersArray.optJSONObject(i)));
        }

        this.PlayerMappings = new HashMap<String, String>();
        JSONArray mappingsArray = json.optJSONArray("PlayerMapping");
        if(mappingsArray != null)
        {
            for(int i = 0; i < mappingsArray.length(); i++)
            {
                this.PlayerMappings.put(mappingsArray.optJSONObject(i).optString("UserPlayerId"), mappingsArray.optJSONObject(i).optString("AppPlayerId"));
            }
        }
    }

    private Match MatchDetails;
    private List<Player> AppPlayers;
    private List<Player> Players;
    private int Result;
    private Map<String, String> PlayerMappings;
    private int TeamPoints;
    private int AppTeamPoints;

    public Map<String, String> getPlayerMappings() {
        return PlayerMappings;
    }

    public void setPlayerMappings(Map<String, String> playerMappings) {
        PlayerMappings = playerMappings;
    }

    public Match getMatchDetails() {
        return MatchDetails;
    }

    public void setMatchDetails(Match matchDetails) {
        MatchDetails = matchDetails;
    }

    public List<Player> getAppPlayers() {
        return AppPlayers;
    }

    public void setAppPlayers(List<Player> appPlayers) {
        AppPlayers = appPlayers;
    }

    public List<Player> getPlayers() {
        return Players;
    }

    public void setPlayers(List<Player> players) {
        Players = players;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public int getTeamPoints() {
        return TeamPoints;
    }

    public void setTeamPoints(int teamPoints) {
        TeamPoints = teamPoints;
    }

    public int getAppTeamPoints() {
        return AppTeamPoints;
    }

    public void setAppTeamPoints(int appTeamPoints) {
        AppTeamPoints = appTeamPoints;
    }
}


