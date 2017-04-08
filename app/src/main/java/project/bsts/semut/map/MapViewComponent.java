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
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.Tracker;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.TranspostMap;
import project.bsts.semut.pojo.mapview.UserMap;

public class MapViewComponent {

    public static int USER_MAP_COMPONENT = 0;
    public static int CCTV_MAP_COMPONENT = 1;
    public static final int POLICE_MAP_COMPONENT = 2;
    public static final int ACCIDENT_MAP_COMPONENT = 3;
    public static final int TRAFFIC_MAP_COMPONENT = 4;
    public static final int DISASTER_MAP_COMPONENT = 5;
    public static final int CLOSURE_MAP_COMPONENT = 6;
    public static final int OTHER_MAP_COMPONENT = 7;
    //public static final int COMMUTER_MAP_COMPONENT = 8;
    public static final int TRACKER_MAP_COMPONENT = 8;
    public static final int TRANSPORTATION_POST_MAP_COMPONENT = 9;

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


    public static TranspostMap[] getTransPost(int indexComponent, String jsonString){
        TranspostMap[] transpostMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray polices = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            polices = new JSONObject(array.get(indexComponent).toString()).getJSONArray("PublicTran");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        transpostMaps = new TranspostMap[polices.length()];
        for (int i = 0; i <polices.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                transpostMaps[i] = gson.fromJson(polices.get(i).toString(), TranspostMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return transpostMaps;

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


    public static OtherMap[] getOther(int indexComponent, String jsonString){
        OtherMap[] otherMaps;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray others = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            others = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Other");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        otherMaps = new OtherMap[others.length()];
        for (int i = 0; i <others.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                otherMaps[i] = gson.fromJson(others.get(i).toString(), OtherMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return otherMaps;

    }


    public static Tracker[] getTrackers(int indexComponent, String jsonString){
        Tracker[] trackers;
        JSONObject object = null;
        JSONArray array = null;
        JSONArray trackerArr = null;
        try {
            object = new JSONObject(jsonString);
            array = object.getJSONArray("results");
            trackerArr = new JSONObject(array.get(indexComponent).toString()).getJSONArray("Trackers");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        trackers = new Tracker[trackerArr.length()];
        for (int i = 0; i <trackerArr.length(); i++){
            try {
                Gson gson = new GsonBuilder().serializeNulls().create();
                trackers[i] = gson.fromJson(trackerArr.get(i).toString(), Tracker.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trackers;

    }


}
