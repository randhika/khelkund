package com.appacitive.khelkund.infra;

import android.text.TextUtils;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.model.Formation;
import com.appacitive.khelkund.model.History;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.PrivateLeagueTeam;
import com.appacitive.khelkund.model.Statistics;
import com.appacitive.khelkund.model.Team;

import org.json.JSONArray;
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
        if (team != null && team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("Batsman"))
                    batsmen.add(p);
        return batsmen;
    }

    public static List<Player> getBatsmen(List<Player> players) {
        List<Player> bowlers = new ArrayList<Player>();
        if (players != null)
            for (Player p : players)
                if (p.getType().equals("Batsman"))
                    bowlers.add(p);
        return bowlers;
    }

    public static List<Player> getBowlers(Team team) {

        List<Player> bowlers = new ArrayList<Player>();
        if (team != null && team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("Bowler"))
                    bowlers.add(p);
        return bowlers;
    }

    public static List<Player> getBowlers(List<Player> players) {
        List<Player> bowlers = new ArrayList<Player>();
        if (players != null)
            for (Player p : players)
                if (p.getType().equals("Bowler"))
                    bowlers.add(p);
        return bowlers;
    }

    public static List<Player> getAllRounders(Team team) {

        List<Player> allRounders = new ArrayList<Player>();
        if (team != null && team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("AllRounder"))
                    allRounders.add(p);
        return allRounders;
    }

    public static List<Player> getAllRounders(List<Player> players) {
        List<Player> bowlers = new ArrayList<Player>();
        if (players != null)
            for (Player p : players)
                if (p.getType().equals("AllRounder"))
                    bowlers.add(p);
        return bowlers;
    }

    public static List<Player> getWicketKeepers(Team team) {
        List<Player> wk = new ArrayList<Player>();
        if (team != null && team.getPlayers() != null)
            for (Player p : team.getPlayers())
                if (p.getType().equals("WicketKeeper"))
                    wk.add(p);
        return wk;
    }

    public static List<Player> getWicketKeepers(List<Player> players) {
        List<Player> bowlers = new ArrayList<Player>();
        if (players != null)
            for (Player p : players)
                if (p.getType().equals("WicketKeeper"))
                    bowlers.add(p);
        return bowlers;
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
        newTeam.setPreviousMatch(team.getPreviousMatch());
        newTeam.setRank(team.getRank());
        newTeam.setUserId(team.getUserId());
        if (team.getPlayers() != null)
            for (Player p : team.getPlayers())
                newTeam.getPlayers().add(clone(p));
        if(team.getTeamHistory() != null)
            for (History h : team.getTeamHistory())
                newTeam.getTeamHistory().add(clone(h));
        return newTeam;
    }

    public static List<PrivateLeague> clone(List<PrivateLeague> leagues)
    {
        List<PrivateLeague> newList = new ArrayList<PrivateLeague>();
        for(PrivateLeague league : leagues)
            newList.add(clone(league));

        return newList;
    }

    public static PrivateLeague clone(PrivateLeague league)
    {
        if(league == null) return null;
        PrivateLeague newLeague = new PrivateLeague();
        newLeague.setId(league.getId());
        newLeague.setUserId(league.getUserId());
        newLeague.setCode(league.getCode());
        newLeague.setName(league.getName());
        if(league.getTeams() != null)
        {
            for(PrivateLeagueTeam team : league.getTeams())
                newLeague.getTeams().add(clone(team));
        }
        return newLeague;
    }

    public static PrivateLeagueTeam clone(PrivateLeagueTeam team)
    {
        if (team == null) return null;
        PrivateLeagueTeam newTeam = new PrivateLeagueTeam();
        newTeam.setUserId(team.getUserId());
        newTeam.setImageName(team.getImageName());
        newTeam.setRank(team.getRank());
        newTeam.setTotalPoints(team.getTotalPoints());
        newTeam.setUsername(team.getUsername());
        newTeam.setUserTeamId(team.getUserTeamId());
        newTeam.setUserTeamName(team.getUserTeamName());
        return newTeam;
    }

    public static History clone(History history)
    {
        History h = new History();
        if(history == null)
            return h;
        h.setOpposition(history.getOpposition());
        h.setPoints(history.getPoints());
        return h;
    }

    public static Player clone(Player player) {
        Player p = new Player();
        if (player == null)
            return p;
        p.setPointsHistory1(player.getPointsHistory1());
        p.setPointsHistory2(player.getPointsHistory2());
        p.setPointsHistory3(player.getPointsHistory3());
        p.setPointsHistory4(player.getPointsHistory4());
        p.setPointsHistory5(player.getPointsHistory5());
        p.setPoints(player.getPoints());
        p.setDisplayName(player.getDisplayName());
        p.setFirstName(player.getFirstName());
        p.setId(player.getId());
        p.setImageUrl(player.getImageUrl());
        p.setLastName(player.getLastName());
        p.setNextOpponent(player.getNextOpponent());
        p.setPopularity(player.getPopularity());
        p.setPrice(player.getPrice());
        p.setShortTeamName(player.getShortTeamName());
        p.setType(player.getType());
        p.setStatistics(clone(player.getStatistics()));
        return p;
    }

    public static Statistics clone(Statistics statistics) {
        Statistics s = new Statistics();
        if (statistics == null)
            return s;
        s.setEconomy(statistics.getEconomy());
        s.setStrikeRate(statistics.getStrikeRate());
        s.setFifties(statistics.getFifties());
        s.setFiveWickets(statistics.getFiveWickets());
        s.setHundreds(statistics.getHundreds());
        s.setMatchesPlayed(statistics.getMatchesPlayed());
        s.setRunsScored(statistics.getRunsScored());


        s.setThreeWickets(statistics.getThreeWickets());
        s.setWickets(statistics.getWickets());

        return s;
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
            JSONArray playersArray = new JSONArray(playerIds);
            object.put("PlayerIds", playersArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static int getTeamColor(String shortTeamName) {
        if (shortTeamName.equals("DD"))
            return R.color.DD;
        if (shortTeamName.equals("SRH"))
            return R.color.SRH;
        if (shortTeamName.equals("CSK"))
            return R.color.CSK;
        if (shortTeamName.equals("RCB"))
            return R.color.RCB;
        if (shortTeamName.equals("MI"))
            return R.color.MI;
        if (shortTeamName.equals("KXIP"))
            return R.color.KXIP;
        if (shortTeamName.equals("KKR"))
            return R.color.KKR;
        if (shortTeamName.equals("RR"))
            return R.color.RR;

        return R.color.DD;
    }

//    public static int getTeamLogo(String teamName)
//    {
//        if(teamName.equals("KKR"))
//            return R.drawable.kkr;
//        if(teamName.equals("MI"))
//            return R.drawable.mi;
//        if(teamName.equals("SRH"))
//            return R.drawable.srh;
//        if(teamName.equals("KXIP"))
//            return R.drawable.kxip;
//        if(teamName.equals("DD"))
//            return R.drawable.dd;
//        if(teamName.equals("CSK"))
//            return R.drawable.csk;
//        if(teamName.equals("RR"))
//            return R.drawable.rr;
//        if(teamName.equals("RCB"))
//            return R.drawable.rcb;
//
//        return R.drawable.rcb;
//    }


}
