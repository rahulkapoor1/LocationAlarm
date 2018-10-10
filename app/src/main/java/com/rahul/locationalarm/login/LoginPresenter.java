package com.rahul.locationalarm.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.URLs;
import com.rahul.locationalarm.common.DBTaskSaveCompletionListener;
import com.rahul.locationalarm.helpers.VolleyRequestHelper;
import com.rahul.locationalarm.injectors.ComponentHolder;
import com.rahul.locationalarm.utils.RequestCreator;
import com.rahul.locationalarm.utils.ResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class LoginPresenter implements LoginUserActionListener {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginView mView;

    @Inject
    VolleyRequestHelper mRequestHelper;

    LoginPresenter() {
        // Dependency is injected which is useful while writing unit tests
        ComponentHolder.getInstance().getApplicationComponent().inject(this);
    }

    /**
     * To attach a view with presenter.
     *
     * @param view View to get responses.
     */
    public void attachView(@NonNull LoginView view) {
        mView = view;
    }

    @Override
    public void onSubmitClicked(@NonNull String username, @NonNull String password) {

        username = username.trim();
        password = password.trim();

        // If username or password is invalid then show an error message
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mView.onLoginUnSuccessful(R.string.error_mandatory_fields);
        } else {
            mView.showProgressBar();
        }


        mRequestHelper.getResponse(URLs.LOGIN_URL, RequestCreator.getLoginParams(username, password),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        final LoginModel responseModel;
                        try {
                            responseModel = ResponseParser.getLoginResponse(response);

                            if (responseModel == null) {
                                mView.onLoginUnSuccessful(R.string.error_invalid_credentials);
                            } else {

                                new LoginDetailSaveAsync(responseModel, new DBTaskSaveCompletionListener() {
                                    @Override
                                    public void onDBTaskCompleted(boolean isSuccessful) {

                                        if (isSuccessful) {
                                            mView.onLoginSuccessful();
                                        } else {
                                            // Unrealistic case but good to handle
                                            // unable to save in database
                                            mView.onLoginUnSuccessful(R.string.error_unexpected);
                                        }
                                    }
                                }).execute();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Unable to get response");
                            mView.onLoginUnSuccessful(R.string.error_unexpected);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mView.onLoginUnSuccessful(R.string.error_invalid_credentials);
                    }
                });
    }

    /**
     * To remove reference count of Activity.
     */
    public void detachView() {
        mView = null;
    }
}
