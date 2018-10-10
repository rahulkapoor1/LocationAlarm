package com.rahul.locationalarm.login;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rahul.locationalarm.database.DatabaseManager;
import com.rahul.locationalarm.injectors.ComponentHolder;

import javax.inject.Inject;

public class LoginDAOImpl implements LoginDAO {

    // Injected Database manager to get advantage while writing unit tests
    @Inject
    DatabaseManager mDatabaseManager;

    public LoginDAOImpl() {
        ComponentHolder.getInstance().getApplicationComponent().inject(this);

    }

    @Override
    public LoginModel getUserDetail() {

        LoginModel loginModel = null;

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();
        final Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            final int userId = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
            final String userName = cursor.getString(cursor.getColumnIndex(COL_NAME));
            final String role = cursor.getString(cursor.getColumnIndex(COL_ROLE));
            final String token = cursor.getString(cursor.getColumnIndex(COL_TOKEN));

            loginModel = new LoginModel(userId, userName, role, token);

            cursor.close();
        }

        mDatabaseManager.closeDatabase();

        return loginModel;
    }

    @Override
    public boolean saveUserDetail(LoginModel detail) {

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();
        final ContentValues values = new ContentValues();
        values.put(COL_USER_ID, detail.getUserId());
        values.put(COL_NAME, detail.getName());
        values.put(COL_ROLE, detail.getRole());
        values.put(COL_TOKEN, detail.getToken());

        final boolean isAdded = database.insert(TABLE_NAME, null, values) > 0;

        mDatabaseManager.closeDatabase();

        return isAdded;
    }
}
