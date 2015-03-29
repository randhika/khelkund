package com.appacitive.khelkund.model;

import android.text.TextUtils;

import com.appacitive.khelkund.R;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Formation getFormation(Team team) {
        if (TextUtils.isEmpty(team.getFormation()))
            return null;
        String[] parts = team.getFormation().split("_");
        return new Formation(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]), Integer.valueOf(parts[3]), Integer.valueOf(parts[4]));
    }

    public static Team clone(Team team) {
        Team newTeam = new Team();
        newTeam.setTransfersRemaining(team.getTransfersRemaining());
        newTeam.setBalance(team.getBalance());
        newTeam.setName(team.getName());
        newTeam.setCaptainId(team.getCaptainId());
        newTeam.setFormation(team.getFormation());
        newTeam.setTotalPoints(team.getTotalPoints());
        newTeam.setBalance(team.getBalance());
        newTeam.setId(team.getId());
        newTeam.setImageName(team.getImageName());
        newTeam.setPlayers(team.getPlayers());
        newTeam.setPreviousMatch(team.getPreviousMatch());
        newTeam.setRank(team.getRank());
        newTeam.setUserId(team.getUserId());

        return newTeam;
    }

    public static JSONObject getJson(Team team) {
        JSONObject object = new JSONObject();

        try {
            object.put("Formation", team.getFormation());
            object.put("CaptainId", team.getCaptainId());
            object.put("UserId", team.getUserId());
            object.put("ImageName", team.getImageName());
            object.put("Name", team.getName());

            List<String> playerIds = new ArrayList<String>();
            for (Player p : team.getPlayers())
                playerIds.add(p.getId());
            object.put("Players", playerIds);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static int getTeamColor(String shortTeamName)
    {
        if(shortTeamName.equals("DD"))
            return R.color.DD;
        if(shortTeamName.equals("SRH"))
            return R.color.SRH;
        if(shortTeamName.equals("CSK"))
            return R.color.CSK;
        if(shortTeamName.equals("RCB"))
            return R.color.RCB;
        if(shortTeamName.equals("MI"))
            return R.color.MI;
        if(shortTeamName.equals("KXIP"))
            return R.color.KXIP;
        if(shortTeamName.equals("KKR"))
            return R.color.KKR;
        if(shortTeamName.equals("RR"))
            return R.color.RR;

        return R.color.DD;
    }


}
