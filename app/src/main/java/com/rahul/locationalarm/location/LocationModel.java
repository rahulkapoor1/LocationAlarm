package com.rahul.locationalarm.location;

import com.google.android.gms.maps.model.LatLng;

public class LocationModel {

    private final int mLocationId;

    private final double mLatitude;

    private final double mLongitude;


    public LocationModel(final int locationId, final double latitude, final double longitude) {
        this.mLocationId = locationId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public int getAlarmId() {
        return mLocationId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public LatLng getLatLng() {
        // computed property to avoid extra for loops
        return new LatLng(mLatitude, mLongitude);
    }
}
