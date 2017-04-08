package project.bsts.semut.helper;


import org.json.JSONException;
import org.json.JSONObject;

import project.bsts.semut.setup.Constants;

public class JSONRequest {
    public static String storeLocation(String SessionID, double Altitude, double Latitude,
                                     double Longitude, double Speed, String Date, int Radius, int Limit, String Item, int status){
        JSONObject storeObj = new JSONObject();
        try {
            storeObj.put("SessionID", SessionID);
            storeObj.put("Altitude", Altitude);
            storeObj.put("Latitude", Latitude);
            storeObj.put("Longitude", Longitude);
            storeObj.put("Speed", Speed);
            storeObj.put("Timespan", Date);
            storeObj.put("mapitem", Item);
            storeObj.put("StatusOnline", status);
            storeObj.put("Radius", Radius);
            storeObj.put("Limit", Limit);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeObj.toString();
    }

    public static String myLocation(double latitude, double longitude){
        JSONObject object = new JSONObject();
        try {
            object.put(Constants.ENTITY_LATITUDE, latitude);
            object.put(Constants.ENTITY_LONGITUDE, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
