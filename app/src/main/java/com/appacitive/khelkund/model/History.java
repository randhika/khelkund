package com.appacitive.khelkund.model;

import io.realm.RealmObject;

/**
 * Created by sathley on 3/24/2015.
 */
public class History extends RealmObject {

    public History()
    {

    }

    public History(String opposition, int points)
    {
        this.Opposition = opposition;
        this.Points = points;
    }
    private String Opposition;
    private int Points;

    public String getOpposition() {
        return Opposition;
    }

    public void setOpposition(String opposition) {
        Opposition = opposition;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
