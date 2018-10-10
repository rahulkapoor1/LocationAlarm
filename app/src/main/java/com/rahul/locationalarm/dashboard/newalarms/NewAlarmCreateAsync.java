package com.rahul.locationalarm.dashboard.newalarms;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.dashboard.alarms.AlarmDAOImpl;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;

import java.lang.ref.WeakReference;

public class NewAlarmCreateAsync extends AsyncTask<Void, Void, Integer> {

    private final AlarmModel mAlarmDetail;

    private final WeakReference<AlarmSaveCallback> mCompletionListener;

    public NewAlarmCreateAsync(@NonNull final AlarmModel alarm,
                               @NonNull final AlarmSaveCallback completionListener) {
        this.mAlarmDetail = alarm;
        this.mCompletionListener = new WeakReference<>(completionListener);
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        return new AlarmDAOImpl().saveAlarmDetail(mAlarmDetail);
    }

    @Override
    protected void onPostExecute(Integer savedRowId) {
        super.onPostExecute(savedRowId);

        if (this.mCompletionListener.get() != null) {
            this.mCompletionListener.get().onAlarmSave(savedRowId);
        }
    }
}
