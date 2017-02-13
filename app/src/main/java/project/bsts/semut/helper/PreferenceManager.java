package project.bsts.semut.helper;

import android.content.Context;
import android.content.SharedPreferences;

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
}
