package com.rahul.locationalarm.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.rahul.locationalarm.location.LocationHelper;

/**
 * To detect idle state of Device.
 */
public class DeviceIdleReceiver extends BroadcastReceiver {

    private static final String LOGGER_TAG = DeviceIdleReceiver.class.getSimpleName();

    private static final String ACTION_IDLE_MODE_CHANGED = "android.os.action.DEVICE_IDLE_MODE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {

        final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (intent.getAction() != null && intent.getAction().equals(ACTION_IDLE_MODE_CHANGED)) {

                if (powerManager != null) {
                    final boolean isDozeMode = powerManager.isDeviceIdleMode();

                    Log.d(LOGGER_TAG, "Is Doze Mode" + isDozeMode);

                    LocationHelper.setAlarmService(context, isDozeMode);
                }

            }
        }

    }
}
