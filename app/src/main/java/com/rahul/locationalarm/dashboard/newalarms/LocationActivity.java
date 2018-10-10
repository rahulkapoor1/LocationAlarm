package com.rahul.locationalarm.dashboard.newalarms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseActivity;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, OnSuccessListener<Location>, OnFailureListener {

    public static String KEY_LOCATION = "key_location";

    private GoogleMap mGoogleMap;

    private double mLatitude;

    private double mLongitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        findViewById(R.id.btn_done).setOnClickListener(this);

        final SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert map != null;
        map.getMapAsync(this);
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showAlertWithOkButton(getString(R.string.allow_fine_location));
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mGoogleMap = googleMap;

        // Last last known location of user
        getLastKnownLocation();

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                mLatitude = marker.getPosition().latitude;
                mLongitude = marker.getPosition().longitude;

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_done) {

            final Intent data = new Intent();
            final double[] location = new double[]{mLatitude, mLongitude};
            data.putExtra(KEY_LOCATION, location);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onSuccess(Location location) {
        final LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());

        mLatitude = latLong.latitude;
        mLongitude = latLong.longitude;

        mGoogleMap.addMarker(new MarkerOptions().position(latLong)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)).draggable(true));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLong));

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, "Unable to Get known location", Toast.LENGTH_SHORT).show();
        finish();
    }
}
