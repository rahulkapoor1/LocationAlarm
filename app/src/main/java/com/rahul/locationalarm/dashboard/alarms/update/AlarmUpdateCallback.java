package com.rahul.locationalarm.dashboard.alarms.update;

public interface AlarmUpdateCallback {

    void onDeleteAlarm(int positionOfAlarm);

    void onUpdateAlarmState(int positionOfAlarm, boolean isActivated);

}
