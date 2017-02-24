package project.bsts.semut.map;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.UserMap;

public class MapViewComponent {

    public static int USER_MAP_COMPONENT = 0;
    public static int CCTV_MAP_COMPONENT = 1;
    public static int POLICE_MAP_COMPONENT = 2;
    public static int ACCIDENT_MAP_COMPONENT = 3;
    public static int TRAFFIC_MAP_COMPONENT = 4;
    public static int DISASTER_MAP_COMPONENT = 5;
    public static int CLOSURE_MAP_COMPONENT = 6;

    public static UserMap[] getUsers(int indexComponent, String jsonString){
        UserMap[] userMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray users = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            users = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Users");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        userMaps = new UserMap[users.length()];
        for (int i = 0; i <users.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                userMaps[i] = gson.fromJson(users.get(i).toString(), UserMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return userMaps;

    }


    public static CctvMap[] getCCTVs(int indexComponent, String jsonString){
        CctvMap[] cctvMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray users = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            users = new JSONObject(array.get(indexComponent).toString()).getJSONArray("CCTV");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cctvMaps = new CctvMap[users.length()];
        for (int i = 0; i <users.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                cctvMaps[i] = gson.fromJson(users.get(i).toString(), CctvMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return cctvMaps;

    }


    public static PoliceMap[] getPolicesPost(int indexComponent, String jsonString){
        PoliceMap[] policeMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray polices = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            polices = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Polices");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        policeMaps = new PoliceMap[polices.length()];
        for (int i = 0; i <polices.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                policeMaps[i] = gson.fromJson(polices.get(i).toString(), PoliceMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return policeMaps;

    }


    public static AccidentMap[] getAccident(int indexComponent, String jsonString){
        AccidentMap[] accidentMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray accidents = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            accidents = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Accidents");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        accidentMaps = new AccidentMap[accidents.length()];
        for (int i = 0; i <accidents.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                accidentMaps[i] = gson.fromJson(accidents.get(i).toString(), AccidentMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return accidentMaps;

    }


    public static TrafficMap[] getTraffic(int indexComponent, String jsonString){
        TrafficMap[] trafficMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray traffics = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            traffics = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Traffics");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        trafficMaps = new TrafficMap[traffics.length()];
        for (int i = 0; i <traffics.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                trafficMaps[i] = gson.fromJson(traffics.get(i).toString(), TrafficMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trafficMaps;

    }


    public static DisasterMap[] getDisaster(int indexComponent, String jsonString){
        DisasterMap[] disasterMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray disasters = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            disasters = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Disasters");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        disasterMaps = new DisasterMap[disasters.length()];
        for (int i = 0; i <disasters.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                disasterMaps[i] = gson.fromJson(disasters.get(i).toString(), DisasterMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return disasterMaps;

    }


    public static ClosureMap[] getClosure(int indexComponent, String jsonString){
        ClosureMap[] closureMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray closures = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            closures = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Closures");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        closureMaps = new ClosureMap[closures.length()];
        for (int i = 0; i <closures.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                closureMaps[i] = gson.fromJson(closures.get(i).toString(), ClosureMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return closureMaps;

    }


}
