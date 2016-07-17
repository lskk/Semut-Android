package project.bsts.semut.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "Semut";
	
	private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_SKIP = "isSkip";
    private static final String KEY_SESSID = "Sessid";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LON = "longitude";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGEDIN, false);
	}

    public void setSkip(boolean isSkip) {

        editor.putBoolean(KEY_IS_SKIP, isSkip);

        // commit changes
        editor.commit();

        Log.d(TAG, "User skip login screen modified!");
    }

    public boolean isSkip(){
        return pref.getBoolean(KEY_IS_SKIP, false);
    }

    public void setSessid(Integer sessid) {

        editor.putInt(KEY_SESSID, sessid);

        // commit changes
        editor.commit();

        Log.d(TAG, "User session id modified!");
    }

    public Integer getSessid(){
        return pref.getInt(KEY_SESSID, 0);
    }

    public void setLocation(double lat, double lon){
        editor.putString(KEY_LAT,Double.toString(lat));
        editor.putString(KEY_LON,Double.toString(lon));
        editor.commit();
        Log.d(TAG,"Location changed");
    }

    public double getLatitude(){
        return Double.parseDouble(pref.getString(KEY_LAT,"-6.89597"));
    }

    public double getLongitude(){
        return Double.parseDouble(pref.getString(KEY_LON,"107.6155"));
    }
}
