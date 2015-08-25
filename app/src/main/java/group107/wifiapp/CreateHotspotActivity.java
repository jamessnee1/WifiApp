package group107.wifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateHotspotActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //listener, locationmanager and instances to GPS and Network
    private LocationListener listener = null;
    private LocationManager locationManager = null;
    private Location myLocationGPS, myLocationNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hotspot);
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

            //SET UP LISTENERS HERE
            mMap.setOnMarkerDragListener(this);

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

        //Get locationmanager object from system service
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        myLocationNetwork = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        myLocationGPS = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        listener = new myLocationListener();

        //call the listener with either network or GPS (whichever is not null, GPS has priority)
        if (myLocationGPS == null){
            listener.onLocationChanged(myLocationNetwork);
        }
        else if (myLocationNetwork == null) {
            listener.onLocationChanged(myLocationGPS);
        }
        //if both GPS and Wifi values are null, show error and finish activity
        else if (myLocationGPS == null && myLocationNetwork == null){

            Toast.makeText(this, "Error: Could not establish connection! Please ensure that Wifi and GPS " +
                    "are turned on in your device's settings, and that you are in range.", Toast.LENGTH_LONG).show();
            finish();

        }
        //we should never get to this, this is just for safety
        else {
            Toast.makeText(this, "An unknown error occurred!", Toast.LENGTH_LONG).show();
            finish();
        }

        //setup button to create new hotspot


        //setup map type, can possibly be changed in options
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //initial starting message
        Toast.makeText(this, "Long-press on markers to move them. To add an end point, tap the screen.", Toast.LENGTH_LONG).show();

        //for testing
        //createNewHotspotDialog();

    }


    //marker change listener

    LatLng fromPosition;
    LatLng toPosition;

    @Override
    public void onMarkerDragStart(Marker marker) {

        fromPosition = marker.getPosition();

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        toPosition = marker.getPosition();
        Toast.makeText(getApplicationContext(), "Marker " + marker.getTitle() + " dragged from " +
                fromPosition +  " to " + toPosition, Toast.LENGTH_LONG).show();

    }

    //add map marker map click listener
    @Override
    public void onMapClick(LatLng latLng) {

        MarkerOptions endPointMarker =  new MarkerOptions().position
                (new LatLng(latLng.latitude, latLng.longitude)).title("End Point");

        mMap.addMarker(endPointMarker);

    }

    public class myLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            //create new LatLng for start point
            LatLng latLngStart = new LatLng(location.getLatitude(), location.getLongitude());
            //animate camera to current location, 1 is furthest away and 21 is closest
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngStart, 20);
            mMap.animateCamera(cameraUpdate);
            //add marker to current location
            MarkerOptions startPoint;
            mMap.addMarker(startPoint = new MarkerOptions().position(latLngStart).title("Start point").draggable(true));


            //check if incoming position has come from GPS or Network
            if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
                locationManager.removeUpdates(this);
            }else {
                locationManager.removeUpdates(listener);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            locationManager.removeUpdates(this);
            locationManager.removeUpdates(listener);

        }
    }

    //method for the pop up overlay when the new button is clicked
    public void createNewHotspotDialog(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create new hotspot");

        final EditText hotspotName = new EditText(this);
        hotspotName.setHint("Hotspot name");
        final EditText hotspotPassword = new EditText(this);
        hotspotPassword.setHint("Hotspot password");

        hotspotName.setInputType(InputType.TYPE_CLASS_TEXT);
        hotspotPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(hotspotName);
        layout.addView(hotspotPassword);
        builder.setView(layout);

        //set up buttons
        builder.setPositiveButton("Register Hotspot", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //get inputs and send them to the AppData
                //String hotspot_name = hotspotName.getText();
                //String hotspot_pass = hotspotPassword.getText();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
