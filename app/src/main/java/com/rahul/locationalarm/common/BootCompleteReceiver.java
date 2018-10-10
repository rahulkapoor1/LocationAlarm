package com.rahul.locationalarm.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rahul.locationalarm.dashboard.newalarms.geofence.GeofenceManager;
import com.rahul.locationalarm.helpers.AppSetupManager;
import com.rahul.locationalarm.location.LocationHelper;

public class BootCompleteReceiver extends BroadcastReceiver {

    private final static String TAG = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        AppSetupManager.getInstance().setupApp(context);
        // Re register geofence if needed
        new GeofenceManager(context).registerGeofenceIfNeeded();

        LocationHelper.startBackgroundServices(context);
    }
}
