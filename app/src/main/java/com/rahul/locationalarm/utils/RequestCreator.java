package com.rahul.locationalarm.utils;

import android.support.annotation.NonNull;

import com.rahul.locationalarm.location.LocationModel;
import com.rahul.locationalarm.server.RequestKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestCreator {

    public static Map<String, String> getLoginParams(@NonNull final String username, @NonNull final String password) {
        final Map<String, String> params = new HashMap<>(2);
        params.put(RequestKeys.KEY_MOBILE, username);
        params.put(RequestKeys.KEY_PASSWORD, password);
        return params;
    }

    public static String getBodyForLocationSave(@NonNull final List<LocationModel> locations) throws JSONException {

        final JSONArray locationArray = new JSONArray();

        for(LocationModel location : locations) {

            final JSONObject locationObject = new JSONObject();
            locationObject.put(RequestKeys.KEY_LATITUDE, String.valueOf(location.getLatitude()));
            locationObject.put(RequestKeys.KEY_LONGITUDE, String.valueOf(location.getLongitude()));

            locationArray.put(locationObject);
        }

        return locationArray.toString();
    }

}
