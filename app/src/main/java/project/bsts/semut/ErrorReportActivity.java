package project.bsts.semut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hynra on 9/16/2015.
 */
public class ErrorReportActivity extends Activity {

    Button send;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forceclose);
        send = (Button) findViewById(R.id.btnLogin);

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "hendrapermana.m@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Error Report Semut App");
                intent.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("error"));

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

    }
}
