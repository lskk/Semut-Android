package project.bsts.semut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmergencyActivity extends AppCompatActivity {

    @BindView(R.id.layout_common_emergency)
    RelativeLayout mButtonCommonEmergency;
    @BindView(R.id.layout_ambulance_emergency)
    RelativeLayout mButtonAmbulanceEmergency;
    @BindView(R.id.layout_police_emergency)
    RelativeLayout mButtonPoliceEmergency;
    @BindView(R.id.layout_fire_emergency)
    RelativeLayout mButtonDamkarEmergency;
    @BindView(R.id.layout_derek_emergency)
    RelativeLayout mButtonDerekEemergency;
    @BindView(R.id.button_showall)
    Button mButtonShowAll;
    @BindView(R.id.layout_showall)
    LinearLayout mLayoutShowAll;

    private int mEmergencyType = 0;
    private String mEmergencyName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mLayoutShowAll.setVisibility(View.GONE);
        mButtonShowAll.setOnClickListener(view -> {
            if(mLayoutShowAll.getVisibility() == View.GONE){
                mLayoutShowAll.setVisibility(View.VISIBLE);
                mButtonShowAll.setText("Sembunyikan");
            }else {
                mLayoutShowAll.setVisibility(View.GONE);
                mButtonShowAll.setText("Tampilkan Semua");
            }
        });
        mButtonCommonEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 5;
            mEmergencyName = "Panic Button";
            return false;
        });
        mButtonAmbulanceEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 1;
            mEmergencyName = "Ambulance Call";
            return false;
        });
        mButtonPoliceEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 2;
            mEmergencyName = "Police Call";
            return false;
        });
        mButtonDamkarEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 3;
            mEmergencyName = "Fire Button";
            return false;
        });
        mButtonDerekEemergency.setOnLongClickListener(view -> {
            mEmergencyType = 4;
            mEmergencyName = "Derek Call";
            return false;
        });
    }

}
