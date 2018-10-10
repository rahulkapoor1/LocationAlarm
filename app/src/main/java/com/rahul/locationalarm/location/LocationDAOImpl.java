package com.rahul.locationalarm.location;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.rahul.locationalarm.database.DatabaseManager;
import com.rahul.locationalarm.injectors.ComponentHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LocationDAOImpl implements LocationDAO {

    // Injected Database manager to get advantage while writing unit tests
    @Inject
    DatabaseManager mDatabaseManager;

    public LocationDAOImpl() {
        ComponentHolder.getInstance().getApplicationComponent().inject(this);

    }

    @Override
    public List<LocationModel> getNonSyncedLocationDetails(int userId) {

        List<LocationModel> locations = new ArrayList<>();

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        // where clause to get non synced locations
        final String whereClause = COL_USER_ID + " =?";
        final String[] whereArgs = new String[]{String.valueOf(userId)};

        final Cursor cursor = database.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                final int locationId = cursor.getInt(cursor.getColumnIndex(COL_LOCATION_ID));
                final double latitude = cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE));
                final double longitude = cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE));

                locations.add(new LocationModel(locationId, latitude, longitude));

            } while (cursor.moveToNext());

            cursor.close();
        }

        mDatabaseManager.closeDatabase();

        return locations;
    }

    @Override
    public boolean saveLocationDetail(int userId, List<Location> details) {

        boolean isSuccessful = true;
        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        try {

            // Bulk insert
            database.beginTransaction();

            for (Location location : details) {
                final ContentValues values = new ContentValues();
                values.put(COL_USER_ID, userId);
                values.put(COL_LATITUDE, location.getLatitude());
                values.put(COL_LONGITUDE, location.getLongitude());

                // If save is not successful, mark it unsuccessful
                if ((int) database.insert(TABLE_NAME, null, values) == 0) {
                    isSuccessful = false;
                }
            }

            database.setTransactionSuccessful();

        } finally {
            database.endTransaction();
            mDatabaseManager.closeDatabase();
        }
        return isSuccessful;
    }

    @Override
    public void deleteLocationDetails(int userId, int[] locationIds) {

        final SQLiteDatabase database = mDatabaseManager.getOpenDatabase();

        database.execSQL(String.format("DELETE FROM " + TABLE_NAME +" WHERE "+ COL_LOCATION_ID + " IN (%s);",
                getCommaSeparatedLocations(locationIds)));

        mDatabaseManager.closeDatabase();
    }


    private String getCommaSeparatedLocations(final int[] locationIds) {
        String result = "";
        if (locationIds.length > 0) {
            final StringBuilder sb = new StringBuilder();
            for (int id : locationIds) {
                sb.append(id).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

}

