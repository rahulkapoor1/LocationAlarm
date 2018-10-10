package com.rahul.locationalarm.location;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.URLs;
import com.rahul.locationalarm.helpers.VolleyRequestHelper;
import com.rahul.locationalarm.injectors.ComponentHolder;
import com.rahul.locationalarm.login.LoginModel;
import com.rahul.locationalarm.login.UserDetailAsync;
import com.rahul.locationalarm.login.UserDetailCallBack;
import com.rahul.locationalarm.utils.ResponseParser;

import org.json.JSONException;

import javax.inject.Inject;

public class SavedLocationPresenterImpl implements SavedLocationPresenter, UserDetailCallBack {

    private static final String LOGGER_TAG = SavedLocationPresenterImpl.class.getSimpleName();

    private SavedLocationView mView;

    @Inject
    VolleyRequestHelper mRequestHelper;

    SavedLocationPresenterImpl() {
        // Dependency is injected which is useful while writing unit tests
        ComponentHolder.getInstance().getApplicationComponent().inject(this);
    }

    public void attachView(@NonNull final SavedLocationView view) {
        mView = view;
    }

    @Override
    public void getSavedLocations() {

        new UserDetailAsync(this).execute();

    }

    public void detachView() {
        mView = null;
    }

    @Override
    public void onUserDetailExist(LoginModel detail) {

        mRequestHelper.getSavedLocations(URLs.GET_LOCATIONS_URL, detail.getToken(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(LOGGER_TAG, "Received locations");

                if (mView != null) {
                    try {
                        mView.onSavedLocationsReceived(ResponseParser.getLocationsResponse(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mView.onReceivedErrorInGettingLocations(R.string.error_fetching_locations);
                        Log.d(LOGGER_TAG, "Received locations");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOGGER_TAG, "Unable to receive locations");
                mView.onReceivedErrorInGettingLocations(R.string.error_fetching_locations);
            }
        });

    }

    @Override
    public void onUserDetailUnavailable() {
        // unrealistic case
        Log.w(LOGGER_TAG, "User detail is unavailable");
        mView.onReceivedErrorInGettingLocations(R.string.error_fetching_locations);
    }
}
