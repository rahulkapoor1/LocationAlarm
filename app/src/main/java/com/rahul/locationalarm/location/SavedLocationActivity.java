package com.rahul.locationalarm.location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseActivity;

import java.util.List;

import javax.inject.Inject;

public class SavedLocationActivity extends BaseActivity implements OnMapReadyCallback, SavedLocationView {

    private GoogleMap mGoogleMap;

    @Inject
    SavedLocationPresenterImpl mLocationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synced_locations);
        loadMap();

        initPresenter();
        getSavedLocations();
    }

    private void loadMap() {
        final SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert map != null;
        map.getMapAsync(this);
    }

    /**
     * To Initialize presenter
     */
    private void initPresenter() {
        DaggerSavedLocationComponent.builder().build().inject(this);
        mLocationPresenter.attachView(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getSavedLocations() {
        mLocationPresenter.getSavedLocations();
    }

    @Override
    public void showProgressBar() {
        Toast.makeText(this, "Fetching details", Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgressBar() {
        // DO Nothing
    }

    @Override
    public void onSavedLocationsReceived(List<LocationModel> locations) {

        final PolylineOptions polylineOptions = new PolylineOptions()
                .width(15)
                .color(ContextCompat.getColor(this, R.color.colorAccent));

        for (LocationModel location : locations) {
            polylineOptions.add(location.getLatLng());
        }

        final Polyline line = mGoogleMap.addPolyline(polylineOptions);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(line.getPoints().get(0)));

    }

    @Override
    public void onReceivedErrorInGettingLocations(int messageResId) {
        showAlertWithOkButton(getString(messageResId));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationPresenter.detachView();
    }
}
