package com.rahul.locationalarm.Splash;

import android.os.Bundle;
import android.os.Handler;

import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseActivity;
import com.rahul.locationalarm.dashboard.alarms.AlarmsActivity;
import com.rahul.locationalarm.login.LoginActivity;
import com.rahul.locationalarm.login.LoginModel;
import com.rahul.locationalarm.login.UserDetailAsync;
import com.rahul.locationalarm.login.UserDetailCallBack;

/*
 * Splash Activity to show logo of the App and to start with startup screens.
 * */
public class SplashActivity extends BaseActivity implements UserDetailCallBack {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private Handler mNextScreenHandler;

    private boolean mIsUserDetailExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        getExistingUserDetail();
    }

    /*
     * To Start next screen after delay
     * */
    private void getExistingUserDetail() {
        new UserDetailAsync(this).execute();
    }

    @Override
    public void onUserDetailExist(LoginModel detail) {
        mNextScreenHandler = new Handler();
        mIsUserDetailExist = true;
        goToNextScreen();
    }

    @Override
    public void onUserDetailUnavailable() {
        mNextScreenHandler = new Handler();
        mIsUserDetailExist = false;
        goToNextScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        goToNextScreen();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNextScreenHandler != null) {
            mNextScreenHandler.removeCallbacks(null);
        }
    }

    private void goToNextScreen() {
        if (mNextScreenHandler != null) {
            mNextScreenHandler.postDelayed(getNextScreenToExecute(), SPLASH_DISPLAY_LENGTH);
        }
    }

    private Runnable getNextScreenToExecute() {
        return new Runnable() {
            @Override
            public void run() {
                final Class<?> activityToStart = mIsUserDetailExist ? AlarmsActivity.class : LoginActivity.class;
                startActivity(activityToStart, true);
            }
        };
    }
}
