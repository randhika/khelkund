package com.appacitive.khelkund.model.events.privateleague;

/**
 * Created by sathley on 4/15/2015.
 */
public class PrivateLeagueShareEvent {
    public PrivateLeagueShareEvent(String leagueId)
    {
        this.LeagueId = leagueId;
    }
    public String LeagueId;
}
