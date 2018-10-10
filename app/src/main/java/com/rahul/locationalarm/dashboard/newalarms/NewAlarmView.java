package com.rahul.locationalarm.dashboard.newalarms;

import com.rahul.locationalarm.common.BaseViewInterface;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;

public interface NewAlarmView extends BaseViewInterface {

    void onAlarmDetailSaved(AlarmModel alarm);

    void onReceivedErrorInSavingAlarm(int messageResId);

}
