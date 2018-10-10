package com.rahul.locationalarm.login;

import android.support.annotation.NonNull;

public interface LoginUserActionListener {

    /*
    * Action taken by user to do login by passing username and password.
    */
    void onSubmitClicked(@NonNull String username, @NonNull String password);

}
