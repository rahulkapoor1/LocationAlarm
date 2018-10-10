package com.rahul.locationalarm.injectors;

import com.rahul.locationalarm.dashboard.alarms.AlarmsActivity;
import com.rahul.locationalarm.dashboard.newalarms.NewAlarmActivity;

import dagger.Component;

@Component(modules = AlarmsModule.class)
public interface AlarmsComponent {

    void inject(AlarmsActivity target);

    void inject(NewAlarmActivity target);

}
