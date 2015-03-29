package com.appacitive.khelkund.infra;

import android.app.Application;

import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.Team;
import com.appacitive.khelkund.model.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sathley on 3/24/2015.
 */
public class StorageManager {

    public void Save(Player player)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(player);
        realm.commitTransaction();
    }

    public void Save(User user)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public void Save(List<Player> players)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(players);
        realm.commitTransaction();
    }

    public void Save(Team team)
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

    public User GetUser(String userId)
    {
        Realm realm = Realm.getInstance(KhelkundApplication.getAppContext());
        RealmQuery<User> query = realm.where(User.class);
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
}
