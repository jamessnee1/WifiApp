package group107.wifiapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConnectToExistingHotspotActivity extends ListActivity {

    public static String wifiToCheck = "RagnarosTheFirelord";
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
                        createDialog("Wifi hotspot found!", "Wifi hotspot was found! SSID is "
                                + foundNetworkName, foundNetwork);
                        break;

                    }

                }
                if(!foundNetwork) {

                    createDialog("Wifi hotspot not found!", "Error: No wifi hotspots were found!", foundNetwork);

                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_connect_to_existing_hotspot);


        //search for wifi here
        wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMgr.startScan();

        //Create cursor of all values in database
        Cursor retrieved = DatabaseHandler.getInstance(getApplicationContext()).retrieveAllData();

        if (retrieved.getCount() == 0){

            createDialog("Hotspots", "Error: no saved hotspots were found! " +
                    "Create a new hotspot for it to appear in this list.", false);

        }

        //Go through retrieved hotspot details and populate the hotspot list
        ArrayList<String> hotspotList = new ArrayList<String>();

        while(retrieved.moveToNext()){

            hotspotList.add("Hotspot name: " + retrieved.getString(1) + "\n" +
                    "Number of allowed users: " + retrieved.getInt(3) + "\n" +
                    "Allowed data: " + retrieved.getInt(12) + "mb\n");

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hotspotList);
        setListAdapter(adapter);

    }

    //Item selected in list will go to a new Google Maps activity and populate it with the chosen hotspot data
    protected void onListItemClick(ListView l, View v, int position, long id){

        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();

    }

    public void createDialog(String title, String message, final boolean foundNetwork){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if (!foundNetwork){
                    finish();
                }

            }
        });

        builder.show();


    }
}
