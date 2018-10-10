package com.rahul.locationalarm.dashboard.alarms;

import java.util.List;

public interface AlarmDAO {

    String TABLE_NAME = "Alarm";

    String COL_ALARM_ID = "alarm_id";

    String COL_NAME = "name";

    String COL_VIBRATION = "vibration";

    String COL_ACTIVE = "active";

    String COL_RINGTONE = "ringtone";

    String COL_LATITUDE = "latitude";

    String COL_LONGITUDE = "longitude";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT,"
            + COL_RINGTONE + " TEXT,"
            + COL_VIBRATION + " INTEGER,"
            + COL_ACTIVE + " INTEGER,"
            + COL_LATITUDE + " REAL,"
            + COL_LONGITUDE + " REAL "
            + ")";

    int saveAlarmDetail(AlarmModel alarmModel);

    List<AlarmModel> getAlarmDetails();

    boolean deleteAlarmDetail(int alarmId);

    boolean updateAlarmState(int alarmId, boolean isActive);

    /**
     * To get the alarm detail. Method will get called from Notification.
     * @param alarmId Id of alarm which needs to fetch.
     * @return Detail of alarm.
     */
    AlarmModel getAlarmDetail(String alarmId);

}
