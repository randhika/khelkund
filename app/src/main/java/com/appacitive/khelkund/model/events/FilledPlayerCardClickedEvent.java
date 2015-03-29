package com.appacitive.khelkund.model.events;

import com.appacitive.khelkund.model.PlayerType;

import java.io.Serializable;

/**
 * Created by sathley on 3/28/2015.
 */
public class FilledPlayerCardClickedEvent extends PlayerCardClickedEventBase implements Serializable {
    public String playerId;
}
