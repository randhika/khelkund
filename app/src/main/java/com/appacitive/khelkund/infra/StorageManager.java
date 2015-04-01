package com.appacitive.khelkund.infra;

import com.appacitive.khelkund.model.KhelkundUser;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sathley on 3/24/2015.
 */
public class StorageManager {

    public void SaveTeam(Player player)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(player);
        realm.commitTransaction();
    }

    public void SaveUser(KhelkundUser user)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public void SavePlayers(List<Player> players)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(players);
        realm.commitTransaction();
    }

    public void SaveTeam(Team team)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(team);
        realm.commitTransaction();
    }

    public Team GetTeam(String userId)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Team> query = realm.where(Team.class);
        query.equalTo("UserId", userId);
        return query.findFirst();
    }

    public RealmResults<Player> GetAllPlayers(String type)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        return query.findAll();
    }

    public KhelkundUser GetUser(String userId)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<KhelkundUser> query = realm.where(KhelkundUser.class);
        query.equalTo("Id", userId);
        return query.findFirst();
    }

    public RealmResults<Player> GetTopPerformingPlayersByType(String type) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        return query.findAllSorted("Points", false);
    }

    public RealmResults<Player> GetBargainPlayersByType(String type) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Type", type);
        query.lessThan("Price", 10000000 / 11);
        return query.findAllSorted("Points", false);
    }

    public Player GetPlayer(String playerId) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Player> query = realm.where(Player.class);
        query.equalTo("Id", playerId);
        return query.findFirst();
    }

    public void deleteUser(String userId) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.allObjects(KhelkundUser.class).clear();
        realm.allObjects(Team.class).clear();
        realm.commitTransaction();
    }

    public void SaveMatches(List<Match> matchList) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(matchList);
        realm.commitTransaction();
    }

    public List<Match> GetAllMatches() {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Match> query = realm.where(Match.class);
        return query.findAll();
    }

    public Match GetMatch(String matchId) {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<Match> query = realm.where(Match.class);
        query.equalTo("Id", matchId);
        return query.findFirst();
    }
}
