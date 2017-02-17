package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.LoadingIndicator;
import project.bsts.semut.ui.MainDrawer;
import project.bsts.semut.utilities.CheckService;

public class SemutActivity extends AppCompatActivity implements OnMapReadyCallback, BroadcastManager.UIBroadcastListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MainDrawer drawer;
    private Context context;
    private String TAG = this.getClass().getSimpleName();
    private GoogleMap mMap;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 101;
    private BroadcastManager broadcastManager;
    private double latitude, longitude;
    private LoadingIndicator loadingIndicator;
    private boolean firstInit = false;
    private Intent locService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semut);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        context = this;
        broadcastManager = new BroadcastManager(context);
        loadingIndicator = new LoadingIndicator(context);
        broadcastManager.subscribeToUi(this);
        loadingIndicator.show();
        drawer = new MainDrawer(context, toolbar, 0);
        drawer.initDrawer();
        locService = new Intent(context, LocationService.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        int accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionsResult(REQUEST_ACCESS_FINE_LOCATION,
                    new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    new int[] { PackageManager.PERMISSION_GRANTED });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(locService);
                } else {
                    Log.i(TAG, "Location Rejected");
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        moveMyLocation(-34, 151);
    }


    private void moveMyLocation(double latitude, double longitude){
        mMap.clear();
        LatLng myLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        broadcastManager.unSubscribeToUi();
        if(CheckService.isLocationServiceRunning(context)){
            stopService(locService);
        }
    }

    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "Receive on UI : Type : "+type);
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, msg);
        Log.i(TAG, "-------------------------------------");
        switch (type){
            case Constants.BROADCAST_MY_LOCATION:
                if(!firstInit) {
                    loadingIndicator.hide();
                    firstInit = false;
                }
                try {
                    JSONObject object = new JSONObject(msg);
                    latitude = object.getDouble(Constants.ENTITY_LATITUDE);
                    longitude = object.getDouble(Constants.ENTITY_LONGITUDE);
                    moveMyLocation(latitude, longitude);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:
                break;
        }
    }
}
