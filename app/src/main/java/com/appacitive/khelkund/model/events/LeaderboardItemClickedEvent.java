package com.appacitive.khelkund.model.events;

/**
 * Created by sathley on 4/20/2015.
 */
public class LeaderboardItemClickedEvent {

    public LeaderboardItemClickedEvent(String userId)
    {
        this.UserId = userId;
    }

    public String UserId;
}
