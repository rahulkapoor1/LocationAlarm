package com.rahul.locationalarm.location;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.Constants;

public class LocationHelper {

    /**
     * To start back ground services.
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
     * @param context Context of Application.
     */
    public static void setAlarmService(@NonNull Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            // to handle Doze mode
            // Link - https://hashedin.com/blog/save-your-android-service-from-doze-mode/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        Constants.LOCATION_TRACK_DELAY, pendingIntent);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Constants.LOCATION_TRACK_DELAY, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, Constants.LOCATION_TRACK_DELAY, pendingIntent);
            }
        }
    }
}
