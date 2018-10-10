package com.rahul.locationalarm.login;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.common.DBTaskSaveCompletionListener;

import java.lang.ref.WeakReference;

/**
 * To save user details.
 */
public class LoginDetailSaveAsync extends AsyncTask<Void, Void, Boolean> {

    private final LoginModel mLoginModel;

    private final WeakReference<DBTaskSaveCompletionListener> mCompletionListener;

    public LoginDetailSaveAsync(@NonNull final LoginModel loginModel,
                                @NonNull final DBTaskSaveCompletionListener dbTaskCompletionListener) {
        this.mLoginModel = loginModel;
        this.mCompletionListener = new WeakReference<>(dbTaskCompletionListener);
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        // To save detail in database
        return new LoginDAOImpl().saveUserDetail(mLoginModel);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (mCompletionListener.get() != null) {
            // Inform about task completion
            mCompletionListener.get().onDBTaskCompleted(result);
        }

    }
}
