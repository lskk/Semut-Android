package project.bsts.semut.map;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.bsts.semut.pojo.mapview.CCTVLocation;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.UserMap;

public class MapViewComponent {

    public static int USER_MAP_COMPONENT = 0;
    public static int CCTV_MAP_COMPONENT = 1;

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


}
