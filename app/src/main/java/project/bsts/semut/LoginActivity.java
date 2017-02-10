package project.bsts.semut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.fb_Login)
    Button fbLoginBtn;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}