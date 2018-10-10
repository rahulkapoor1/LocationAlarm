package com.rahul.locationalarm.location;

import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.common.DBTaskSaveCompletionListener;
import com.rahul.locationalarm.login.LoginDAOImpl;
import com.rahul.locationalarm.login.LoginModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class LocationSaveAsync extends AsyncTask<Void, Void, Boolean> {

    private final List<Location> mLocations;

    private final WeakReference<DBTaskSaveCompletionListener> mCompletionListener;

    public LocationSaveAsync(@NonNull final List<Location> locations,
                             @NonNull final DBTaskSaveCompletionListener completionListener) {
        this.mLocations = locations;
        this.mCompletionListener = new WeakReference<>(completionListener);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        boolean isSuccessful = false;

        final LoginModel loginDetail = new LoginDAOImpl().getUserDetail();

        if (loginDetail != null) {
            final int userId = loginDetail.getUserId();
            isSuccessful = new LocationDAOImpl().saveLocationDetail(userId, mLocations);
        }

        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean aResult) {
        super.onPostExecute(aResult);

        if (mCompletionListener.get() != null) {
            mCompletionListener.get().onDBTaskCompleted(aResult);
        }

    }
}
