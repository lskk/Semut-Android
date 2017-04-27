package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.connections.broker.BrokerCallback;
import project.bsts.semut.connections.broker.Config;
import project.bsts.semut.connections.broker.Factory;
import project.bsts.semut.connections.broker.Producer;
import project.bsts.semut.helper.JSONRequest;
import project.bsts.semut.helper.PermissionHelper;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.pojo.Session;
import project.bsts.semut.services.GetLocation;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.CommonAlerts;
import project.bsts.semut.utilities.CheckService;


public class EmergencyActivity extends AppCompatActivity implements BrokerCallback {

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
    private Intent locService;
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private PermissionHelper mPermissionHelper;
    private Factory mqFactory;
    private Producer mqProducer;
    private PreferenceManager mPreferencesManager;

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
            publish();
            return false;
        });
        mButtonAmbulanceEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 1;
            mEmergencyName = "Ambulance Call";
            publish();
            return false;
        });
        mButtonPoliceEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 2;
            mEmergencyName = "Police Call";
            publish();
            return false;
        });
        mButtonDamkarEmergency.setOnLongClickListener(view -> {
            mEmergencyType = 3;
            mEmergencyName = "Fire Button";
            publish();
            return false;
        });
        mButtonDerekEemergency.setOnLongClickListener(view -> {
            mEmergencyType = 4;
            mEmergencyName = "Derek Call";
            publish();
            return false;
        });

        mContext = this;
        mPermissionHelper = new PermissionHelper(mContext);
        mPreferencesManager = new PreferenceManager(mContext);
        locService = new Intent(mContext, GetLocation.class);
        locService.putExtra(Constants.INTENT_LOCATION_WITH_STORING, false);
        if (mPermissionHelper.requestFineLocation()) startService(locService);
        connectToRabbit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionHelper.REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Location granted");
                    startService(locService);
                } else {
                    Log.i(TAG, "Location Rejected");
                    CommonAlerts.commonError(mContext, "Untuk melanjutkan menggunakan aplikasi, Anda harus mengizinkan aplikasi menggunakan lokasi Anda");
                }
                break;
        }
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        if(CheckService.isLocationServiceRunning(mContext)){
            stopService(locService);
        }
    }


    private void connectToRabbit() {
        mqFactory = new Factory(Config.hostName, Config.virtualHostname, Config.username, Config.password, Config.exchange, Config.rotuingkey, Config.port);

    }

    private void publish(){
        mqProducer = mqFactory.createProducer(EmergencyActivity.this);
        mqProducer.setRoutingkey(Constants.ROUTING_KEY_EMEREGNCY);
        JSONObject message = new JSONObject();
        try {
            message.put("SessionID", new Gson().fromJson(mPreferencesManager.getString(Constants.PREF_SESSION_ID), Session.class).getSessionID());
            message.put("Latitude", mPreferencesManager.getDouble(Constants.ENTITY_LATITUDE, 0));
            message.put("Longitude", mPreferencesManager.getDouble(Constants.ENTITY_LONGITUDE, 0));
            message.put("EmergencyID", mEmergencyType);
            message.put("EmergencyType", mEmergencyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, message.toString());
        mqProducer.publish(message.toString(), null, true);
        Toast.makeText(mContext, "Berhasil memuat laporan, pastikan Nomor telepon Anda Aktif!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onMQConnectionFailure(String message) {
        CommonAlerts.commonError(mContext, "Server tidak merespon atau koneksi internet Anda tidak stabil, coba beberapa saat lagi");
    }

    @Override
    public void onMQDisconnected() {

    }

    @Override
    public void onMQConnectionClosed(String message) {

    }
}
