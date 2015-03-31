package com.appacitive.khelkund.model;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 3/24/2015.
 */

@RealmClass
public class Statistics extends RealmObject {

    public Statistics()
    {

    }

    public Statistics(JSONObject json)
    {
        if (json == null)
            return;
        this.Economy = json.optDouble("Economy");
        this.Fifties = json.optInt("Fifties");
        this.FiveWickets = json.optInt("FiveWickets");
        this.Hundreds = json.optInt("Hundreds");
        this.MatchesPlayed = json.optInt("MatchesPlayed");
        this.RunsScored = json.optInt("RunsScored");
        this.StrikeRate = json.optDouble("StrikeRate");
        this.ThreeWickets = json.optInt("ThreeWickets");
        this.Wickets = json.optInt("Wickets");
    }

    private double Economy =0.0;
    private int Fifties=0;
    private int Hundreds=0;
    private int FiveWickets=0;
    private int ThreeWickets=0;
    private int Wickets=0;
    private int MatchesPlayed=0;
    private int RunsScored=0;
    private double StrikeRate=0.0;

    public double getStrikeRate() {
        return StrikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        StrikeRate = strikeRate;
    }

    public int getRunsScored() {
        return RunsScored;
    }

    public void setRunsScored(int runsScored) {
        RunsScored = runsScored;
    }

    public int getMatchesPlayed() {
        return MatchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        MatchesPlayed = matchesPlayed;
    }

    public int getWickets() {
        return Wickets;
    }

    public void setWickets(int wickets) {
        Wickets = wickets;
    }

    public int getThreeWickets() {
        return ThreeWickets;
    }

    public void setThreeWickets(int threeWickets) {
        ThreeWickets = threeWickets;
    }

    public int getFiveWickets() {
        return FiveWickets;
    }

    public void setFiveWickets(int fiveWickets) {
        FiveWickets = fiveWickets;
    }

    public int getHundreds() {
        return Hundreds;
    }

    public void setHundreds(int hundreds) {
        Hundreds = hundreds;
    }

    public int getFifties() {
        return Fifties;
    }

    public void setFifties(int fifties) {
        Fifties = fifties;
    }

    public double getEconomy() {
        return Economy;
    }

    public void setEconomy(double economy) {
        Economy = economy;
    }
}
