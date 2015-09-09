package group107.wifiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


    }

    protected void onResume(){
        super.onResume();

        if (AppData.getInstance().getAppDataPopulated() == true) {
            startButton.setText("View current hotspot");
        }

    }


    //Button methods
    public void startHotspotButtonPressed(View view){

        //If user has already created a hotspot this session, pressing this button will
        //take the user to a populated map

        startButton = (Button)findViewById(R.id.startHotspotButton);
        //takes the user to a Google Maps Activity to view current hotspot data that is active
        if (AppData.getInstance().getAppDataPopulated() == true){
            //startButton.setText("View current hotspot");

            Intent viewIntent = new Intent(this, ViewCurrentHotspotActivity.class);
            startActivity(viewIntent);

        }
        else {

            //takes the user to a Google Maps Activity to start the process of creating
            //a new WiFi hotspot
            Intent intent = new Intent(this, CreateHotspotActivity.class);
            startActivity(intent);

        }

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
