package group107.wifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.Method;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d("onCreate Settings", "Device name is " + AppData.getInstance().getHotspotName());

        //wifi on/off switch
        Switch wifiSwitch = (Switch)findViewById(R.id.settingsWifiSwitch);
        final WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        //wifi hotspot on/off switch
        Switch wifiHotspotSwitch = (Switch)findViewById(R.id.wifiHotspotSwitch);

        //set wifi on/off switch according to current wifi status
        if (wifiManager.isWifiEnabled()) {
            wifiSwitch.setChecked(true);
            AppData.getInstance().setIsWifiEnabled(true);
        } else {
            wifiSwitch.setChecked(false);
            AppData.getInstance().setIsWifiEnabled(false);
        }

        //set wifi hotspot switch on/off according to current hotspot status
        if (isWifiHotspotOn(this) == true){
            wifiHotspotSwitch.setChecked(true);
            AppData.getInstance().setIsWifiHotspotEnabled(true);
        }
        else {
            wifiHotspotSwitch.setChecked(false);
            AppData.getInstance().setIsWifiHotspotEnabled(false);
        }

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Switch status
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    AppData.getInstance().setIsWifiEnabled(true);
                } else {
                    wifiManager.setWifiEnabled(false);
                    AppData.getInstance().setIsWifiEnabled(false);
                }

            }
        });

        wifiHotspotSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Switch status
                if (isChecked) {
                    //TODO: call set hotspot on, move these functions to another class and call them here
                    //ApManager.configHotspot(SettingsActivity.this);
                    AppData.getInstance().setIsWifiHotspotEnabled(true);
                } else {
                    //TODO: call set hotspot off, move these functions to another class and call them here
                    //ApManager.configHotspot(SettingsActivity.this);
                    AppData.getInstance().setIsWifiHotspotEnabled(false);
                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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


    public void showCreditsAlert(View view) {

        AlertDialog credits = new AlertDialog.Builder(SettingsActivity.this).create();
        credits.setTitle("Programming Project 1 - Group 107 - Credits");
        credits.setCancelable(false);
        credits.setMessage("Programming: James Snee\nProgramming: Trent Rozkowicz\n" +
                "Assistant Programming: Lincoln Birch\nDesign: Pei Chuang\nProject Supervisor:" +
                " Azadeh Ghari Neiat");
        credits.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        credits.show();

    }

    //Method to check if wifiHotspot is on or off
    public static boolean isWifiHotspotOn(Context context){

        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);

        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean)method.invoke(wifiManager);
        }
        catch(Throwable ignored){
            return false;
        }

    }

    //Method to change wifi hotspot config
    public static boolean configHotspot(Context context){

        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);

        WifiConfiguration wifiConfiguration = null;
        try {
            //if Wifi on, turn it off
            if (isWifiHotspotOn(context)){
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, wifiConfiguration, !isWifiHotspotOn(context));
            return true;
        }
        catch (Exception e){
            //TODO: Error dialog goes here
            e.printStackTrace();
        }

        return false;

    }
}
