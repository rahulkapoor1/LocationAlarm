package com.rahul.locationalarm.dashboard.newalarms.geofence;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.Splash.SplashActivity;
import com.rahul.locationalarm.dashboard.alarms.AlarmDAOImpl;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;
import com.rahul.locationalarm.helpers.AppSetupManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    public static final int GEOFENCE_NOTIFICATION_ID = 0;

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppSetupManager.getInstance().setupApp(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        final int geoFenceTransition = geofencingEvent.getGeofenceTransition();

        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {

            final List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get notification setting for last geofence
            // It is better to show single notification with Append detail.

            final String alarmId = triggeringGeofences.get(triggeringGeofences.size() - 1).getRequestId();
            final AlarmModel alarmDetail = new AlarmDAOImpl().getAlarmDetail(alarmId);

            if(alarmDetail != null) {
                final String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);
                sendNotification(geofenceTransitionDetails, alarmDetail);
            }

        }
    }


    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {

        final ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER )
            status = getString(R.string.geofence_enter_tile) + " ";
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT )
            status = getString(R.string.geofence_exit_tile) + " ";
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }

    private void sendNotification(@NonNull final String msg, @NonNull final AlarmModel alarmDetail) {
        Log.i(TAG, "sendNotification: " + msg );

        // For now load notification setting for last triggered geofence

        final Intent notificationIntent = new Intent(this, SplashActivity.class);

        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        final PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        assert notificationManager != null;
        notificationManager.notify(GEOFENCE_NOTIFICATION_ID, createNotification(alarmDetail , msg, notificationPendingIntent));
    }

    private Notification createNotification(@NonNull final AlarmModel alarmDetail, @NonNull final String body, PendingIntent notificationPendingIntent) {

        Uri sound = null;

        // Load custom ringtone which was selected by user

        final File soundFile = new File(alarmDetail.getRingtone());
        if(soundFile.exists()) {
            sound = Uri.fromFile(soundFile);
        } else {
            sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        final CharSequence name = getString(R.string.app_name);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int importance = NotificationManager.IMPORTANCE_HIGH;
            final NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(notificationPendingIntent);

        if(alarmDetail.isShouldVibrate()) {
            builder.setVibrate(new long[]{0, 1000, 500, 1000});
        }

        return builder.build();
    }

    private static String getErrorString(int errorCode) {
        return GeofenceStatusCodes.getStatusCodeString(errorCode);
    }
}