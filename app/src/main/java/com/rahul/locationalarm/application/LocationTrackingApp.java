package com.rahul.locationalarm.application;

import android.app.Application;

import com.rahul.locationalarm.helpers.AppSetupManager;

public class LocationTrackingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppSetupManager.getInstance().setupApp(this);
    }


}
