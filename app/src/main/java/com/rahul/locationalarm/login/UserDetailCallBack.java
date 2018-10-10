package com.rahul.locationalarm.login;

public interface UserDetailCallBack {

    void onUserDetailExist(LoginModel detail);

    void onUserDetailUnavailable();
}
