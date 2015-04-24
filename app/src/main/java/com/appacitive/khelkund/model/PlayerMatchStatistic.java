package com.appacitive.khelkund.model;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathley on 4/24/2015.
 */
public class PlayerMatchStatistic extends RealmObject {

    public PlayerMatchStatistic()
    {

    }

    public PlayerMatchStatistic(JSONObject jsonObject)
    {
        if(jsonObject == null)
            return;

        this.PlayerId = jsonObject.optString("PlayerId");
        this.Catches = jsonObject.optInt("Catches");
        this.Points = jsonObject.optInt("Points");
        this.TotalBallsFaced = jsonObject.optInt("TotalBallsFaced");
        this.TotalFours = jsonObject.optInt("TotalFours");
        this.TotalMaidenOvers = jsonObject.optInt("TotalMaidenOvers");
        this.TotalOversBowled = jsonObject.optInt("TotalOversBowled");
        this.TotalRunOuts = jsonObject.optInt("TotalRunOuts");
        this.TotalRuns = jsonObject.optInt("TotalRuns");
        this.RunsGiven = jsonObject.optInt("RunsGiven");
        this.TotalSixes = jsonObject.optInt("TotalSixes");
        this.TotalWickets = jsonObject.optInt("TotalWickets");
        this.Economy = jsonObject.optDouble("Economy");
        this.StrikeRate = jsonObject.optDouble("StrikeRate");

    }

    @PrimaryKey
    private String PlayerId;
    @Index
    private String MatchId;

    private int Catches;
    private double Economy;
    private int Points;
    private double StrikeRate;
    private int TotalBallsFaced;
    private int TotalFours;
    private int TotalMaidenOvers;
    private int TotalOversBowled;
    private int TotalRunOuts;
    private int TotalRuns;
    private int RunsGiven;
    private int TotalSixes;
    private int TotalWickets;

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public int getCatches() {
        return Catches;
    }

    public void setCatches(int catches) {
        Catches = catches;
    }

    public double getEconomy() {
        return Economy;
    }

    public void setEconomy(double economy) {
        Economy = economy;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public double getStrikeRate() {
        return StrikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        StrikeRate = strikeRate;
    }

    public int getTotalBallsFaced() {
        return TotalBallsFaced;
    }

    public void setTotalBallsFaced(int totalBallsFaced) {
        TotalBallsFaced = totalBallsFaced;
    }

    public int getTotalFours() {
        return TotalFours;
    }

    public void setTotalFours(int totalFours) {
        TotalFours = totalFours;
    }

    public int getTotalMaidenOvers() {
        return TotalMaidenOvers;
    }

    public void setTotalMaidenOvers(int totalMaidenOvers) {
        TotalMaidenOvers = totalMaidenOvers;
    }

    public int getTotalOversBowled() {
        return TotalOversBowled;
    }

    public void setTotalOversBowled(int totalOversBowled) {
        TotalOversBowled = totalOversBowled;
    }

    public int getTotalRunOuts() {
        return TotalRunOuts;
    }

    public void setTotalRunOuts(int totalRunOuts) {
        TotalRunOuts = totalRunOuts;
    }

    public int getTotalRuns() {
        return TotalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        TotalRuns = totalRuns;
    }

    public int getTotalSixes() {
        return TotalSixes;
    }

    public void setTotalSixes(int totalSixes) {
        TotalSixes = totalSixes;
    }

    public int getTotalWickets() {
        return TotalWickets;
    }

    public void setTotalWickets(int totalWickets) {
        TotalWickets = totalWickets;
    }

    public int getRunsGiven() {
        return RunsGiven;
    }

    public void setRunsGiven(int runsGiven) {
        RunsGiven = runsGiven;
    }
}
