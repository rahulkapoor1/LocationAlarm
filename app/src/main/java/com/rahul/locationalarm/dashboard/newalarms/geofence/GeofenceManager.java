package com.rahul.locationalarm.dashboard.newalarms.geofence;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rahul.locationalarm.common.DBTaskFetchCompletionListener;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;
import com.rahul.locationalarm.dashboard.alarms.SavedAlarmsFetchAsync;
import com.rahul.locationalarm.login.LoginModel;
import com.rahul.locationalarm.login.UserDetailAsync;
import com.rahul.locationalarm.login.UserDetailCallBack;

import java.util.List;

/**
 * Class responsible to re-register geofences if needed.
 */
public class GeofenceManager implements UserDetailCallBack, DBTaskFetchCompletionListener {

    private final static String TAG = GeofenceManager.class.getSimpleName();

    private final Context mContext;

    public GeofenceManager(@NonNull final Context context) {
        this.mContext = context;
    }

    public void registerGeofenceIfNeeded() {
        new UserDetailAsync(this).execute();
    }

    @Override
    public void onUserDetailExist(LoginModel detail) {
        Log.d(TAG, "User detail exist");

        // Get saved alarms
        new SavedAlarmsFetchAsync(this).execute();

    }

    @Override
    public void onUserDetailUnavailable() {
        Log.d(TAG, "User detail not exist");
    }

    @Override
    public void onDBTaskCompleted(List<?> items) {
        new GeofenceHelper().registerForGeofence(mContext, (List<AlarmModel>) items);
    }
}
