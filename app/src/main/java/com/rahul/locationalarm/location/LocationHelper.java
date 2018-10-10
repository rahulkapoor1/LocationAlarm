package com.rahul.locationalarm.location;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

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
}
