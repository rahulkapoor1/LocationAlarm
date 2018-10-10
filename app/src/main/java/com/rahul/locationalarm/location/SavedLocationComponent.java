package com.rahul.locationalarm.location;

import dagger.Component;

@Component(modules = SavedLocationModule.class)
public interface SavedLocationComponent {
    void inject(SavedLocationActivity target);
}
