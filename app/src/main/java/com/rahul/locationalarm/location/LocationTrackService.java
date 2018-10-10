package com.rahul.locationalarm.location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.DBTaskSaveCompletionListener;
import com.rahul.locationalarm.helpers.AppSetupManager;

/**
 * Service responsible to track location of user and to save in a database.
 */
public class LocationTrackService extends Service implements DBTaskSaveCompletionListener {

    private static final String TAG = LocationTrackService.class.getSimpleName();

    // Every 60 seconds
    private static final long LOCATION_UPDATE_INTERVAL = 60 * 1000;

    // Every 30 seconds
    private static final long FASTEST_LOCATION_UPDATE_INTERVAL = 30 * 1000;

    // Every 5 Minutes
    private static final long BATCH_LOCATION_MAX_WAIT_TIME = 60 * 1000 * 5;

    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // To setup database and dagger
        AppSetupManager.getInstance().setupApp(this);

        initLocationRequest();
        initLocationCallback();
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setMaxWaitTime(BATCH_LOCATION_MAX_WAIT_TIME);
    }

    /**
     * To initialize the location change listener
     */
    private void initLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult != null) {
                    Log.d(TAG, "Received location");
                    new LocationSaveAsync(locationResult.getLocations(), LocationTrackService.this).execute();
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        connectLocationClient();

        // To make Service run in background
        return Service.START_STICKY;
    }

    /**
     * To get the regular location updates
     */
    private void connectLocationClient() {

        if (mFusedLocationClient == null) {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // No permission for listening location
                Toast.makeText(this, getString(R.string.allow_fine_location), Toast.LENGTH_SHORT).show();

                return;
            }

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }


    private void stopLocationUpdates() {
        if (mFusedLocationClient != null && mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public void onDBTaskCompleted(boolean isSuccessful) {
        // Database save transaction is completed
        Log.d(TAG, "Is location saved " + isSuccessful);
    }
}
