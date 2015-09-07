package group107.wifiapp;

import android.net.wifi.WifiManager;
import android.provider.ContactsContract;

/**
 * Created by James on 8/13/2015.
 * This class is a singleton class which will store all the app settings
 * accessable from anywhere within the app
 */
public class AppData {

    private static AppData instance;
    private int sessionId;
    private String hotspotName;
    private String password;
    private int isUserConnected;
    private int numOfUsers;
    private boolean isWifiEnabled;
    private boolean isWifiHotspotEnabled;
    private double lat_startPoint;
    private double long_startPoint;
    private double lat_endPoint;
    private double long_endPoint;
    //start and end time is a string?
    private String startTime;
    private String endTime;
    private float dataAllowed;
    private float timeAllowed;


    private AppData(){
        //Constructor, settings to go here
        this.sessionId = sessionId;
        this.hotspotName = hotspotName;
        this.password = password;
        this.isUserConnected = isUserConnected;
        this.numOfUsers = numOfUsers;
        this.isWifiEnabled = isWifiEnabled;
        this.isWifiHotspotEnabled = isWifiHotspotEnabled;
        this.lat_startPoint = lat_startPoint;
        this.long_startPoint = long_startPoint;
        this.lat_endPoint = lat_endPoint;
        this.long_endPoint = long_endPoint;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataAllowed = dataAllowed;
        this.timeAllowed = timeAllowed;

    }

    //getters
    public int getSessionId() {
        return sessionId;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public String getPassword() {
        return password;
    }

    public int getIsUserConnected() {
        return isUserConnected;
    }

    public int getNumOfUsers() { return numOfUsers; }

    public boolean getIsWifiEnabled() {
        return isWifiEnabled;
    }

    public boolean getIsWifiHotspotEnabled() {
        return isWifiHotspotEnabled;
    }

    public double getLat_startPoint() { return lat_startPoint; }

    public double getLong_startPoint() { return long_startPoint; }

    public double getLat_endPoint() { return lat_endPoint; }

    public double getLong_endPoint() { return long_endPoint; }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public float getDataAllowed() {
        return dataAllowed;
    }

    public float getTimeAllowed() {
        return timeAllowed;
    }

    public static AppData getInstance() {

        if (instance == null){
            instance = new AppData();
        }

        return instance;
    }


    //setters
    public static void setInstance(AppData instance) {
        AppData.instance = instance;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public void setPassword(String password)  {
        this.password = password;
    }

    public void setIsUserConnected(int isUserConnected) {
        this.isUserConnected = isUserConnected;
    }

    public void setNumOfUsers(int numOfUsers) { this.numOfUsers = numOfUsers; }

    public void setIsWifiEnabled(boolean isWifiEnabled) {
        this.isWifiEnabled = isWifiEnabled;
    }

    public void setIsWifiHotspotEnabled(boolean isWifiHotspotEnabled) {
        this.isWifiHotspotEnabled = isWifiHotspotEnabled;
    }

    public void setLat_startPoint(double lat_startPoint){ this.lat_startPoint = lat_startPoint; }

    public void setLong_startPoint(double long_startPoint){ this.long_startPoint = long_startPoint; }

    public void setLat_endPoint(double lat_endPoint){ this.lat_endPoint = lat_endPoint; }

    public void setLong_endPoint(double long_endPoint) { this.long_endPoint = long_endPoint; }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDataAllowed(float dataAllowed) {
        this.dataAllowed = dataAllowed;
    }

    public void setTimeAllowed(float timeAllowed) {
        this.timeAllowed = timeAllowed;
    }

}
