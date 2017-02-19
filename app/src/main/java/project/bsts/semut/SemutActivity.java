package project.bsts.semut;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.fragments.FilterFragment;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.ui.LoadingIndicator;
import project.bsts.semut.ui.MainDrawer;
import project.bsts.semut.utilities.CheckService;
import project.bsts.semut.utilities.FragmentTransUtility;

public class SemutActivity extends AppCompatActivity implements OnMapReadyCallback,
        BroadcastManager.UIBroadcastListener, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_layout)
    RelativeLayout filterLayout;
    @BindView(R.id.filter_button)
    ImageButton filterBtn;

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
    private FragmentTransUtility fragmentTransUtility;
    private AnimationView animationView;
    private Animation slideUp, slideDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semut);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        context = this;
        broadcastManager = new BroadcastManager(context);
        loadingIndicator = new LoadingIndicator(context);
        fragmentTransUtility = new FragmentTransUtility(context);
        animationView = new AnimationView(context);
        setAnim();

        fragmentTransUtility.setFilterFragment(new FilterFragment(), filterLayout.getId());
        broadcastManager.subscribeToUi(this);
        loadingIndicator.show();
        drawer = new MainDrawer(context, toolbar, 0);
        drawer.initDrawer();
        locService = new Intent(context, LocationService.class);
        filterBtn.setImageDrawable(new IconicsDrawable(context)
        .color(context.getResources().getColor(R.color.primary_dark))
        .sizeDp(24)
        .icon(GoogleMaterial.Icon.gmd_list));
        filterBtn.setOnClickListener(this);

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

    private void setAnim(){
        slideUp = animationView.getAnimation(R.anim.slide_up, null);
        slideDown = animationView.getAnimation(R.anim.slide_down, new AnimationView.AnimationViewListener() {
            @Override
            public void onAnimationEnd(Animation anim) {
                filterLayout.setVisibility(View.GONE);
            }
        });
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



    //--------- Receive brodcast from service
    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "Receive on UI : Type : "+type);
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, msg);
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



    //--------- click listener
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filter_button:
                if(filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    filterLayout.startAnimation(slideUp);
                    filterBtn.setImageDrawable(new IconicsDrawable(context)
                            .color(context.getResources().getColor(R.color.primary_dark))
                            .sizeDp(24)
                            .icon(GoogleMaterial.Icon.gmd_done));
                }
                else {
                 //   filterLayout.setVisibility(View.GONE);
                    filterLayout.startAnimation(slideDown);
                    filterBtn.setImageDrawable(new IconicsDrawable(context)
                            .color(context.getResources().getColor(R.color.primary_dark))
                            .sizeDp(24)
                            .icon(GoogleMaterial.Icon.gmd_list));

                }
                break;
        }
    }
}
