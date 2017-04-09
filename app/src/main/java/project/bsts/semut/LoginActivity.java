package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;
import project.bsts.semut.connections.rest.IConnectionResponseHandler;
import project.bsts.semut.connections.rest.RequestRest;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.ui.LoadingIndicator;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.ShowSnackbar;

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult>, IConnectionResponseHandler {

    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.email)
    EditText emailEditText;
    @BindView(R.id.password)
    EditText passEditText;
    @BindView(R.id.skip_btn)
    Button skipBtn;
    @BindView(R.id.signup_btn)
    Button signupBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.radio_group_login_type)
    RadioRealButtonGroup mLoginTypeGroup;
    @BindView(R.id.radio_email)
    RadioRealButton mRadioEmail;
    @BindView(R.id.radio_phone)
    RadioRealButton mRadioPhone;
    @BindView(R.id.textInputEmail)
    TextInputLayout textInputLayout;

    CallbackManager callbackManager;
    LoginButton fbLoginBtn;
    LoadingIndicator loadingIndicator;
    private Context context;
    private RequestRest rest;
    private String TAG = this.getClass().getSimpleName();
    PreferenceManager preferenceManager;
    private int loginType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        context = this;
        preferenceManager = new PreferenceManager(context);
        if(preferenceManager.getBoolean(Constants.IS_LOGGED_IN)){
            toDashBoard();
        }
        loadingIndicator = new LoadingIndicator(context);
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn = (LoginButton)findViewById(R.id.loginButton);
        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        fbLoginBtn.registerCallback(callbackManager, this);
        loginBtn.setOnClickListener(view -> {
            if(emailEditText.getText().toString().equals("") || passEditText.getText().toString().equals("")){
                Snackbar.make(loginBtn, "Kolom tidak boleh kosong", Snackbar.LENGTH_LONG).show();
            }else doLogin(emailEditText.getText().toString(), passEditText.getText().toString());
        });

        signupBtn.setOnClickListener(v -> startActivity(new Intent(context, SignupActivity.class)));
        mLoginTypeGroup.setOnClickedButtonPosition(position -> {
            loginType = position;
            if(loginType == 0){
                textInputLayout.setHint("Email");
                emailEditText.setText("");
            }else {
                textInputLayout.setHint("Nomor Telepon");
                emailEditText.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void doLogin(String email, String password){
        rest = new RequestRest(context, this);
        rest.login(email, password, loginType);
        loadingIndicator.show();
    }

    //------------------------- Facebook login
    @Override
    public void onSuccess(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                (object, response) -> {
                    try {
                        Log.i("FB", String.valueOf(object));
                        Log.i("FB", response.getRawResponse());
                        String name=object.getString("name");
                        String email=object.getString("email");
                        Log.i("Login Success : ", name+", "+email);
                    } catch(JSONException ex) {
                        ex.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
        error.printStackTrace();
    }


    private void populateUserData(String profile, String sessionID){
        preferenceManager.save(profile, Constants.PREF_PROFILE);
        preferenceManager.save("{SessionID:"+sessionID+"}", Constants.PREF_SESSION_ID);
        preferenceManager.save(true, Constants.IS_LOGGED_IN);
        preferenceManager.apply();
        toDashBoard();
    }


    private void toDashBoard(){
        Intent intent = new Intent(context, SplashScreenActivity.class);
        startActivity(intent);
        finish();
    }

    //------------------------- success request
    @Override
    public void onSuccessRequest(String pResult, String type) {
        switch (type){
            case Constants.REST_USER_LOGIN:
                loadingIndicator.hide();
                Log.i(TAG, pResult);
                try {
                    JSONObject object = new JSONObject(pResult);
                    boolean success = (object.getBoolean("success"));
                    if(success) populateUserData(object.getJSONObject("Profile").toString(), object.getString("SessionID"));
                    else new ShowSnackbar(loginBtn).show(object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REST_ERROR:
                loadingIndicator.hide();
                break;
        }
    }
}