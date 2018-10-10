package com.rahul.locationalarm.dashboard.alarms;

import com.rahul.locationalarm.common.BasePresenter;

public interface AlarmsPresenter extends BasePresenter {

    void getSavedAlarms();

    void saveAlarmDetail(AlarmModel alarm);

    void deleteAlarmDetail(int alarmId);

    void updateAlarmState(int alarmId, boolean isActive);

}
