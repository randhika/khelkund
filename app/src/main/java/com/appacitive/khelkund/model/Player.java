package com.appacitive.khelkund.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathley on 3/24/2015.
 */
public class Player extends RealmObject {

    public Player() {

    }

    public Player(JSONObject json) {
        if (json == null)
            return;
        this.DisplayName = json.optString("DisplayName");
        this.Id = json.optString("Id");
        this.ImageUrl = json.optString("TImageUrl");
        this.ShortTeamName = json.optString("ShortTeamName");
        this.Price = json.optInt("Price");
        this.Popularity = json.optInt("Popularity");
        this.Points = json.optInt("Points");
        this.PlayingPosition = json.optString("PlayingPosition");
        this.NextOpponent = json.optString("NextOpponent");
        JSONArray history = json.optJSONArray("PointsHistory");
        if (history != null) {
            if (history.isNull(0) == false) {
                this.PointsHistory1 = (int) history.opt(0);
            }
            if (history.isNull(1) == false) {
                this.PointsHistory2 = (int) history.opt(1);
            }
            if (history.isNull(2) == false) {
                this.PointsHistory3 = (int) history.opt(2);
            }
            if (history.isNull(3) == false) {
                this.PointsHistory4 = (int) history.opt(3);
            }
            if (history.isNull(4) == false) {
                this.PointsHistory5 = (int) history.opt(4);
            }
        }
    }

    @PrimaryKey
    private String Id;
    private String DisplayName;
    private String ImageUrl;
    private int Price;
    private int Popularity;
    private int Points;
    private String PlayingPosition;
    private String NextOpponent;
    private String ShortTeamName;
    private int PointsHistory1;
    private int PointsHistory2;
    private int PointsHistory3;
    private int PointsHistory4;
    private int PointsHistory5;
    private Statistics Statistics;

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getPopularity() {
        return Popularity;
    }

    public void setPopularity(int popularity) {
        Popularity = popularity;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }

    public String getPlayingPosition() {
        return PlayingPosition;
    }

    public void setPlayingPosition(String playingPosition) {
        PlayingPosition = playingPosition;
    }

    public String getNextOpponent() {
        return NextOpponent;
    }

    public void setNextOpponent(String nextOpponent) {
        NextOpponent = nextOpponent;
    }

    public String getShortTeamName() {
        return ShortTeamName;
    }

    public void setShortTeamName(String shortTeamName) {
        ShortTeamName = shortTeamName;
    }

    public int getPointsHistory1() {
        return PointsHistory1;
    }

    public void setPointsHistory1(int pointsHistory1) {
        PointsHistory1 = pointsHistory1;
    }

    public int getPointsHistory2() {
        return PointsHistory2;
    }

    public void setPointsHistory2(int pointsHistory2) {
        PointsHistory2 = pointsHistory2;
    }

    public int getPointsHistory4() {
        return PointsHistory4;
    }

    public void setPointsHistory4(int pointsHistory4) {
        PointsHistory4 = pointsHistory4;
    }

    public int getPointsHistory3() {
        return PointsHistory3;
    }

    public void setPointsHistory3(int pointsHistory3) {
        PointsHistory3 = pointsHistory3;
    }

    public int getPointsHistory5() {
        return PointsHistory5;
    }

    public void setPointsHistory5(int pointsHistory5) {
        PointsHistory5 = pointsHistory5;
    }

    public com.appacitive.khelkund.model.Statistics getStatistics() {
        return Statistics;
    }

    public void setStatistics(com.appacitive.khelkund.model.Statistics statistics) {
        Statistics = statistics;
    }
}
