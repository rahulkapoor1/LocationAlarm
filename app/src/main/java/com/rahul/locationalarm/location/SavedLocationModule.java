package com.rahul.locationalarm.location;

import dagger.Module;
import dagger.Provides;

@Module
public class SavedLocationModule {

    @Provides
    public SavedLocationPresenterImpl provideSavedLocationPresenter() {
        return new SavedLocationPresenterImpl();
    }

}
