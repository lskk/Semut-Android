/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
    private TextView btnBack;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
    private SQLiteHandler db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
	//	Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.activity_login);

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnBack = (TextView)findViewById(R.id.btn_back);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
        db = new SQLiteHandler(getApplicationContext());

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
                Integer session_id = session.getSessid();

				// Check for empty data in the form
				if (email.trim().length() > 0 && password.trim().length() > 0) {
					// login user
                    TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    String deviceid = tm.getDeviceId();
					checkLogin(email, password, deviceid, String.valueOf(session_id));
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();

			}
		});

        // Link to Prelogin Screen
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        PreloginActivity.class);
                startActivity(i);
                finish();
            }
        });

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password, final String duid, final String sessid) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.API_LOGIN, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Login Response: " + response);
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
							boolean success = jObj.getBoolean("success");

							// Check for error node in json
							if (success) {
                                Integer sessid = jObj.getInt("SessionID");
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
								String Barcode  = user.getString("Barcode");

                                // Inserting row in users table
                                db.addUser(ID, Name, Email, Gender, Birthday, Joindate, Poin, Poinlevel, Visibility, Avatar, Barcode);
                                // Create login session

                                session.setLogin(true);
                                session.setSkip(true);
                                session.setSessid(sessid);

                                Intent intent = new Intent(LoginActivity.this,
                                        SemutActivity.class);
                                startActivity(intent);
                                finish();

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
						Log.e(TAG, "Login Error: " + error.getMessage());
						hideDialog();
					}
				}) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", sessid);
                headers.put("deviceid", duid);
                return headers;
            }

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("Email", email);
				params.put("Password", password);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
