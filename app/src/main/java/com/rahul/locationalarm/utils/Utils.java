package com.rahul.locationalarm.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.content.Context.POWER_SERVICE;

public class Utils {

    public static boolean checkPermissionForReadExternalStorage(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissionForReadExtertalStorage(final Context context, final int requestCode) {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean checkLocationPermission(final Context context) {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    public static void requestPermissionForLocation(final Context context, final int requestCode) {
        ActivityCompat.requestPermissions(
                (Activity) context,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                requestCode
        );
    }

    public static void requestLowBatteryOptimizationPermission(final  Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
            if (pm!= null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }
    }

    public static String getNameOfRingtone(@NonNull final String ringtonePath) {
        return ringtonePath.substring(ringtonePath.lastIndexOf("/") + 1, ringtonePath.length());
    }

}
