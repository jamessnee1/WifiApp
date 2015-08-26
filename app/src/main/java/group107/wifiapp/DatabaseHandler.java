package group107.wifiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by James on 8/15/2015.
 * Maintains methods for SQLite database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Hotspot.db";
    public static final String TABLE_NAME = "hotspots";
    public static final String COLUMN1 = "ID";
    public static final String COLUMN2 = "HOTSPOT_NAME";
    public static final String COLUMN3 = "PASSWORD";
    public static final String COLUMN4 = "NUMOFUSERS";
    public static final String COLUMN5 = "ISWIFIENABLED";
    public static final String COLUMN6 = "ISWIFIHOTSPOTENABLED";
    public static final String COLUMN7 = "LAT_STARTPOINT";
    public static final String COLUMN8 = "LONG_STARTPOINT";
    public static final String COLUMN9 = "LAT_ENDPOINT";
    public static final String COLUMN10 = "LONG_ENDPOINT";
    public static final String COLUMN11 = "STARTTIME";
    public static final String COLUMN12 = "ENDTIME";
    public static final String COLUMN13 = "DATAALLOWED";
    public static final String COLUMN14 = "TIMEALLOWED";

    public DatabaseHandler(Context context) {
        //context, name, version
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE IF NOT EXISTS should be used here to ensure we don't lose data. For now, this is just for testing.
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, HOTSPOT_NAME TEXT, " +
                "PASSWORD TEXT, NUMOFUSERS INT, ISWIFIENABLED INTEGER, ISWIFIHOTSPOTENABLED INTEGER, LAT_STARTPOINT TEXT, LONG_STARTPOINT TEXT," +
                "LAT_ENDPOINT TEXT, LONG_ENDPOINT TEXT, STARTTIME TEXT, ENDTIME TEXT, " +
                "DATAALLOWED TEXT, TIMEALLOWED TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    //method to insert data
    public boolean insertData(){

        //This method handles insertion of data, we can always add and remove fields when needed.
        //Initially we want to only call this after all the settings have been input and the hotspot
        //is fully created with all parameters set. The idea is to insert all parameters as they are
        //created into the AppData class, and then use the getters to grab them here and put them into the database.
        //The AppData class will have the current hotspot data, where as the database will have the saved hotspot data.
        //Return true: method was successful. Return false: something went wrong and we can throw
        //the appropriate error Dialog.

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN2, AppData.getInstance().getHotspotName());
        contentValues.put(COLUMN3, AppData.getInstance().getPassword());
        contentValues.put(COLUMN4, AppData.getInstance().getNumOfUsers());
        contentValues.put(COLUMN5, AppData.getInstance().getIsWifiEnabled());
        contentValues.put(COLUMN6, AppData.getInstance().getIsWifiHotspotEnabled());
        contentValues.put(COLUMN7, AppData.getInstance().getLat_startPoint());
        contentValues.put(COLUMN8, AppData.getInstance().getLong_startPoint());
        contentValues.put(COLUMN9, AppData.getInstance().getLat_endPoint());
        contentValues.put(COLUMN10, AppData.getInstance().getLong_endPoint());
        contentValues.put(COLUMN11, AppData.getInstance().getStartTime());
        contentValues.put(COLUMN12, AppData.getInstance().getEndTime());
        contentValues.put(COLUMN13, AppData.getInstance().getDataAllowed());
        contentValues.put(COLUMN14, AppData.getInstance().getTimeAllowed());

        long result = db.insert(TABLE_NAME, null, contentValues);

        //error checking
        if (result == -1){
            return false;
        }

        return true;
    }
}
