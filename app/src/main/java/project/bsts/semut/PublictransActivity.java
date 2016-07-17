package project.bsts.semut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import project.bsts.semut.app.ExceptionHandler;


public class PublictransActivity extends Activity {
    Button btnTaxi;
    Button btnAngkot;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_publictrans);
        btnTaxi = (Button)findViewById(R.id.btnTaxi);
        btnAngkot = (Button)findViewById(R.id.btnAngkot);
        btnBack = (ImageButton)findViewById(R.id.backButton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TaxiActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnAngkot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "This feature will be available soon.", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(0, R.anim.slideout);
    }


}
