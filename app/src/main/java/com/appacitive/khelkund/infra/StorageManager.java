package com.appacitive.khelkund.infra;

import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.Team;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sathley on 3/24/2015.
 */
public class StorageManager {

    private Realm getInstance()
    {
        return Realm.getInstance(KhelkundApplication.getAppContext(), "db-v2.realm");
    }

    public void reset() {
        Realm.deleteRealmFile(KhelkundApplication.getAppContext());
    }

    public List<PrivateLeague> GetAllPrivateLeaguesForUser(String id)
    {
        Realm realm = getInstance();
        RealmQuery<PrivateLeague> query = realm.where(PrivateLeague.class);
        query.equalTo("UserId", id);
        return query.findAll();
    }

    public PrivateLeague GetPrivateLeague(String leagueId)
    {
        Realm realm = getInstance();
        RealmQuery<PrivateLeague> query = realm.where(PrivateLeague.class);
        query.equalTo("Id", leagueId);
        return query.findFirst();
    }

    public void SavePrivateLeagues(List<PrivateLeague> privateLeagues)
    {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(privateLeagues);
        realm.commitTransaction();
        realm.close();
    }

    public void SavePrivateLeague(PrivateLeague privateLeague)
    {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(privateLeague);
        realm.commitTransaction();
        realm.close();
    }

    public void SaveTeam(Player player) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(player);
        realm.commitTransaction();
        realm.close();
    }

    public void SaveUser(KhelkundUser user) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
        realm.close();
    }

    public void SavePlayers(List<Player> players) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(players);
        realm.commitTransaction();
        realm.close();
    }

    public void SaveTeam(Team team) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(team);
        realm.commitTransaction();
        realm.close();
    }

    public Team GetTeam(String userId) {
        Realm realm = getInstance();
        RealmQuery<Team> query = realm.where(Team.class);
        query.equalTo("UserId", userId);
        return query.findFirst();
    }

    public RealmResults<Player> GetAllPlayers(String type) {
        Realm realm = getInstance();
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        return query.findAll();
    }

    public KhelkundUser GetUser(String userId) {
        Realm realm = getInstance();
        RealmQuery<KhelkundUser> query = realm.where(KhelkundUser.class);
        query.equalTo("Id", userId);
        return query.findFirst();
    }

    public RealmResults<Player> GetTopPerformingPlayersByType(String type) {
        Realm realm = getInstance();
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        return query.findAllSorted("Points", false);
    }

    public RealmResults<Player> GetBargainPlayersByType(String type) {
        Realm realm = getInstance();
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        query.lessThan("Price", 10000000 / 11);
        return query.findAllSorted("Points", false);
    }

    public Player GetPlayer(String playerId) {
        Realm realm = getInstance();
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Id", playerId);
        return query.findFirst();
    }

    public void deleteUser(String userId) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.allObjects(KhelkundUser.class).clear();
        realm.allObjects(Team.class).clear();
        realm.commitTransaction();
        realm.close();
    }

    public void SaveMatches(List<Match> matchList) {
        Realm realm = getInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(matchList);
        realm.commitTransaction();
        realm.close();
    }

    public List<Match> GetAllMatches() {
        Realm realm = getInstance();
        RealmQuery<Match> query = realm.where(Match.class);
        return query.findAll();
    }

    public Match GetMatch(String matchId) {
        Realm realm = getInstance();
        RealmQuery<Match> query = realm.where(Match.class);
        query.equalTo("Id", matchId);
        return query.findFirst();
    }

    public String getNextMatchId(String matchId) {
        Realm realm = getInstance();
        RealmQuery<Match> query = realm.where(Match.class);
        query.equalTo("Id", matchId);
        Match currentMatch = query.findFirst();

        RealmQuery<Match> query1 = realm.where(Match.class);
        query1.equalTo("MatchNumber", currentMatch.getMatchNumber() + 1);
        Match nextMatch = query1.findFirst();
        if (nextMatch == null)
            return null;
        return nextMatch.getId();
    }

    public String getPreviousMatchId(String matchId) {
        Realm realm = getInstance();
        RealmQuery<Match> query = realm.where(Match.class);
        query.equalTo("Id", matchId);
        Match currentMatch = query.findFirst();

        RealmQuery<Match> query1 = realm.where(Match.class);
        query1.equalTo("MatchNumber", currentMatch.getMatchNumber() - 1);
        Match previousMatch = query1.findFirst();
        if (previousMatch == null)
            return null;
        return previousMatch.getId();
    }
}