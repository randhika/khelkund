package com.appacitive.khelkund.model.events;

import com.appacitive.khelkund.model.PrivateLeague;

/**
 * Created by sathley on 4/15/2015.
 */
public class PrivateLeagueSelectedEvent {

    public PrivateLeagueSelectedEvent(String leagueId)
    {
        this.leagueId = leagueId;
    }
    public String leagueId;
}
