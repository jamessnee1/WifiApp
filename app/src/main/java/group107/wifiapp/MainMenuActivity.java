package group107.wifiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        AppData.getInstance().setHotspotName("Test Device");
        Log.d("onCreate", "Device Name is " + AppData.getInstance().getHotspotName());


    }

    //Button methods
    public void startHotspotButtonPressed(View view){
        //takes the user to a Google Maps Activity to start the process of creating
        //a new WiFi hotspot
        Intent intent = new Intent(this, CreateHotspotActivity.class);
        startActivity(intent);
    }

    public void connectToHotspotButtonPressed(View view) {
        //takes the user to a ListActivity to view the stored Hotspots already created
        Intent intent = new Intent(this, ConnectToExistingHotspotActivity.class);
        startActivity(intent);
    }

    public void settingsButtonPressed(View view) {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);


    }

}
