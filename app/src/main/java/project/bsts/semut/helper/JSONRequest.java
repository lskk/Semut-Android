package project.bsts.semut.helper;


import org.json.JSONException;
import org.json.JSONObject;

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
            mapviewObj.put("Radius", Radius);
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
}
