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
public class User extends RealmObject {

    public User()
    {
    }

    public User(JSONObject json)
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
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//    private User(Parcel in)
//    {
//        Id = in.readString();
//        FirstName = in.readString();
//        LastName = in.readString();
//    }

}
