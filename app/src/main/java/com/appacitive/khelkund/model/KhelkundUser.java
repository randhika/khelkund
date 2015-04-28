package com.appacitive.khelkund.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 3/24/2015.
 */

@RealmClass
public class KhelkundUser extends RealmObject {

    public KhelkundUser()
    {
    }

    public KhelkundUser(JSONObject json)
    {
        if(json == null)
            return;
        this.Id = json.optString("UserId");
        this.FirstName = json.optString("FirstName");
        this.LastName = json.optString("LastName");
        this.ReferralCode = json.optString("ReferralCode");

    }

    @PrimaryKey
    private String Id;
    private String FirstName;
    private String LastName;
    private String ReferralCode;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }
}
