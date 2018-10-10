package com.rahul.locationalarm.login;

import android.support.annotation.StringRes;

import com.rahul.locationalarm.common.BaseViewInterface;

public interface LoginView extends BaseViewInterface {

    void onLoginSuccessful();

    void onLoginUnSuccessful(@StringRes int errorMessageResId);
}
