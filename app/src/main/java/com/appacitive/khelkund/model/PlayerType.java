package com.appacitive.khelkund.model;

/**
 * Created by sathley on 3/28/2015.
 */
public enum  PlayerType {
    BATSMAN ("Batsman"),
    BOWLER ("Bowler"),
    ALLROUNDER ("AllRounder"),
    WICKETKEEPER ("WicketKeeper");

    private final String name;

    private PlayerType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
        return name;
    }
}
