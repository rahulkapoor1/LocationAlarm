package com.rahul.locationalarm.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.BuildConfig;
import com.rahul.locationalarm.Constants;

public class LocationHelper {

    /**
     * To start back ground services.
     *
     * @param context Context of Application.
     */
    public static void startBackgroundServices(@NonNull Context context) {

        final Intent serviceIntent = new Intent(context, LocationTrackService.class);
        context.startService(serviceIntent);

        final Intent locationPostIntent = new Intent(context, LocationPostService.class);
        context.startService(locationPostIntent);
    }

    /**
     * To set alarm for executing background services.
     *
     * @param context      Context of Application.
     * @param isDeviceIdle Idle state boolean which will use to alter delay in starting services.
     */
    public static void setAlarmService(@NonNull Context context, boolean isDeviceIdle) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(Constants.KEY_STATE_IDLE, isDeviceIdle);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {

            // in case of debug mode, location tracking should work in every 1 minute
            final long normalDelay = BuildConfig.DEBUG ? Constants.FAST_LOCATION_TRACK_DELAY : Constants.LOCATION_TRACK_DELAY;

            final long delay = System.currentTimeMillis() +
                    (isDeviceIdle ? Constants.LOCATION_TRACK_LONG_DELAY : normalDelay);

            // to handle Doze mode
            // Link - https://hashedin.com/blog/save-your-android-service-from-doze-mode/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        delay, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
            }
        }
    }
}
