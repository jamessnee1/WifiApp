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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
