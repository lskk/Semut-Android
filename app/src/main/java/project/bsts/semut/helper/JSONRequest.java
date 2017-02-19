package project.bsts.semut.helper;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import project.bsts.semut.setup.Constants;

public class JSONRequest {
    public static String storeLocation(String SessionID, double Altitude, double Latitude,
                                     double Longitude, double Speed, String Date, int Radius, int Limit, String Item){
        JSONObject mainObj = new JSONObject();
        JSONObject storeObj = new JSONObject();
        JSONObject mapviewObj = new JSONObject();
        try {
            storeObj.put("SessionID", SessionID);
            storeObj.put("Altitude", Altitude);
            storeObj.put("Latitude", Latitude);
            storeObj.put("Longitude", Longitude);
            storeObj.put("Speed", Speed);
            storeObj.put("Date", Date);
            mainObj.put("store", storeObj);
            mapviewObj.put("SessionID", SessionID);
            mapviewObj.put("Radius", Radius*1000);
            mapviewObj.put("Limit", Limit);
            mapviewObj.put("Latitude", Latitude);
            mapviewObj.put("Longitude", Longitude);
            mapviewObj.put("Item", Item);
            mainObj.put("mapview", mapviewObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mainObj.toString();
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
