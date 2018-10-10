package com.rahul.locationalarm.location;

import com.rahul.locationalarm.common.BaseViewInterface;

import java.util.List;

public interface SavedLocationView extends BaseViewInterface {

    void onSavedLocationsReceived(List<LocationModel> locations);

    void onReceivedErrorInGettingLocations(int messageResId);

}
