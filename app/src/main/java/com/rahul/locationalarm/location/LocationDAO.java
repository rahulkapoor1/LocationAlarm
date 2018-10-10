package com.rahul.locationalarm.location;

import android.location.Location;

import java.util.List;

public interface LocationDAO {

    String TABLE_NAME = "Location";

    String COL_USER_ID = "user_id";

    String COL_LOCATION_ID = "location_id";

    String COL_LATITUDE = "latitude";

    String COL_LONGITUDE = "longitude";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_USER_ID + " INTEGER,"
            + COL_LATITUDE + " REAL,"
            + COL_LONGITUDE + " REAL "
            + ")";

    /**
     * To get the list of un-synced locations from database. Synced will delete from database.
     *
     * @param userId Id of current user.
     * @return List of locations if any otherwise empty list.
     */
    List<LocationModel> getNonSyncedLocationDetails(final int userId);

    /**
     * To save location details.
     *
     * @param userId  Id of current user.
     * @param details Location detail to save.
     * @return True if saved successfully otherwise false.
     */
    boolean saveLocationDetail(final int userId, final List<Location> details);

    /**
     * To delete saved locations from database.
     *
     * @param userId      Id of current user.
     * @param locationIds Location ids to delete.
     */
    void deleteLocationDetails(final int userId, final int[] locationIds);

}
