package com.trackify.trackify;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.tab_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab2Clicked();
            }
        });
        findViewById(R.id.tab_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTab3Clicked();
            }
        });
        findViewById(R.id.listViewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListButtonClicked();
            }
        });

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create coordinates for home and DTU
        LatLng dtu = new LatLng(55.785637, 12.521382);
        LatLng home = new LatLng(55.709824, 12.572348);

        //Add circle and marker for DTU
        mMap.addCircle(new CircleOptions()
                .center(dtu)
                .radius(1000)
                .strokeColor(Color.RED)
                .fillColor(0x330000FF));
        mMap.addMarker(new MarkerOptions()
                .position(dtu)
                .title("University")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school_black_36dp))
                .anchor(.5f,.5f));

        //Add circle and marker for home
        mMap.addCircle(new CircleOptions()
                .center(home)
                .radius(300)
                .strokeColor(Color.RED)
                .fillColor(0x330000FF));
        mMap.addMarker(new MarkerOptions()
                .position(home)
                .title("Home")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home_black_36dp))
        .anchor(.5f,.5f));

        //Create polyline
        mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(new LatLng(55.709824, 12.572348)
                        ,new LatLng(55.714552, 12.558977)
                        ,new LatLng(55.764217, 12.520590)
                        ,new LatLng(55.785637, 12.521382)
                        ,new LatLng(55.728385, 12.441790)
                        ,new LatLng(55.696614, 12.523890))
                .color(Color.BLUE));

        //Adjust camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
        mMap.setMinZoomPreference(10);
    }

    private void onListButtonClicked() {
        Intent intent = new Intent(this, TodayActivity.class);
        startActivity(intent);
    }

    private void onTab2Clicked() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void onTab3Clicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
