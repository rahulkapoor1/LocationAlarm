package com.rahul.locationalarm.dashboard.alarms;

import android.text.TextUtils;
import android.util.Log;

import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseViewInterface;
import com.rahul.locationalarm.common.DBTaskFetchCompletionListener;
import com.rahul.locationalarm.common.DBTaskUpdateCompletionListener;
import com.rahul.locationalarm.dashboard.alarms.update.AlarmUpdateAsync;
import com.rahul.locationalarm.dashboard.alarms.update.UpdateType;
import com.rahul.locationalarm.dashboard.newalarms.AlarmSaveCallback;
import com.rahul.locationalarm.dashboard.newalarms.NewAlarmCreateAsync;
import com.rahul.locationalarm.dashboard.newalarms.NewAlarmView;

import java.util.List;

public class AlarmsPresenterImpl implements AlarmsPresenter {

    public static final String TAG = AlarmsPresenterImpl.class.getSimpleName();

    private BaseViewInterface mView;

    @Override
    public void attachView(BaseViewInterface view) {
        mView = view;
    }

    @Override
    public void getSavedAlarms() {
        mView.showProgressBar();
        // To get detail of alarm. Database manager is injected so no worry in terms of internal dependency
        new SavedAlarmsFetchAsync(new DBTaskFetchCompletionListener() {
            @Override
            public void onDBTaskCompleted(List<?> items) {
                mView.hideProgressBar();
                ((AlarmView) mView).onSavedAlarmsReceived((List<AlarmModel>) items);
            }
        }).execute();
    }

    @Override
    public void saveAlarmDetail(final AlarmModel alarm) {

        final NewAlarmView view = ((NewAlarmView) mView);

        view.showProgressBar();

        final String alarmName = alarm.getName().trim();

        if (TextUtils.isEmpty(alarmName)) {
            view.onReceivedErrorInSavingAlarm(R.string.error_alarm_name);

        } else if (alarm.getRingtone() == null) {
            view.onReceivedErrorInSavingAlarm(R.string.error_alarm_ringtone);

        } else if (alarm.getLatitude() == 0 || alarm.getLongitude() == 0) {
            view.onReceivedErrorInSavingAlarm(R.string.error_location);

        } else {

            view.showProgressBar();
            // To save detail of new alarm. Database manager is injected so no worry in terms of internal dependency
            new NewAlarmCreateAsync(alarm, new AlarmSaveCallback() {
                @Override
                public void onAlarmSave(int rowId) {

                    mView.hideProgressBar();

                    final AlarmModel savedAlarm = new AlarmModel(rowId, alarm.getName(), alarm.getRingtone(), alarm.isShouldVibrate(),
                            alarm.isActive(), alarm.getLatitude(), alarm.getLongitude());
                    ((NewAlarmView) mView).onAlarmDetailSaved(savedAlarm);
                }
            }).execute();

        }

    }

    @Override
    public void deleteAlarmDetail(int alarmId) {

        mView.showProgressBar();

        new AlarmUpdateAsync(alarmId, UpdateType.DELETE, new DBTaskUpdateCompletionListener() {
            @Override
            public void onUpdateCompleted(boolean isUpdated) {
                Log.d(TAG, "Is Alarm deleted - " + isUpdated);
                mView.hideProgressBar();
            }
        }).execute();

    }

    @Override
    public void updateAlarmState(int alarmId, boolean isActive) {

        mView.showProgressBar();

        new AlarmUpdateAsync(alarmId, UpdateType.CHANGE_ALARM_STATE, new DBTaskUpdateCompletionListener() {
            @Override
            public void onUpdateCompleted(boolean isUpdated) {
                Log.d(TAG, "Is Alarm updated - " + isUpdated);
                mView.hideProgressBar();
            }
        }).execute(isActive);

    }

    public void detachView() {
        mView = null;
    }

}
