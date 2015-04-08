package com.appacitive.khelkund.model.events;

/**
 * Created by sathley on 4/8/2015.
 */
public class NewPlayerAddedEvent {
    public NewPlayerAddedEvent(String playerId)
    {
        this.PlayerId = playerId;
    }
    public String PlayerId;
}
