package project.bsts.semut;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.Window;
import android.widget.Toast;

import project.bsts.semut.helper.PermissionHelper;
import project.bsts.semut.ui.CommonAlerts;
import project.bsts.semut.utilities.CheckService;

public class SplashScreenActivity extends AppCompatActivity {


    private static final int SPLASH_TIME = 2 * 1000;// 3 * 1000
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        context = this;

        new Handler().postDelayed(() -> {

            if(CheckService.isInternetAvailable(context)) {
                if (CheckService.isGpsEnabled(this)) {
                    PermissionHelper permissionHelper = new PermissionHelper(this);
                    if (permissionHelper.requestFineLocation()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                } else CommonAlerts.gspIsDisable(this);
            }else CommonAlerts.internetIsDisabled(this);


        }, SPLASH_TIME);

        new Handler().postDelayed(() -> {
        }, SPLASH_TIME);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionHelper.REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(this.getClass().getSimpleName(), "Location granted");
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Log.i(this.getClass().getSimpleName(), "Location Rejected");
                    Toast.makeText(context, "Maaf, Anda harus memberi izin lokasi kepada aplikasi untuk melanjutkan", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
       // this.finish();
        super.onBackPressed();
    }
}