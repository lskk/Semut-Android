package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SessionManager;


public class CctvActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = CctvActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ImageButton backButton;
    private ArrayList<HashMap<String, String>> data;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_cctv);

        data = new ArrayList<HashMap<String, String>>();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        backButton = (ImageButton)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        list = (ListView)findViewById(R.id.listView);
        list.setOnItemClickListener(this);

        fetchData();
    }

    private void fetchData() {
        // Tag used to cancel the request
        String tag_string_req = "req_cctv";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_CCTV + "1", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CCTV Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        JSONArray arr = jObj.getJSONArray("Result");

                        for (int i=0; i<arr.length(); i++) {
                            JSONObject one = arr.getJSONObject(i);

                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("name", one.optString("Name"));
                            item.put("subname", one.optString("City") + "-" + one.optString("Province") + " " + one.optString("Country"));
                            item.put("url", one.optString("Video"));

                            data.add(item);
                        }

                        final String[] fromMapKey = new String[] {"name", "subname"};
                        final int[] toLayoutId = new int[] {R.id.textView1, R.id.textView2};

                        SimpleAdapter adapter =  new SimpleAdapter(getApplicationContext(), data, R.layout.cctv_list, fromMapKey, toLayoutId);

                        list.setAdapter(adapter);
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
                Log.e(TAG, "CCTV Error: " + error.getMessage());
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

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> item = data.get(position);

        Intent intent = new Intent(this, CctvPlayerActivity.class);
        intent.putExtra("urlStr", item.get("url"));
        startActivity(intent);
    }
}
