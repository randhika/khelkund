package com.appacitive.khelkund.infra;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathley on 3/24/2015.
 */
public class Http {

    public Http() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(KhelkundApplication.getAppContext());
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
                Crashlytics.logException(error);
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
                Crashlytics.logException(error);
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

    private static String processResponse(String response) {
        return response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
    }

    public void put(String url, final HashMap<String, String> headers, final String payload, final APCallback callback) {
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callback != null)
                    try {
                        callback.success(new JSONObject(processResponse(response)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return payload.getBytes();
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
                Crashlytics.logException(error);
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
                Crashlytics.logException(error);
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
