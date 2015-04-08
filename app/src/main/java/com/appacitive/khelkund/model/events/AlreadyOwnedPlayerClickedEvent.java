package com.appacitive.khelkund.model.events;

/**
 * Created by sathley on 4/8/2015.
 */
public class AlreadyOwnedPlayerClickedEvent {
    public AlreadyOwnedPlayerClickedEvent(String playerId)
    {
        this.PlayerId = playerId;
    }
    public String PlayerId;
}
