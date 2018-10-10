package com.rahul.locationalarm.login;

public class LoginModel {

    private int mUserId;

    private String mName;

    private String mRole;

    private String mToken;


    public LoginModel(int userId, String name, String role, String token) {
        this.mUserId = userId;
        this.mName = name;
        this.mRole = role;
        this.mToken = token;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getName() {
        return mName;
    }

    public String getRole() {
        return mRole;
    }

    public String getToken() {
        return mToken;
    }
}
