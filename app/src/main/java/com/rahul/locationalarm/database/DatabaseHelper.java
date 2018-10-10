package com.rahul.locationalarm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.dashboard.alarms.AlarmDAO;
import com.rahul.locationalarm.location.LocationDAO;
import com.rahul.locationalarm.login.LoginDAO;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "airtel_db";

    @Inject
    public DatabaseHelper(@NonNull final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create Login table
        db.execSQL(LoginDAO.CREATE_TABLE);
        db.execSQL(AlarmDAO.CREATE_TABLE);
        db.execSQL(LocationDAO.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
