package com.rahul.locationalarm.injectors;

import android.content.Context;

import com.android.volley.toolbox.Volley;
import com.rahul.locationalarm.database.DatabaseHelper;
import com.rahul.locationalarm.database.DatabaseManager;
import com.rahul.locationalarm.helpers.VolleyRequestHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context context) {
        this.mContext = context;
    }

    @Provides
    public VolleyRequestHelper provideRequestHelper() {
        return new VolleyRequestHelper(Volley.newRequestQueue(mContext));
    }

    @Provides
    @Singleton
    public DatabaseHelper provideDatabaseHelper() {
        return new DatabaseHelper(mContext);
    }

    @Provides
    public DatabaseManager provideDatabaseManager() {
        return DatabaseManager.getInstance();
    }
}
