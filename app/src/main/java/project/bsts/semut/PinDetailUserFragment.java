package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.data.FriendData;
import project.bsts.semut.helper.SessionManager;


public class PinDetailUserFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = PinDetailUserFragment.class.getSimpleName();
    private JSONObject data;
    private TextView nameText;
    private TextView levelText;
    private TextView pointText;
    private ImageView avatar;
    private Button addButton;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_pin_detail_user, container, false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        nameText = (TextView) view.findViewById(R.id.nameText);
        levelText = (TextView) view.findViewById(R.id.levelText);
        pointText = (TextView) view.findViewById(R.id.pointText);
        avatar = (ImageView) view.findViewById(R.id.imageView);
        addButton = (Button) view.findViewById(R.id.addButton);

        nameText.setText(titleCase(data.optString("Name")));
        levelText.setText("Newbie");
        pointText.setText("100");
        avatar.setImageResource(getResId("post_bg_" + data.optInt("AvatarID")));
        addButton.setOnClickListener(this);

        fetchUserData();

        return view;
    }

    private String titleCase(String str){
        StringBuilder s = new StringBuilder(str);
        s.replace(0, s.length(), s.toString().toLowerCase());
        s.setCharAt(0, Character.toTitleCase(s.charAt(0)));
        return s.toString();
    }

    private static int getResId(String resName) {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(resName);
            int drawableId = field.getInt(null);

            return drawableId;
        }
        catch (Exception e) {
            return -1;
        }
    }

    private void fetchUserData(){
        String tag_string_req = "user";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_USERPROFILE + "?UserID=" + data.optInt("ID"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Profile: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        JSONObject profile = jObj.optJSONObject("Profile");
                        levelText.setText(profile.optString("Poinlevel"));
                        pointText.setText(profile.optString("Poin"));
                        addButton.setVisibility(data.optBoolean("Friend")?View.GONE:View.VISIBLE);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getActivity().getApplicationContext(),
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
                Log.e(TAG, "Profile Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getActivity().getApplicationContext());
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

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
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
        String tag_string_req = "request_friends";

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
                        addButton.setVisibility(View.GONE);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getActivity().getApplicationContext(),
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
                if(error == null){
                    addButton.setVisibility(View.GONE);
                }
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getActivity().getApplicationContext());
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
                params.put("ReceiverID", ""+data.optInt("ID"));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
