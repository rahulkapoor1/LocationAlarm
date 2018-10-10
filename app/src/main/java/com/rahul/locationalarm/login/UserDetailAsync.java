package com.rahul.locationalarm.login;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/*
 *
 * To return the details of existing user if any.
 *
 */
public class UserDetailAsync extends AsyncTask<Void, Void, LoginModel> {

    private final WeakReference<UserDetailCallBack> mListener;

    public UserDetailAsync(@NonNull final UserDetailCallBack listener) {
        this.mListener = new WeakReference<>(listener);
    }

    @Override
    protected LoginModel doInBackground(Void... voids) {
        return new LoginDAOImpl().getUserDetail();
    }

    @Override
    protected void onPostExecute(LoginModel loginDetail) {
        super.onPostExecute(loginDetail);

        if (mListener.get() != null) {
            if (loginDetail == null) {
                mListener.get().onUserDetailUnavailable();
            } else {
                mListener.get().onUserDetailExist(loginDetail);
            }
        }
    }
}
