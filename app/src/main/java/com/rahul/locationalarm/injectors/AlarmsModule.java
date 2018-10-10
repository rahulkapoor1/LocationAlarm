package com.rahul.locationalarm.injectors;

import com.rahul.locationalarm.dashboard.alarms.AlarmsPresenter;
import com.rahul.locationalarm.dashboard.alarms.AlarmsPresenterImpl;
import com.rahul.locationalarm.dashboard.newalarms.geofence.GeofenceHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class AlarmsModule {

    @Provides
    public AlarmsPresenter provideAlarmPresenter() {
        return new AlarmsPresenterImpl();
    }

    @Provides
    public GeofenceHelper provideGeofenceHelper() {
        return new GeofenceHelper();
    }

}
