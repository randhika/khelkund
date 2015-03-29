package com.appacitive.khelkund.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley on 3/28/2015.
 */
public class TeamHelper {

    public static List<Player> getBatsmen(Team team) {
        List<Player> batsmen = new ArrayList<Player>();
        if (team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("Batsman"))
                    batsmen.add(p);
        return batsmen;
    }

    public static List<Player> getBowlers(Team team) {
        List<Player> bowlers = new ArrayList<Player>();
        if (team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("Bowler"))
                    bowlers.add(p);
        return bowlers;
    }

    public static List<Player> getAllRounders(Team team) {
        List<Player> allRounders = new ArrayList<Player>();
        if (team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("AllRounder"))
                    allRounders.add(p);
        return allRounders;
    }

    public static List<Player> getWicketKeepers(Team team) {
        List<Player> wk = new ArrayList<Player>();
        if (team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("WicketKeeper"))
                    wk.add(p);
        return wk;
    }

    public static Formation getFormation(Team team)
    {
        if(TextUtils.isEmpty(team.getFormation()))
            return null;
        String[] parts = team.getFormation().split("_");
        return new Formation(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), Integer.valueOf(parts[4]));
    }


}
