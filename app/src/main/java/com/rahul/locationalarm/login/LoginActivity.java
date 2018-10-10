package com.rahul.locationalarm.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.rahul.locationalarm.common.BaseActivity;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.dashboard.alarms.AlarmsActivity;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView {

    private ProgressBar mProgressBar;

    private EditText mEdtUsername;

    private EditText mEdtPassword;

    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initPresenter();
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLoginPresenter.attachView(this);
    }

    /**
     * To Initialize presenter
     */
    private void initPresenter() {
        DaggerLoginComponent.builder().build().inject(this);
    }

    private void findViews() {
        mProgressBar = findViewById(R.id.progress_bar);
        mEdtUsername = findViewById(R.id.edt_username);
        mEdtPassword = findViewById(R.id.edt_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_login) {
            mLoginPresenter.onSubmitClicked(mEdtUsername.getText().toString(), mEdtPassword.getText().toString());
        }
    }

    @Override
    public void showProgressBar() {
        makeUINonInteracting();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        makeUIInteracting();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoginSuccessful() {
        hideProgressBar();
        startActivity(AlarmsActivity.class, true);
    }

    @Override
    public void onLoginUnSuccessful(int errorMessageResId) {
        hideProgressBar();
        showAlertWithOkButton(getString(errorMessageResId));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoginPresenter.detachView();
    }
}
