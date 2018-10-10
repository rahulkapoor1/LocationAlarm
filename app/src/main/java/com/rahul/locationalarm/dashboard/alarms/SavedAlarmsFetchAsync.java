package com.rahul.locationalarm.dashboard.alarms;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.common.DBTaskFetchCompletionListener;

import java.lang.ref.WeakReference;
import java.util.List;

public class SavedAlarmsFetchAsync extends AsyncTask<Void, Void, List<AlarmModel>> {

    private final WeakReference<DBTaskFetchCompletionListener> mCompletionListener;

    public SavedAlarmsFetchAsync(@NonNull final DBTaskFetchCompletionListener completionListener) {
        this.mCompletionListener = new WeakReference<>(completionListener);
    }


    @Override
    protected List<AlarmModel> doInBackground(Void... voids) {
        return new AlarmDAOImpl().getAlarmDetails();
    }

    @Override
    protected void onPostExecute(List<AlarmModel> alarms) {
        super.onPostExecute(alarms);

        if(mCompletionListener.get() != null) {
            mCompletionListener.get().onDBTaskCompleted(alarms);
        }
    }
}
