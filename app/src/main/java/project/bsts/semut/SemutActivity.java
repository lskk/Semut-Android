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

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.ui.MainDrawer;

public class SemutActivity extends AppCompatActivity implements OnMapReadyCallback, BroadcastManager.UIBroadcastListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private MainDrawer drawer;
    private Context context;
    private String TAG = this.getClass().getSimpleName();
    private GoogleMap mMap;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 101;
    private BroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semut);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        context = this;
        broadcastManager = new BroadcastManager(context);
        broadcastManager.subscribeToUi(this);
        drawer = new MainDrawer(context, toolbar, 0);
        drawer.initDrawer();

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
                    startService(new Intent(context, LocationService.class));
                } else {
                    Log.i(TAG, "Location Rejected");
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        broadcastManager.unSubscribeToUi();
    }

    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, msg);
    }
}
