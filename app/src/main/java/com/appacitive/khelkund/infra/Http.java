package com.appacitive.khelkund.infra;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley on 3/24/2015.
 */
public class Http {

    public Http(Context cxt) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(cxt.getApplicationContext());
    }

    private static RequestQueue requestQueue = null;

    private static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void get(String url, final Map<String, String> headers, final APCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null)
                    callback.success((response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>((headers));
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        getRequestQueue().add(request);
    }


    public void delete(String url, final Map<String, String> headers, final APCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null)
                    callback.success((response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>((headers));
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        getRequestQueue().add(request);
    }


    public void put(String url, final Map<String, String> headers, final JSONObject payload, final APCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null)
                    callback.success((response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
                return new HashMap<String, String>((headers));
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        getRequestQueue().add(request);
    }


    public void post(String url, final Map<String, String> headers, final JSONObject payload, final APCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null)
                    callback.success((response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.failure(error);
            }
        }
        ) {
            @Override
            public HashMap<String, String> getHeaders() {
//                headers.put("Content-Type", "application/json; charset=utf-8");
                return new HashMap<String, String>((headers));
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        getRequestQueue().add(request);
    }
}
