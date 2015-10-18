package group107.wifiapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Method;

//Settings Activity, by James Snee
//Mostly UI, Settings page to control certain things

public class SettingsActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private WifiApManager wifiApManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //wifi on/off switch
        Switch wifiSwitch = (Switch)findViewById(R.id.settingsWifiSwitch);
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

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
        if (WifiApManager.getInstance(wifiManager).isWifiApEnabled()){
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

                    turnOnOffHotspot(getApplicationContext(), true);
                    AppData.getInstance().setIsWifiHotspotEnabled(true);
                } else {

                    turnOnOffHotspot(getApplicationContext(), false);
                    AppData.getInstance().setIsWifiHotspotEnabled(false);
                }

            }
        });



    }

    public void showCreditsAlert(View view) {

        AlertDialog credits = new AlertDialog.Builder(SettingsActivity.this).create();
        credits.setTitle("Programming Project 1 - Group 107 - Credits");
        credits.setCancelable(false);
        credits.setMessage("Programming: James Snee\nAssistant Programming: Lincoln Birch\n" +
                "Design: Pei Chuang\nResearch: Trent Rozkowicz\nProject Supervisor:" +
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

    //clear all hotspots
    public void clearHotspotButtonPressed(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Clear all hotspots");
        builder.setMessage("WARNING: This will clear all saved hotspots. Are you sure you want to continue?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //clear database here
                DatabaseHandler.getInstance(getApplicationContext()).deleteAllData();
                Toast.makeText(getApplicationContext(), "All hotspots deleted!", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();

    }

    //turn on and off hotspot
    public void turnOnOffHotspot(Context context, boolean isOn){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        wifiApManager = WifiApManager.getInstance(wifiManager);

        if(wifiApManager != null){

            //turn wifi off first
            wifiManager.setWifiEnabled(false);

            wifiApManager.setWifiApEnabled(wifiApManager.getApWifiConfiguration(), isOn);

        }
    }

    //Takes user to device's APN settings page
    public void apnSettingsButtonPressed(View view) {

        //startActivityForResult(new Intent(Settings.ACTION_APN_SETTINGS), 0);
        startActivity(new Intent(Settings.ACTION_APN_SETTINGS));

    }
}
