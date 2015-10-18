package group107.wifiapp;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by James Snee on 11/09/15.
 * Reference: http://omtlab.com/android-enable-disable-hotspot-programmatically/
 * Since the WRITE_APN_SETTINGS permission was deprecated in Android Ice Cream Sandwich, we are not able to use
 * this class.
 */
public class WifiApManager {

    private static Method getWifiApState;
    private static Method isWifiApEnabled;
    private static Method setWifiApEnabled;
    private static Method getWifiApConfiguration;

    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";

    public static final int WIFI_AP_STATE_DISABLED = WifiManager.WIFI_STATE_DISABLED;
    public static final int WIFI_AP_STATE_DISABLING = WifiManager.WIFI_STATE_DISABLING;
    public static final int WIFI_AP_STATE_ENABLED = WifiManager.WIFI_STATE_ENABLED;
    public static final int WIFI_AP_STATE_ENABLING = WifiManager.WIFI_STATE_ENABLING;
    public static final int WIFI_AP_STATE_FAILED = WifiManager.WIFI_STATE_UNKNOWN;

    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = WifiManager.EXTRA_PREVIOUS_WIFI_STATE;
    public static final String EXTRA_WIFI_AP_STATE = WifiManager.EXTRA_WIFI_STATE;

    static {
        //Look up methods and fields not defined publicly in the SDK
        Class <?> cls = WifiManager.class;

        for (Method method : cls.getDeclaredMethods()){

            String methodName = method.getName();

            if(methodName.equals("getWifiApState")){
                getWifiApState = method;
            }
            else if(methodName.equals("isWifiApEnabled")){
                isWifiApEnabled = method;
            }
            else if(methodName.equals("setWifiApEnabled")){
                setWifiApEnabled = method;
            }
            else if(methodName.equals("getWifiApConfiguration")){
                getWifiApConfiguration = method;
            }

        }
    }

    //check if device supports hotspots
    public static boolean isApSupported(){
        return (getWifiApState != null && isWifiApEnabled != null
                && setWifiApEnabled != null && getWifiApConfiguration != null);
    }

    private WifiManager wifiManager;

    //constructor
    private WifiApManager(WifiManager wifiManager){
        this.wifiManager = wifiManager;
    }

    //getter
    public static WifiApManager getInstance(WifiManager wifiManager){

        if(!isApSupported()){
            return null;
        }

        return new WifiApManager(wifiManager);
    }

    //isWifiApEnabled
    public boolean isWifiApEnabled(){
        try{
            return (boolean)isWifiApEnabled.invoke(wifiManager);
        }
        catch(Exception e){
            //should not happen
            Log.v("Error:" , e.toString(), e);
            return false;
        }

    }

    //getWifiApState
    public int getWifiApState(){
        try {
            return (Integer) getWifiApState.invoke(wifiManager);
        } catch (Exception e) {
            //should not happen
            Log.v("Error:", e.toString(), e);
            return -1;
        }
    }

    //getWifiApConfiguration
    public WifiConfiguration getApWifiConfiguration(){
        try{
            return (WifiConfiguration) getWifiApConfiguration.invoke(wifiManager);
        }
        catch (Exception e){
            //should not happen
            Log.v("Error:", e.toString(), e);
            return null;
        }
    }

    //setWifiApEnabled
    public boolean setWifiApEnabled(WifiConfiguration config, boolean enabled){
        try{
            return (boolean) setWifiApEnabled.invoke(wifiManager, config, enabled);
        }
        catch(Exception e){
            //should not happen
            Log.v("Error:", e.toString(), e);
            return false;
        }
    }

}
