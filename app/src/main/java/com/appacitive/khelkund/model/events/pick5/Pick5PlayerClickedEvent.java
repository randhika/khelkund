package com.appacitive.khelkund.model.events.pick5;

import com.appacitive.khelkund.model.Player;

/**
 * Created by sathley on 4/10/2015.
 */
public class Pick5PlayerClickedEvent {
    public Pick5PlayerClickedEvent(int position)
    {
        this.position = position;
    }
    public int position;
    public Player player;
}
