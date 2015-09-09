package group107.wifiapp;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class ViewCurrentHotspotActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MarkerOptions startPointMarker, endPointMarker;
    private LatLng start, end;
    private Polyline mapLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_current_hotspot);

        //get data from AppData
        start = new LatLng(AppData.getInstance().getLat_startPoint(), AppData.getInstance().getLong_startPoint());
        end = new LatLng(AppData.getInstance().getLat_endPoint(), AppData.getInstance().getLong_endPoint());

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        //Enable My Location button layer (user can choose this to get their location)
        mMap.setMyLocationEnabled(true);

        //setup map type, normal as default
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //add marker to current location
        startPointMarker = new MarkerOptions().position(start).title("Start point");
        mMap.addMarker(startPointMarker);

        //add end marker
        endPointMarker = new MarkerOptions().position(end).title("End point");
        mMap.addMarker(endPointMarker);

        //draw polyline between the two locations
        PolylineOptions options = new PolylineOptions().add(start,end).width(5).color(Color.RED);
        mapLine = mMap.addPolyline(options);

        //animate camera to start location, 1 is furthest away and 21 is closest
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(start, 20);
        mMap.animateCamera(cameraUpdate);

    }

    //map buttons
    public void normalMapButtonPressed(View view) {

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    public void satelliteMapButtonPressed(View view) {

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void hybridMapButtonPressed(View view) {

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

}
