package com.rahul.locationalarm.location;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahul.locationalarm.URLs;
import com.rahul.locationalarm.helpers.AppSetupManager;
import com.rahul.locationalarm.helpers.VolleyRequestHelper;
import com.rahul.locationalarm.injectors.ComponentHolder;
import com.rahul.locationalarm.login.LoginDAOImpl;
import com.rahul.locationalarm.login.LoginModel;
import com.rahul.locationalarm.utils.RequestCreator;
import com.rahul.locationalarm.utils.ResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

/**
 * To post un-synced saved locations on server from background and to delete synced locations from database.
 */
public class LocationPostService extends IntentService {

    private static final String TAG = LocationPostService.class.getSimpleName();

    private final int INVALID_USER_ID = 0;

    @Inject
    VolleyRequestHelper mRequestHelper;


    public LocationPostService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // To setup database and dagger
        AppSetupManager.getInstance().setupApp(this);
        ComponentHolder.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // It is a background thread so no need to create separate threads or async tasks
        try {
            postLocations();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Unable to post data due to JSON exception");
            stopSelf();
        }
    }

    /**
     * To post locations on server.
     */
    private void postLocations() throws JSONException {
        // First fetch un-synced locations for current user
        final LoginModel loginDetail = new LoginDAOImpl().getUserDetail();

        if (loginDetail != null) {

            final int userId = loginDetail.getUserId();

            if (userId != INVALID_USER_ID) {

                final LocationDAOImpl locationDAO = new LocationDAOImpl();

                final List<LocationModel> locations = locationDAO.getNonSyncedLocationDetails(userId);

                final int nonSyncedLocationsCount = locations.size();

                if (nonSyncedLocationsCount > 0) {

                    mRequestHelper.postLocations(URLs.SAVE_LOCATION_URL, loginDetail.getToken(),
                            RequestCreator.getBodyForLocationSave(locations),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String  response) {

                                    try {

                                        final JSONObject jsonObject = new JSONObject(response);

                                        if (ResponseParser.getLocationSaveResponse(jsonObject)) {

                                            // Delete synced locations
                                            final int[] locationsToDelete = new int[nonSyncedLocationsCount];

                                            for (int locationCounter = 0; locationCounter < nonSyncedLocationsCount; locationCounter++) {
                                                locationsToDelete[locationCounter] = locations.get(locationCounter).getAlarmId();
                                            }

                                            locationDAO.deleteLocationDetails(userId, locationsToDelete);
                                        } else {
                                            showMessage("unable to save details");
                                            Log.d(TAG, "Unable to save details");
                                            stopSelf();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "Unable to save details");
                                        stopSelf();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "Unable to save details");
                                    stopSelf();
                                }
                            });

                } else {
                    showMessage("All Data Synced with server");
                    Log.d(TAG, "All details are synced up");
                    stopSelf();
                }

            } else {
                Log.d(TAG, "Login detail not found");
                stopSelf();
            }
        } else {
            Log.d(TAG, "Login detail not found");
            stopSelf();

        }
    }

    private void showMessage(@NonNull final String message) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
