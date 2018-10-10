package com.rahul.locationalarm.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rahul.locationalarm.location.LocationModel;
import com.rahul.locationalarm.login.LoginModel;
import com.rahul.locationalarm.server.ResponseKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ResponseParser {

    private static final String LOGGER_TAG = ResponseParser.class.getSimpleName();

    public static LoginModel getLoginResponse(@NonNull final JSONObject loginResponse) throws JSONException {

        LoginModel result = null;

        final JSONObject statusObject = loginResponse.getJSONObject(ResponseKeys.KEY_STATUS);

        final Object status = statusObject.get(ResponseKeys.KEY_CODE);

        if (status instanceof Integer && HttpURLConnection.HTTP_OK == (int) status) {

            final JSONObject resultObject = loginResponse.getJSONObject(ResponseKeys.KEY_RESULT);

            final int userId = resultObject.getInt(ResponseKeys.KEY_USER_ID);
            final String userName = resultObject.getString(ResponseKeys.KEY_USER_NAME);
            final String role = resultObject.getString(ResponseKeys.KEY_ROLE);
            final String token = resultObject.getString(ResponseKeys.KEY_TOKEN);

            result = new LoginModel(userId, userName, role, token);
        }


        return result;

    }

    public static boolean getLocationSaveResponse(@NonNull final JSONObject locationSaveResponse) throws JSONException {

        final JSONObject statusObject = locationSaveResponse.getJSONObject(ResponseKeys.KEY_STATUS);
        final Object status = statusObject.get(ResponseKeys.KEY_CODE);

        return status != null && status instanceof Integer && HttpURLConnection.HTTP_OK == (int) status;
    }

    public static List<LocationModel> getLocationsResponse(@NonNull final String response) throws JSONException {

        final List<LocationModel> locations = new ArrayList<>();

        final JSONObject responseJSONObject = new JSONObject(response);

        final JSONObject statusObject = responseJSONObject.getJSONObject(ResponseKeys.KEY_STATUS);

        final Object status = statusObject.get(ResponseKeys.KEY_CODE);

        if (status instanceof Integer && HttpURLConnection.HTTP_OK == (int) status) {

            final JSONArray resultArray = responseJSONObject.getJSONArray(ResponseKeys.KEY_RESULT);

            for (int counter = 0; counter < resultArray.length(); counter++) {

                try {

                    final JSONObject locationDetailObj = resultArray.getJSONObject(counter);

                    final int locationId = locationDetailObj.optInt(ResponseKeys.KEY_LOCATION_ID);
                    final String latitude = locationDetailObj.optString(ResponseKeys.KEY_LATITUDE);
                    final String longitude = locationDetailObj.optString(ResponseKeys.KEY_LONGITUDE);

                    if (latitude != null && longitude != null) {
                        locations.add(new LocationModel(locationId, Double.valueOf(latitude), Double.valueOf(longitude)));

                    } else {
                        Log.w(LOGGER_TAG, "Location is not correct");
                    }

                } catch (NumberFormatException ex) {
                    Log.w(LOGGER_TAG, "Location is not correct");
                }
            }

        }

        return locations;
    }


}
