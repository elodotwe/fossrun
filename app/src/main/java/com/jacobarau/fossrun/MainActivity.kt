package com.jacobarau.fossrun

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class MainActivity : AppCompatActivity() {

    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, "pk.eyJ1IjoiamJvdGFsYW4iLCJhIjoicTdhWFhTYyJ9.hC3f3RsDb-gD1zDtj1F6QA")

        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS) }
//        mapView.getMapAsync(new OnMapReadyCallback () {
//            @Override
//            public void onMapReady(@NonNull MapboxMap mapboxMap) {
//                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style . OnStyleLoaded () {
//                    @Override
//                    public void onStyleLoaded(@NonNull Style style) {
//
//                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
//
//
//                    }
//                });
//            }
//        });
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume()
    {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    override fun onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    override fun onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    override fun onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            mapView.onSaveInstanceState(outState)
        }
    }
}
