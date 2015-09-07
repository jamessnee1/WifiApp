package group107.wifiapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class ConnectToExistingHotspotActivity extends AppCompatActivity {

    public static String wifiToCheck = "RMIT-University";
    private WifiManager wifiMgr;

    //create broadcast receiver
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction() == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                List<ScanResult> mScanResults = wifiMgr.getScanResults();

                boolean foundNetwork = false;
                String foundNetworkName = "";

                //go thru wifi list
                for(ScanResult scan : mScanResults){

                    foundNetworkName = scan.SSID;

                    //if network is not found, show dialog
                    if(foundNetworkName.equals(wifiToCheck)){

                        foundNetwork = true;
                        createDialog("Wifi hotspot found!", "Wifi hotspot was found! SSID is " + foundNetworkName);

                    }

                }
                if(!foundNetwork) {

                    createDialog("Wifi hotspot not found!", "Error: No wifi hotspots were found!");

                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_existing_hotspot);

        //search for wifi here
        wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMgr.startScan();

    }

    public void createDialog(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        builder.show();


    }
}
