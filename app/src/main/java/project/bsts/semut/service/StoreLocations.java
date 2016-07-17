package project.bsts.semut.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;

public class StoreLocations extends Service {
    private static final String TAG = StoreLocations.class.getSimpleName();
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private SessionManager session;
    private SQLiteHandler db;
    private TelephonyManager tm;
    private String deviceID;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        db = new SQLiteHandler(context);
        session = new SessionManager(context);
        Log.i("STATUS", "Store Loc Service Created");
        handler = new Handler();
        tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        final Runnable r = new Runnable() {
            public void run() {
                int countloc = db.countLocation();
                if (countloc > 1){
                    final Integer sessid = session.getSessid();
                    final JSONArray locations = db.getLocation();
                    InsertLocations(String.valueOf(sessid), locations, deviceID);
                }
                handler.postDelayed(this, 60000);
            }
        };
        handler.postDelayed(r, 60000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(runnable);
        Log.i("STATUS", "Store Loc Service Stoped");
    }

    public void InsertLocations(final String sessid,final JSONArray locations, final String deviceid) {
        // Tag used to cancel the request
        String tag_string_insertloc = "store_loc";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_STORELOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Store Loc Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");
                    if (success) {
                        db.deleteLocations();
                        Log.d("STATUS", Msg);
                    } else {
                        Log.d("STATUS", Msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid",sessid);
                headers.put("deviceid", deviceid);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Location", locations.toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_insertloc);
    }

}