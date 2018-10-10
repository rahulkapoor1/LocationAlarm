package com.rahul.locationalarm.helpers;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/*
 * Request helper class to do GET/POST on URLs.
 */
public class VolleyRequestHelper {

    private final RequestQueue mRequestQueue;

    public VolleyRequestHelper(@NonNull RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
    }

    /**
     * Method to send get request.
     *
     * @param url              Url of request.
     * @param params           Request parameters.
     * @param responseListener To receive callback of response.
     * @param errorListener    To receive callback of error.
     */
    public void getResponse(@NonNull final String url, @NonNull final Map<String, String> params,
                            @NonNull final Response.Listener<JSONObject> responseListener,
                            @NonNull final Response.ErrorListener errorListener) {

        final JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params), responseListener, errorListener);
        mRequestQueue.add(request);
    }

    public void postLocations(@NonNull final String url,
                              @NonNull final String token,
                              @NonNull final String requestBody,
                              @NonNull final Response.Listener<String> responseListener,
                              @NonNull final Response.ErrorListener errorListener) {

        final StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeader(token);
            }
        };

        mRequestQueue.add(request);
    }

    public void getSavedLocations(@NonNull final String url,
                                  @NonNull final String token,
                                  @NonNull final Response.Listener<String> responseListener,
                                  @NonNull final Response.ErrorListener errorListener) {

        final StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getRequestHeader(token);
            }
        };

        mRequestQueue.add(request);
    }

    private Map<String, String> getRequestHeader(@NonNull final String token) {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Accept", "application/json; charset=utf-8");
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

}
