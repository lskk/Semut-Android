package project.bsts.semut;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.Window;

public class SplashScreenActivity extends Activity {


    private static final int SPLASH_TIME = 2 * 1000;// 3 * 1000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this,
                    MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }, SPLASH_TIME);

        new Handler().postDelayed(() -> {
        }, SPLASH_TIME);

    }


    @Override
    public void onBackPressed() {
       // this.finish();
        super.onBackPressed();
    }
}