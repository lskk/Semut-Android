package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import io.github.yuweiguocn.lib.squareloading.SquareLoading;
import project.bsts.semut.connections.rest.IConnectionResponseHandler;
import project.bsts.semut.connections.rest.RequestRest;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.pojo.Profile;
import project.bsts.semut.pojo.Session;
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

    CallbackManager callbackManager;
    LoginButton fbLoginBtn;
    LoadingIndicator loadingIndicator;
    private Context context;
    private RequestRest rest;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        context = this;
        loadingIndicator = new LoadingIndicator((SquareLoading)findViewById(R.id.loadingIndicator));
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn = (LoginButton)findViewById(R.id.loginButton);
        fbLoginBtn.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        fbLoginBtn.registerCallback(callbackManager, this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailEditText.getText().toString().equals("") || passEditText.getText().toString().equals("")){
                    Snackbar.make(loginBtn, "Email atau password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                }else doLogin(emailEditText.getText().toString(), passEditText.getText().toString());
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
        rest.login(email, password);
        loadingIndicator.show();
    }

    //------------------------- Facebook login
    @Override
    public void onSuccess(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.i("FB", String.valueOf(object));
                            Log.i("FB", response.getRawResponse());
                            String name=object.getString("name");
                            String email=object.getString("email");
                            Log.i("Login Success : ", name+", "+email);
                        } catch(JSONException ex) {
                            ex.printStackTrace();
                        }
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
        PreferenceManager preferenceManager = new PreferenceManager(context);
        preferenceManager.save(profile, Constants.PREF_PROFILE);
        preferenceManager.save("{SessionID:"+sessionID+"}", Constants.PREF_SESSION_ID);
        preferenceManager.apply();
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