package com.rahul.locationalarm.dashboard.newalarms.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rahul.locationalarm.Constants;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;

import java.util.ArrayList;
import java.util.List;


public class GeofenceHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GeofenceHelper.class.getSimpleName();

    private static final int NO_INITIAL_TRIGGER = 0;

    private PendingIntent mPendingIntent;

    private GoogleApiClient mGoogleApiClient;

    private Context mContext;

    private AlarmModel mAlarmModel;

    private boolean mIsRemoving;

    private void initHelper(@NonNull final Context context, @NonNull final AlarmModel alarmDetail) {
        this.mAlarmModel = alarmDetail;
        this.mContext = context;
    }

    /**
     * To re register notification.
     * @param context {@link Context}
     * @param alarmDetails List of alarms
     */
    public void registerForGeofence(@NonNull final Context context, @NonNull final List<AlarmModel> alarmDetails) {

        mIsRemoving = false;

        for (AlarmModel alarmDetail : alarmDetails) {

            initHelper(context, alarmDetail);

            final GoogleApiClient apiClient = getGoogleApiClient(context);

            // if client is not connected only then connect
            if (!apiClient.isConnected() && !apiClient.isConnecting()) {
                apiClient.connect();
            } else {
                addGeofence(context, alarmDetail);
            }
        }

    }


    /**
     * To register notification.
     * @param context {@link Context}
     * @param alarmDetail Alarms
     */
    public void registerForGeofence(@NonNull final Context context, @NonNull final AlarmModel alarmDetail) {

        mIsRemoving = false;

        initHelper(context, alarmDetail);

        final GoogleApiClient apiClient = getGoogleApiClient(context);

        // if client is not connected only then connect
        if (!apiClient.isConnected() && !apiClient.isConnecting()) {
            apiClient.connect();
        } else {
            addGeofence(context, alarmDetail);
        }
    }

    public void removeGeofence(@NonNull final Context context, @NonNull final AlarmModel alarmDetail) {

        mIsRemoving = true;

        initHelper(context, alarmDetail);

        final GoogleApiClient apiClient = getGoogleApiClient(context);

        // if client is not connected only then connect
        if (!apiClient.isConnected() && !apiClient.isConnecting()) {
            apiClient.connect();
        } else {
            removeGeofenceFromApiClient();
        }
    }

    private void addGeofence(@NonNull final Context context, @NonNull final AlarmModel alarmDetail) {
        final Geofence geofence = new Geofence.Builder()
                .setRequestId(String.valueOf(alarmDetail.getId()))
                .setCircularRegion(alarmDetail.getLatitude(), alarmDetail.getLongitude(), Constants.GEOFENCE_RADIUS_IN_METERS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        final GeofencingRequest request = new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(NO_INITIAL_TRIGGER)
                .build();


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getGeofencingClient(context).addGeofences(request, getPendingIntent(context))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to register Geofence");
                        Toast.makeText(context, "Failed to register Geofence", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully registered Geofence");
                        Toast.makeText(context, "Successfully registered Geofence", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void removeGeofenceFromApiClient() {
        final List<String> geofencesToRemove = new ArrayList<>();
        geofencesToRemove.add(String.valueOf(mAlarmModel.getId()));
        LocationServices.getGeofencingClient(mContext).removeGeofences(geofencesToRemove)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Removed Geofence");
                        Toast.makeText(mContext, "Removed Geofence", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to Remove Geofence");
                Toast.makeText(mContext, "Failed to Remove Geofence", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private GoogleApiClient getGoogleApiClient(@NonNull Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return mGoogleApiClient;
    }

    private PendingIntent getPendingIntent(@NonNull Context context) {

        if (mPendingIntent == null) {
            final Intent serviceIntent = new Intent(context, GeofenceTransitionsIntentService.class);
            mPendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);

        }
        return mPendingIntent;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mIsRemoving) {
            removeGeofenceFromApiClient();
        } else {
            addGeofence(mContext, mAlarmModel);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void disconnectApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

}
