/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package project.bsts.semut;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;

public class RegisterActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnRegister;
    private TextView btnBack;
	private EditText inputFullName;
	private EditText inputEmail;
	private EditText inputPassword;
	private EditText retypePassword;
    private EditText inputPhone;
    private EditText inputBirthday;
    private RadioGroup gender;
    private RadioButton inputGender;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;
    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
		setContentView(R.layout.activity_register);

		inputFullName = (EditText) findViewById(R.id.name);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputBirthday = (EditText) findViewById(R.id.birthday);
        retypePassword = (EditText) findViewById(R.id.retypepassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBack = (TextView) findViewById(R.id.btn_back);
        gender = (RadioGroup) findViewById(R.id.gender);
        inputGender = (RadioButton)findViewById(R.id.gendermale);
        gender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.gendermale:
                        // do operations specific to this selection
                        inputGender = (RadioButton)findViewById(R.id.gendermale);
                        break;

                    case R.id.genderfemale:
                        inputGender = (RadioButton)findViewById(R.id.genderfemale);
                        break;

                }

            }
        });
        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

        inputBirthday.setFocusable(false);
        inputBirthday.setFocusableInTouchMode(false);
        inputBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });


		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
                String retypepass = retypePassword.getText().toString();
				String password = inputPassword.getText().toString();
                String phone = inputPhone.getText().toString();
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                String inputsex = inputGender.getTag().toString();
                String bd = year+"-"+month+"-"+day;

                if (!retypepass.equals(password)){
                    Toast.makeText(getApplicationContext(),
                            "Password not match. Please enter valid password!", Toast.LENGTH_LONG)
                            .show();
                }else if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
					registerUser(name, email,inputsex,bd,phone, password, deviceid);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter your details!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// Link to Login Screen
		btnBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            inputBirthday.setText(year+"-"+month+"-"+day);
        }
    };

	private void registerUser(final String name, final String email, final String gender, final String birth, final String phone, final String password, final String duid) {
		// Tag used to cancel the request
		String tag_string_req = "req_register";

		pDialog.setMessage("Registering ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.API_REGISTER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Register Response: " + response);
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
							boolean success = jObj.getBoolean("success");
                            if (success) {
                                String sess_id = jObj.getString("sessID");
                                Integer sessid = Integer.valueOf(sess_id);
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
                                db.addUser(ID,Name,Email,Gender,Birthday,Joindate,Poin,Poinlevel,Visibility,Avatar, Barcode);

                                // Create login session
                                session.setLogin(true);
                                session.setSessid(sessid);
                                session.setSkip(true);

                                Intent intent = new Intent(RegisterActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
							} else {

                                String errorMsg = jObj.getString("Message");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());
						hideDialog();
					}
				}) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", "0");
                headers.put("deviceid", duid);
                return headers;
            }

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("Name", name);
				params.put("Email", email);
				params.put("Gender", gender);
				params.put("Birthday", birth);
				params.put("PhoneNumber", phone);
				params.put("Password", password);
				params.put("CountryCode", "62");

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
}
