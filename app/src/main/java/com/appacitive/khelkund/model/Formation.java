package com.appacitive.khelkund.model;

import java.io.Serializable;

/**
 * Created by sathley on 3/27/2015.
 */
public class Formation implements Serializable {

    public Formation(int batsmenCount, int bowlersCount, int allRoundersCount, int wicketKeepersCount)
    {
        BatsmenCount = batsmenCount;
        BowlersCount = bowlersCount;
        AllRoundersCount = allRoundersCount;
        WicketKeepersCount = wicketKeepersCount;
    }

    public int BatsmenCount = 0;
    public int BowlersCount = 0;
    public int AllRoundersCount = 0;
    public int WicketKeepersCount = 0;
}
