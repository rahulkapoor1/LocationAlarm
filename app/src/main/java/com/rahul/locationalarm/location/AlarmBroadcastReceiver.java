package com.rahul.locationalarm.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rahul.locationalarm.Constants;
import com.rahul.locationalarm.helpers.AppSetupManager;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received command to start location tracking");

        AppSetupManager.getInstance().setupApp(context);

        final Bundle extras = intent.getExtras();

        final boolean isIdleMode = (extras != null && extras.containsKey(Constants.KEY_STATE_IDLE))
                && intent.getExtras().getBoolean(Constants.KEY_STATE_IDLE);

        // Re-register alarm for next execute as repeat is not allowed with setExactAndAllowWhileIdle
        LocationHelper.setAlarmService(context, isIdleMode);

        // start background services
        LocationHelper.startBackgroundServices(context);
    }
}
