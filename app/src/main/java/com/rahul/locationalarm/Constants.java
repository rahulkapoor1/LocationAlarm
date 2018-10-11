package com.rahul.locationalarm;

public class Constants {

    public static final int GEOFENCE_RADIUS_IN_METERS = 100;

    // Track location in every 5 Minutes
    public static final int LOCATION_TRACK_DELAY = 5 * 60 * 1000;


    // Track location in every 10 Minutes
    // In case of doze mode, wake up alarms after 9 Minutes. So better to take 1 Minute extra
    public static final int LOCATION_TRACK_LONG_DELAY = 10 * 60 * 1000;

    // To find out doze mode and to to alter delay of timer
    public static final String KEY_STATE_IDLE = "key_idle";

}
