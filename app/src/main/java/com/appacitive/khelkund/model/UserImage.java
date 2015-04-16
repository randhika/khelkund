package com.appacitive.khelkund.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by sathley on 4/16/2015.
 */

@RealmClass
public class UserImage extends RealmObject {

    public UserImage()
    {

    }

    @PrimaryKey
    private String UserId;

    private byte[] Image;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
