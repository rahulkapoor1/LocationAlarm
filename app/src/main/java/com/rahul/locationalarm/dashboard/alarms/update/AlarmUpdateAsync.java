package com.rahul.locationalarm.dashboard.alarms.update;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.common.DBTaskUpdateCompletionListener;
import com.rahul.locationalarm.dashboard.alarms.AlarmDAOImpl;

import java.lang.ref.WeakReference;

public class AlarmUpdateAsync extends AsyncTask<Boolean, Void, Boolean> {

    private final int mAlarmId;

    private final UpdateType mUpdateTypeEnum;

    private final WeakReference<DBTaskUpdateCompletionListener> mCompletionListener;

    public AlarmUpdateAsync(final int alarmId,
                            @NonNull final UpdateType updateTypeEnum,
                            @NonNull final DBTaskUpdateCompletionListener completionListener) {
        this.mAlarmId = alarmId;
        this.mUpdateTypeEnum = updateTypeEnum;
        this.mCompletionListener = new WeakReference<>(completionListener);
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {

        boolean isSuccessful;

        if (mUpdateTypeEnum == UpdateType.DELETE) {
            isSuccessful = new AlarmDAOImpl().deleteAlarmDetail(mAlarmId);
        } else {
            isSuccessful = params.length == 1 && new AlarmDAOImpl().updateAlarmState(mAlarmId, params[0]);
        }

        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (mCompletionListener.get() != null) {
            mCompletionListener.get().onUpdateCompleted(aBoolean);
        }
    }
}
