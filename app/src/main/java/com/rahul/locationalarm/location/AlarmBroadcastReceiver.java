package com.rahul.locationalarm.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rahul.locationalarm.helpers.AppSetupManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received command to start location tracking");

        AppSetupManager.getInstance().setupApp(context);

        // Re-register alarm for next execute as repeat is not allowed with setExactAndAllowWhileIdle
        LocationHelper.setAlarmService(context);

        // start background services
        LocationHelper.startBackgroundServices(context);
    }
}
