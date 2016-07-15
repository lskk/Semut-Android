package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
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


public class SearchFriendActivity extends Activity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private SearchView searchView;
    private ImageButton backButton;
    private ListView listView;
    private static final String TAG = SearchFriendActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_search_friend);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        listView = (ListView)findViewById(R.id.listView);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        String tag_string_req = "search_friends";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Search Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // friends
                        JSONArray arr = jObj.getJSONArray("Result");

                        ArrayList<FriendData> data = new ArrayList<FriendData>();
                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);
                            JSONObject relInfo = one.optJSONObject("RelationInfo");

                            FriendData item = new FriendData(Integer.parseInt(one.optString("ID")), one.optString("Name"), one.optString("Email"));
                            if(relInfo != null) {
                                String rtime = relInfo.getString("ResponseTime");
                                item.setIsRequest((relInfo.optBoolean("IsRequest") && rtime.length() < 12));
                                item.setRelationID(relInfo.optInt("RelationID"));
                            }
                            item.setNeedToAdd(!one.optBoolean("Friend"));
                            data.add(item);
                        }

                        adapter =  new FriendListAdapter(SearchFriendActivity.this, R.layout.friend_list_item, data);
                        listView.setAdapter(adapter);
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
                Log.e(TAG, "Search Error: " + error.getMessage());
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
                params.put("Key", query);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void onClick(View v) {
        finish();
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
                        adapter.notifyDataSetChanged();
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

    public void onClickAddButton(View view){
        final FriendData data = (FriendData)view.getTag();
        String tag_string_req = "req_friends";

        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_REQFRIEND, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Add: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // friends
                        data.setNeedToAdd(false);
                        adapter.notifyDataSetChanged();
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
                Log.e(TAG, "Add Friends Error: " + error.getMessage());
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
                params.put("ReceiverID", ""+data.getId());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
