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

    }

    @PrimaryKey
    private String Id;
    private String FirstName;
    private String LastName;

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

//    @Override
//    public int describeContents() {
//        return this.hashCode();
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(Id);
//        parcel.writeString(FirstName);
//        parcel.writeString(LastName);
//    }
//    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
//        public KhelkundUser createFromParcel(Parcel in) {
//            return new KhelkundUser(in);
//        }
//        public KhelkundUser[] newArray(int size) {
//            return new KhelkundUser[size];
//        }
//    };
//
//    private KhelkundUser(Parcel in)
//    {
//        Id = in.readString();
//        FirstName = in.readString();
//        LastName = in.readString();
//    }

}
