package project.bsts.semut.map;


import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project.bsts.semut.pojo.mapview.UserMap;

public class MapViewComponent {

    public static int USER_MAP_COMPONENT = 0;

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
                userMaps[i] = new Gson().fromJson(users.get(i).toString(), UserMap.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return userMaps;

    }


}
