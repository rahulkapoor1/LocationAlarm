package com.rahul.locationalarm.helpers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rahul.locationalarm.database.DatabaseHelper;
import com.rahul.locationalarm.database.DatabaseManager;
import com.rahul.locationalarm.injectors.ComponentHolder;

/**
 * To setup essentials objects on AppSetup.
 */
public class AppSetupManager {

    private AppSetupManager() {

    }

    private static class SingletonHelper{
        private static final AppSetupManager INSTANCE = new AppSetupManager();
    }

    public static AppSetupManager getInstance(){
        return AppSetupManager.SingletonHelper.INSTANCE;
    }

    /**
     * To setup database helper and Application dagger.
     * @param context Application context.
     */
    public void setupApp(@NonNull Context context) {
        ComponentHolder.getInstance().setApplicationComponent(context);
        initDatabase(context);
    }

    /**
     * To initialise database manager and database helper.
     * @param context Application context.
     */
    private void initDatabase(@NonNull Context context) {
        DatabaseManager.initializeInstance(new DatabaseHelper(context));
    }
}
