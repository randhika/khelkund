package com.appacitive.khelkund.model;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 4/14/2015.
 */
@RealmClass
public class PrivateLeague extends RealmObject {

    public PrivateLeague()
    {
        this.Teams = new RealmList<PrivateLeagueTeam>();
    }

    public PrivateLeague(JSONObject json)
    {
        if(json == null)
            return;
        this.Id = json.optString("PrivateLeagueId");
        this.Name = json.optString("PrivateLeagueName");
        this.Code = json.optString("PrivateLeagueCode");
        this.Teams = new RealmList<PrivateLeagueTeam>();
        JSONArray teamsArray = json.optJSONArray("UserTeams");
        if(teamsArray != null)
        {
            for(int i = 0; i<teamsArray.length(); i++)
            {
                this.Teams.add(new PrivateLeagueTeam(teamsArray.optJSONObject(i)));
            }
        }
    }

    @Index
    private String UserId;

    @PrimaryKey
    private String Id;

    private String Name;

    private String Code;

    private RealmList<PrivateLeagueTeam> Teams;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public RealmList<PrivateLeagueTeam> getTeams() {
        return Teams;
    }

    public void setTeams(RealmList<PrivateLeagueTeam> teams) {
        Teams = teams;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
