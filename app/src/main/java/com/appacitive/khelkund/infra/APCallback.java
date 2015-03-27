package com.appacitive.khelkund.infra;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by sathley on 3/24/2015.
 */
public abstract class APCallback implements Serializable {

    public void success(JSONObject result) {
    }

    public void failure(Exception e) {
    }
}
