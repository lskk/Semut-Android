package project.bsts.semut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import project.bsts.semut.adapter.PreloginAdapter;
import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;


public class PreloginActivity extends FragmentActivity implements FacebookCallback<LoginResult> {
    private Button toLogin;
    private com.facebook.login.widget.LoginButton fbLogin;
    private Button skip;
    private SessionManager session;
    private ViewPager preloginPager;
    private PreloginAdapter preAdapter;
    private JSONObject userData;
    private static final String TAG = PreloginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    ImageView image1;
    ImageView image2;
    ImageView image3;
    Integer currentPage;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_prelogin);
        session = new SessionManager(getApplicationContext());
        preloginPager = (ViewPager) findViewById(R.id.pagerprelogin);
        image1 = (ImageView)findViewById(R.id.imgprelogin1);
        image2 = (ImageView)findViewById(R.id.imgprelogin2);
        image3 = (ImageView)findViewById(R.id.imgprelogin3);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        db = new SQLiteHandler(getApplicationContext());

        toLogin = (Button)findViewById(R.id.btnLinkToLogin);
        skip = (Button)findViewById(R.id.btn_skip);
        toLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent ilogin = new Intent(PreloginActivity.this,LoginActivity.class);
                startActivity(ilogin);
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                session.setSkip(true);
                Intent ifront = new Intent(PreloginActivity.this, FrontActivity.class);
                startActivity(ifront);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        fbLogin = (com.facebook.login.widget.LoginButton)findViewById(R.id.loginfb_button);
        fbLogin.setReadPermissions("public_profile", "user_friends", "email");
        fbLogin.registerCallback(callbackManager, this);

        preAdapter = new PreloginAdapter(getSupportFragmentManager());

        preloginPager.setAdapter(preAdapter);
        currentPage = preloginPager.getCurrentItem();
        preloginPager.setCurrentItem(currentPage, true);

        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == preAdapter.getCount()) {
                    currentPage = 0;
                }
                preloginPager.setCurrentItem(currentPage++, true);


            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 7000);
        preloginPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    image2.setVisibility(View.GONE);
                    image3.setVisibility(View.GONE);
                    image1.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    image1.setVisibility(View.GONE);
                    image3.setVisibility(View.GONE);
                    image2.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    image2.setVisibility(View.GONE);
                    image1.setVisibility(View.GONE);
                    image3.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    // callback facebook
    @Override
    public void onSuccess(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        userData = object;

                        submitLogin();

                        LoginManager loginManager = LoginManager.getInstance();
                        loginManager.logOut();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        Log.d("Facebook ", "Canceled");
        Toast.makeText(getApplicationContext(),
                "Facebook login canceled.", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onError(FacebookException error) {
        Log.d("Facebook Error ", error.getMessage());
        Toast.makeText(getApplicationContext(),
                "Facebook login error "+error.getMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void submitLogin(){
        String tag_string_req = "loginfb_friends";

        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_LOGINFB, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "LoginFB: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        hideDialog();

                        Integer sessid = jObj.getInt("SessionID");
                        JSONObject user = jObj.getJSONObject("Profile");
                        String ID = user.getString("ID");
                        String Name = user.getString("Name");
                        String Email = user.getString("Email");
                        String Gender = user.getString("Gender");
                        String Birthday = user.getString("Birthday");
                        String Joindate = user.getString("Joindate");
                        String Poin = user.getString("Poin");
                        String Poinlevel = user.getString("PoinLevel");
                        String Visibility = user.getString("Visibility");
                        String Avatar = user.getString("AvatarID");
                        String Barcode = user.getString("Barcode");

                        // Inserting row in users table
                        db.addUser(ID, Name, Email, Gender, Birthday, Joindate, Poin, Poinlevel, Visibility, Avatar, Barcode);
                        // Create login session

                        session.setLogin(true);
                        session.setSkip(true);
                        session.setSessid(sessid);

                        Intent intent = new Intent(PreloginActivity.this, SemutActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        signup();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "LoginFB: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("facebookID", "fb"+ userData.optString("id"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void signup(){
        String tag_string_req = "signupfb_friends";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_REGFB, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SignupFB: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        hideDialog();

                        Integer sessid = jObj.getInt("sessID");
                        JSONObject user = jObj.getJSONObject("Profile");
                        String ID = user.getString("ID");
                        String Name = user.getString("Name");
                        String Email = user.getString("Email");
                        String Gender = user.getString("Gender");
                        String Birthday = user.getString("Birthday");
                        String Joindate = user.getString("Joindate");
                        String Poin = user.getString("Poin");
                        String Poinlevel = user.getString("Poinlevel");
                        String Visibility = user.getString("Visibility");
                        String Avatar = user.getString("AvatarID");
                        String Barcode = user.getString("Barcode");

                        // Inserting row in users table
                        db.addUser(ID, Name, Email, Gender, Birthday, Joindate, Poin, Poinlevel, Visibility, Avatar, Barcode);
                        // Create login session

                        session.setLogin(true);
                        session.setSkip(true);
                        session.setSessid(sessid);

                        Intent intent = new Intent(PreloginActivity.this, SemutActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "SignupFB: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("facebookID", "fb"+ userData.optString("id"));
                params.put("Name", userData.optString("name"));
                params.put("Email", userData.optString("email"));
                params.put("Gender", userData.optString("gender").equals("male")?"1":"2");
                params.put("Birthday", "0000-00-00");
                Log.d("SignupFB params=", params.toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
