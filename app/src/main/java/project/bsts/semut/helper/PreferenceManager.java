package project.bsts.semut.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import project.bsts.semut.setup.Constants;

public class PreferenceManager {

    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public PreferenceManager(Context _context){
        context = _context;
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void save(String val, String prefName){
        editor.putString(prefName, val);
    }

    public void save(int val, String prefname){
        editor.putInt(prefname, val);
    }

    public void save(float val, String prefname){
        editor.putFloat(prefname, val);
    }


    public void save(boolean val, String prefName){
        editor.putBoolean(prefName, val);
    }

    public void apply(){
        editor.commit();
    }


    public boolean getBoolean(String name){
        return sharedPreferences.getBoolean(name, false);
    }

    public String getString(String name){
        return sharedPreferences.getString(name, "");
    }

    public int getInt(String name, int initial){
        return sharedPreferences.getInt(name, initial);
    }

    public float getFloat(String name, float initial){
        return sharedPreferences.getFloat(name, initial);
    }

    public double getDouble(String name, int initial){
        return (double)sharedPreferences.getFloat(name, initial);
    }
}
