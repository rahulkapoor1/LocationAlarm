package com.rahul.locationalarm.dashboard.alarms;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rahul.locationalarm.database.DatabaseManager;
import com.rahul.locationalarm.injectors.ComponentHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlarmDAOImpl implements AlarmDAO {

    // Injected Database manager to get advantage while writing unit tests
    @Inject
    DatabaseManager mDatabaseManager;

    public AlarmDAOImpl() {
        ComponentHolder.getInstance().getApplicationComponent().inject(this);

    }

    @Override
    public int saveAlarmDetail(AlarmModel detail) {

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        final ContentValues values = new ContentValues();
        values.put(COL_NAME, detail.getName());
        values.put(COL_VIBRATION, detail.isShouldVibrate() ? 1 : 0);
        values.put(COL_ACTIVE, detail.isActive() ? 1 : 0);
        values.put(COL_RINGTONE, detail.getRingtone());
        values.put(COL_LATITUDE, detail.getLatitude());
        values.put(COL_LONGITUDE, detail.getLongitude());

        final int rowId = (int)database.insert(TABLE_NAME, null, values);
        mDatabaseManager.closeDatabase();
        return rowId;

    }

    @Override
    public List<AlarmModel> getAlarmDetails() {

        List<AlarmModel> models = new ArrayList<>();

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();
        final Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                final int alarmId = cursor.getInt(cursor.getColumnIndex(COL_ALARM_ID));
                final String alarmName = cursor.getString(cursor.getColumnIndex(COL_NAME));
                final boolean shouldVibrate = cursor.getInt(cursor.getColumnIndex(COL_VIBRATION)) == 1;
                final boolean isActive = cursor.getInt(cursor.getColumnIndex(COL_ACTIVE)) == 1;
                final String ringtonePath = cursor.getString(cursor.getColumnIndex(COL_RINGTONE));
                final double latitude = cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE));
                final double longitude = cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE));

                models.add(new AlarmModel(alarmId, alarmName, ringtonePath, shouldVibrate, isActive, latitude, longitude));
            } while (cursor.moveToNext());

            cursor.close();
        }

        mDatabaseManager.closeDatabase();

        return models;
    }

    @Override
    public boolean deleteAlarmDetail(int alarmId) {

        final String whereClause = COL_ALARM_ID + "=?";
        final String[] whereArgs = new String[]{String.valueOf(alarmId)};

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        final boolean isDeleted = (int)database.delete(TABLE_NAME, whereClause, whereArgs) == 1;

        mDatabaseManager.closeDatabase();

        return isDeleted;
    }

    @Override
    public boolean updateAlarmState(int alarmId, boolean isActive) {

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        final ContentValues values = new ContentValues();
        values.put(COL_ACTIVE, isActive ? 1 : 0);

        final String whereClause = COL_ALARM_ID + "=?";
        final String[] whereArgs = new String[]{String.valueOf(alarmId)};

        final boolean isUpdated = database.update(TABLE_NAME, values, whereClause, whereArgs) == 1;
        mDatabaseManager.closeDatabase();
        return isUpdated;
    }

    @Override
    public AlarmModel getAlarmDetail(String alarmId) {

        AlarmModel alarmDetail = null;

        final String whereClause = COL_ALARM_ID + "=?";
        final String[] whereArgs = new String[]{alarmId};

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        final Cursor cursor = database.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {

            final String alarmName = cursor.getString(cursor.getColumnIndex(COL_NAME));
            final boolean shouldVibrate = cursor.getInt(cursor.getColumnIndex(COL_VIBRATION)) == 1;
            final boolean isActive = cursor.getInt(cursor.getColumnIndex(COL_ACTIVE)) == 1;
            final String ringtonePath = cursor.getString(cursor.getColumnIndex(COL_RINGTONE));
            final double latitude = cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE));
            final double longitude = cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE));

            alarmDetail = new AlarmModel(Integer.valueOf(alarmId), alarmName, ringtonePath, shouldVibrate, isActive, latitude, longitude);

            cursor.close();
        }

        mDatabaseManager.closeDatabase();

        return alarmDetail;
    }
}
