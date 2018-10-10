package com.rahul.locationalarm.login;

public interface LoginDAO {

    String TABLE_NAME = "Login";

    String COL_USER_ID = "user_id";

    String COL_NAME = "name";

    String COL_ROLE = "role";

    String COL_TOKEN = "token";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                    + COL_USER_ID + " TEXT,"
                    + COL_NAME + " TEXT,"
                    + COL_ROLE + " TEXT,"
                    + COL_TOKEN + " TEXT "
                    + ")";

    LoginModel getUserDetail();

    boolean saveUserDetail(LoginModel detail);
}
