package com.rahul.locationalarm.dashboard.alarms;

import com.rahul.locationalarm.common.BaseViewInterface;

import java.util.List;

public interface AlarmView extends BaseViewInterface {

    void onSavedAlarmsReceived(List<AlarmModel> alarms);

}
