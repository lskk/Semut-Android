package project.bsts.semut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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
import project.bsts.semut.helper.SessionManager;

public class PinDetailTagFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {
    private static final String TAG = PinDetailUserFragment.class.getSimpleName();
    private JSONObject data;
    private TextView nameText;
    private TextView tagerText;
    private TextView atText;
    private TextView infoText;
    private ImageView image;
    private Button retagButton;
    private Button reportButton;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_pin_detail_tag, container, false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");

        nameText = (TextView) view.findViewById(R.id.nameText);
        tagerText = (TextView) view.findViewById(R.id.tagerText);
        atText = (TextView) view.findViewById(R.id.atText);
        infoText = (TextView) view.findViewById(R.id.infoText);
        image = (ImageView) view.findViewById(R.id.imageView);
        retagButton = (Button) view.findViewById(R.id.retagButton);
        reportButton = (Button) view.findViewById(R.id.reportButton);

        nameText.setText(data.optString("TypeName"));
        tagerText.setText(data.optString("PostBy"));
        atText.setText(data.optString("Time"));
        infoText.setText(data.optString("Description"));
        image.setImageResource(getResId("menu_post_sub_" + twoDigitString(data.optInt("Type"))));
        reportButton.setOnClickListener(this);
        retagButton.setOnClickListener(this);

        return view;
    }

    private String twoDigitString(int a){
        if(a < 10) return "0"+a;

        return "" + a;
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
        if(v == retagButton){
            String tag_string_req = "retag";

            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.API_RETAG, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Retag: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean success = jObj.getBoolean("success");

                        // Check for error node in json
                        if (success) {
                            retagButton.setEnabled(false);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "ReTag Succeeded.", Toast.LENGTH_LONG).show();
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
                    Log.e(TAG, "Retag Error: " + error.getMessage());
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
                    params.put("PostID", ""+data.optInt("ID"));

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }else{
            AlertDialog alert = new AlertDialog.Builder(getActivity())
                    //set message, title, and icon
                    .setTitle("Report")
                    .setMessage("Report this Tag as invalid?")
                    .setPositiveButton("Report", this)
                    .setNegativeButton("Cancel", this)
                    .create();
            alert.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(Math.abs(which) == 1){
            String tag_string_req = "report";

            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.API_REPORTTAG, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Report: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean success = jObj.getBoolean("success");

                        // Check for error node in json
                        if (success) {
                            reportButton.setEnabled(false);
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Thank you for reporting this Tag.", Toast.LENGTH_LONG).show();
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
                    Log.e(TAG, "Report Error: " + error.getMessage());
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
                    params.put("PostID", ""+data.optInt("ID"));

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}
