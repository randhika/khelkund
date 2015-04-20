package com.appacitive.khelkund.model.events.privateleague;

/**
 * Created by sathley on 4/15/2015.
 */
public class PrivateLeagueDeleteEvent {
    public PrivateLeagueDeleteEvent(String leagueId)
    {
        this.LeagueId = leagueId;
    }
    public String LeagueId;
}
