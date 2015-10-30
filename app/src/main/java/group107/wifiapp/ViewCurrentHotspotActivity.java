package group107.wifiapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

//View Current Hotspot Activity, by James Snee

public class ViewCurrentHotspotActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private MarkerOptions startPointMarker, endPointMarker;
    private LatLng start, end;
    private Polyline mapLine;
    private TextView timerText;
    private PolylineOptions lineOptions;
    private String selectedMode = "";

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

        //timer
        timerText = (TextView)findViewById(R.id.timerTextView);
        timerText.setText("00:00:00");

        //set the timer to count down
        final TimerClass timer = new TimerClass(AppData.getInstance().getTimeRemaining(), 1000);
        timer.start();

        //setup map type, normal as default
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //add marker to current location
        startPointMarker = new MarkerOptions().position(start).title("Start point");
        mMap.addMarker(startPointMarker);

        //add end marker
        endPointMarker = new MarkerOptions().position(end).title("End point");
        mMap.addMarker(endPointMarker);

        // Getting URL to the Google Directions API
        String url = getMapsApiDirectionsUrl(start, end, selectedMode);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        //draw polyline between the two locations
        //PolylineOptions options = new PolylineOptions().add(start,end).width(5).color(Color.RED);
        //mapLine = mMap.addPolyline(options);

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

    //Timer Class
    public class TimerClass extends CountDownTimer {

        public TimerClass(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            long millis = millisUntilFinished;
            String hoursMinutesSeconds = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            timerText.setText(hoursMinutesSeconds);

        }

        @Override
        public void onFinish() {

            timerText.setText("00:00:00");

        }

    }

    //construct our string URL for use with the Directions API
    private String getMapsApiDirectionsUrl(LatLng start, LatLng end, String mode){

        String waypoints = "origin=" + start.latitude +
                "," + start.longitude + "&destination=" + end.latitude + "," + end.longitude;

        String sensor = "sensor=false";
        String modeOfTravel = "&mode=" + mode;
        //if no mode is selected, it will default to driving
        String params = waypoints + "&" + sensor + modeOfTravel;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;

    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mapLine = mMap.addPolyline(lineOptions);
        }
    }

}
