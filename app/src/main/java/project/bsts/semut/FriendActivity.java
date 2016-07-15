package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.adapter.FriendListAdapter;
import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.data.FriendData;
import project.bsts.semut.helper.SessionManager;

public class FriendActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    private ImageButton backButton;
    private ImageButton searchButton;
    private ListView listView;
    private static final String TAG = FriendActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private FriendListAdapter adapterFriends;
    private FriendListAdapter adapterSent;
    private FriendListAdapter adapterReceived;
    private SegmentedRadioGroup segment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_friend);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        searchButton = (ImageButton)findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.listView);
        segment = (SegmentedRadioGroup)findViewById(R.id.segment_text);
        segment.setOnCheckedChangeListener(this);

        fetchData();
    }

    private void fetchData() {
        // Tag used to cancel the request
        String tag_string_req = "req_friends";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_FRIENDLIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Friends Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // friends
                        JSONArray arr = jObj.getJSONArray("Friends");

                        ArrayList<FriendData> data = new ArrayList<FriendData>();
                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);

                            FriendData item = new FriendData(Integer.parseInt(one.optString("ID")), one.optString("Name"), one.optString("Email"));
                            data.add(item);
                        }

                        adapterFriends =  new FriendListAdapter(FriendActivity.this, R.layout.friend_list_item, data);

                        // sent
                        arr = jObj.getJSONArray("Sent");

                        data = new ArrayList<FriendData>();
                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);

                            FriendData item = new FriendData(Integer.parseInt(one.optString("ID")), one.optString("Name"), one.optString("Email"));
                            data.add(item);
                        }

                        adapterSent =  new FriendListAdapter(FriendActivity.this, R.layout.friend_list_item, data);

                        // received
                        arr = jObj.getJSONArray("Received");

                        data = new ArrayList<FriendData>();
                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);

                            FriendData item = new FriendData(Integer.parseInt(one.optString("ID")), one.optString("Name"), one.optString("Email"));
                            item.setRelationID(one.optInt("RelationID"));
                            item.setIsRequest(true);
                            data.add(item);
                        }

                        adapterReceived =  new FriendListAdapter(FriendActivity.this, R.layout.friend_list_item, data);

                        updateList();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Friends Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getApplicationContext());
                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void updateList(){
        if(segment.getCheckedRadioButtonId() == R.id.button_one){
            listView.setAdapter(adapterFriends);
        }else if(segment.getCheckedRadioButtonId() == R.id.button_two){
            listView.setAdapter(adapterSent);
        }else if(segment.getCheckedRadioButtonId() == R.id.button_three){
            listView.setAdapter(adapterReceived);
        }
    }

    public void onClickAcceptButton(View view){
        final FriendData data = (FriendData)view.getTag();
        String tag_string_req = "accept_friends";
        Log.d("relation id ", ""+data.getRelationID());

        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_ACCEPTFRIEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Accept: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // friends
                        data.setIsRequest(false);
                        adapterReceived.remove(data);
                        adapterFriends.add(data);
                        updateList();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Friends Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getApplicationContext());
                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("RelationID", ""+data.getRelationID());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        if(v == backButton) {
            finish();
        }else if(v == searchButton){
            Intent search = new Intent(getApplicationContext(), SearchFriendActivity.class);
            startActivity(search);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        updateList();
    }
}
