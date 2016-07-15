/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package project.bsts.semut.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "semut";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "myID";
	private static final String KEY_NAME = "Name";
	private static final String KEY_EMAIL = "Email";
	private static final String KEY_GENDER = "Gender";
	private static final String KEY_BIRTHDAY = "Birthday";
    private static final String KEY_JOINDATE = "Joindate";
    private static final String KEY_POIN = "Poin";
    private static final String KEY_POINLEVEL = "Poinlevel";
    private static final String KEY_VISIBLE = "Visibility";
    private static final String KEY_AVATAR = "AvatarID";
    private static final String KEY_BARCODE = "Barcode";

    // Table user location
    private static final String TABLE_LOCATION = "location";

    //Location table columns names
    private static final String LOC_ALTITUDE = "Altitude";
    private static final String LOC_LATITUDE = "Latitude";
    private static final String LOC_LONGITUDE = "Longitude";
    private static final String LOC_SPEED = "Speed";
    private static final String LOC_TIME = "Timespan";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_GENDER + " INTEGER,"
				+ KEY_BIRTHDAY + " DATE,"
                + KEY_JOINDATE + " DATETIME,"
                + KEY_POIN + " INTEGER,"
                + KEY_POINLEVEL + " INTEGER,"
                + KEY_VISIBLE + " INTEGER,"
                + KEY_AVATAR + " INTEGER, "
                + KEY_BARCODE + " TEXT "
                + ")";
		db.execSQL(CREATE_LOGIN_TABLE);
		Log.d(TAG, "Table user created");
        String CREATE_TABLE_LOCATION =
                "CREATE TABLE "+ TABLE_LOCATION + "( "
                        + LOC_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                        + LOC_ALTITUDE + " DOUBLE,"
                        + LOC_LATITUDE + " DOUBLE,"
                        + LOC_LONGITUDE + " DOUBLE,"
                        + LOC_SPEED + " DOUBLE"
                        + ")";
        db.execSQL(CREATE_TABLE_LOCATION);
        Log.d(TAG, "Table location created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String myid, String name, String email, String gender, String birth,
                        String joindate, String poin, String poinlevel, String visible, String avatar, String barcode) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put(KEY_ID,myid);
		values.put(KEY_NAME, name);
		values.put(KEY_EMAIL, email);
        values.put(KEY_GENDER, gender);
        values.put(KEY_BIRTHDAY, birth);
        values.put(KEY_JOINDATE, joindate);
        values.put(KEY_POIN, poin);
        values.put(KEY_POINLEVEL, poinlevel);
        values.put(KEY_VISIBLE, visible);
        values.put(KEY_AVATAR, avatar);
        values.put(KEY_BARCODE, barcode);

		// Inserting Row
        db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "User inserted");
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
            user.put("ID",cursor.getString(0));
			user.put("Name", cursor.getString(1));
			user.put("Email", cursor.getString(2));
			user.put("Gender", cursor.getString(3));
			user.put("Birthday", cursor.getString(4));
            user.put("Joindate", cursor.getString(5));
            user.put("Poin", cursor.getString(6));
            user.put("Poinlevel", cursor.getString(7));
            user.put("Visibility", cursor.getString(8));
            user.put("AvatarID", cursor.getString(9));
            user.put("Barcode", cursor.getString(10));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching User data from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Getting user login status return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

    public void updateBarcodeUsers(String barcode, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, barcode);

        int result = db.update(TABLE_USER, values, "myID=? ", args);
        if(result==1){
            Log.i("Update Barcode","Berhasil");
        }else{
            Log.i("Update Barcode","Gagal");
        }
        db.close();
    }

    /**
     * Storing location to table location
     * */
    public void addLocation(double altitude, double latitude, double longitude, double speed, String timespan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOC_ALTITUDE, altitude);
        values.put(LOC_LATITUDE, latitude);
        values.put(LOC_LONGITUDE, longitude);
        values.put(LOC_SPEED, speed);
        values.put(LOC_TIME,timespan);

        db.insert(TABLE_LOCATION, null, values);
        db.close();

        Log.d(TAG, "Location inserted");

    }

    public int countLocation() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public JSONArray getLocation(){

        ArrayList<HashMap<String, String>> locs = new ArrayList<>();
        HashMap<String, String> itemloc = new HashMap<>();
        String selectQuery = "SELECT * FROM "+ TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray resultSet = new JSONArray();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                JSONObject rowObject = new JSONObject();
                try {
                    rowObject.put(LOC_TIME, cursor.getString(0));
                    rowObject.put(LOC_ALTITUDE, cursor.getString(1));
                    rowObject.put(LOC_LATITUDE, cursor.getString(2));
                    rowObject.put(LOC_LONGITUDE, cursor.getString(3));
                    rowObject.put(LOC_SPEED, cursor.getString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultSet.put(rowObject);
                Log.d(TAG, "Locations: " + cursor.getString(0) + " | " + cursor.getString(1) + " | " + cursor.getString(2) + " | " + cursor.getString(3) + " | " + cursor.getString(4));
            } while (cursor.moveToNext());
        }
        db.close();
        Log.d(TAG, "Locations: " + resultSet.toString());
        return resultSet;
    }

    public void deleteLocations(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOCATION,null,null);
        db.close();

        Log.d(TAG, "Locations data empty");
    }

}
