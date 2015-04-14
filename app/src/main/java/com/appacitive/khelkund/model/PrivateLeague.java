package com.appacitive.khelkund.model;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 4/14/2015.
 */
@RealmClass
public class PrivateLeague extends RealmObject {

    public PrivateLeague()
    {

    }

    public PrivateLeague(JSONObject json)
    {
        if(json == null)
            return;
        this.Id = json.optString("PrivateLeagueId");
        this.Id = json.optString("PrivateLeagueName");
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

    @PrimaryKey
    private String Id;

    private String Name;

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
}
