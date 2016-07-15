package project.bsts.semut;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitTagFragment extends Fragment implements TextWatcher {
    private TextView titleText;
    private TextView dateText;
    private TextView counterText;
    private EditText remarks;
    private ImageView thumb;
    private ImageButton closeButton;
    private ImageButton backButton;
    private Button submitButton;

    private int postID;
    private int subPostID;
    Date currentDate;
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View view = inflater.inflate(R.layout.fragment_submit_tag, container, false);

        titleText = (TextView)view.findViewById(R.id.title);
        dateText = (TextView)view.findViewById(R.id.date);
        counterText = (TextView)view.findViewById(R.id.counter);
        remarks = (EditText)view.findViewById(R.id.remarks);
        remarks.addTextChangedListener(this);
        thumb = (ImageView)view.findViewById(R.id.thumb);
        submitButton = (Button)view.findViewById(R.id.submitButton);

        currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEEE, dd MMWW yyyy HH:mm:ss");
        String formattedCurrentDate = format.format(currentDate);
        dateText.setText(formattedCurrentDate);

        HashMap<String, String> info = getPostInfo();
        titleText.setText(info.get("text"));
        thumb.setImageResource(Integer.parseInt(info.get("res")));

        backButton = (ImageButton) view.findViewById(R.id.backButton);
        closeButton = (ImageButton) view.findViewById(R.id.closeButton);

        session = new SessionManager(getActivity().getApplicationContext());

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();;
            }
        });

        return view;
    }

    public void back(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    private HashMap<String, String> getPostInfo(){
        int[] childNums = {3, 2, 3, 2, 5, 4};
        String[] texts = {"Normal Traffic", "Heavy Traffic", "Standstill Traffic", "Police Patrol", "Police Raid", "Incident", "Incident with Victim", "Vehicle Broke Down", "Fallen Tree", "Flood", "Minor Road Repair", "Medium Road Repair", "Event", "Construction", "Demonstration", "No Sign", "Damaged Road", "Bus Stop", "Crowded Place"};

        int postID = 0;
        for(int i=0; i<this.postID; i++){
            postID += childNums[i];
        }
        postID += this.subPostID;

        int resID = getResId("menu_post_sub_" + twoDigitString(postID+1));

        HashMap<String, String> map = new HashMap();
        map.put("res", ""+resID);
        map.put("text", texts[postID]);
        map.put("id", ""+(postID+1));

        return map;
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

    /**
     * function to verify login details in mysql db
     * */
    private void submit() {
        // Tag used to cancel the request
        String tag_string_req = "req_tag";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_TAGING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Tagging ", "Tag Response: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tagging ", "Tag Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();
                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> info = getPostInfo();

                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Map<String, String> params = new HashMap<String, String>();
                params.put("Type", info.get("id"));
                params.put("Times", format.format(currentDate));
                params.put("Latitude", String.valueOf(session.getLatitude()));
                params.put("Longitude", String.valueOf(session.getLongitude()));
                params.put("Description", String.valueOf(remarks.getText()));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        getActivity().finish();
    }

    // setter dan getter
    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getSubPostID() {
        return subPostID;
    }

    public void setSubPostID(int subPostID) {
        this.subPostID = subPostID;
    }

    // listener text
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counterText.setText(s.length() + " of 128");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
