package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.fragments.FilterFragment;
import project.bsts.semut.fragments.map.MapCctvFragment;
import project.bsts.semut.fragments.map.MapReportFragment;
import project.bsts.semut.fragments.map.MapUserFragment;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.map.AddMarkerToMap;
import project.bsts.semut.map.MapViewComponent;
import project.bsts.semut.map.radar.MapRipple;
import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.UserMap;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.ui.LoadingIndicator;
import project.bsts.semut.ui.MainDrawer;
import project.bsts.semut.utilities.CheckService;
import project.bsts.semut.utilities.FragmentTransUtility;

public class SemutActivity extends AppCompatActivity implements OnMapReadyCallback,
        BroadcastManager.UIBroadcastListener, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_layout)
    RelativeLayout filterLayout;
    @BindView(R.id.filter_button)
    ImageButton filterBtn;
    @BindView(R.id.addReport)
    FloatingActionButton addReportBtn;
    @BindView(R.id.markerdetail_layout)
    RelativeLayout markerDetailLayout;


    private MainDrawer drawer;
    private Context context;
    private String TAG = this.getClass().getSimpleName();
    private GoogleMap mMap;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 101;
    private BroadcastManager broadcastManager;
    private PreferenceManager preferenceManager;
    private double latitude, longitude;
    private LoadingIndicator loadingIndicator;
    private boolean firstInit = false;
    private Intent locService;
    private FragmentTransUtility fragmentTransUtility;
    private AnimationView animationView;
    private Animation slideUp, slideDown;
    private boolean isFirstInit = true, isMapReady = false, isMarkerShow = false;

    private AddMarkerToMap addMarker;

    private Marker myLocationMarker;
    MapRipple mapRipple;

    private UserMap[] userMaps;
    private CctvMap[] cctvMaps;
    private PoliceMap[] policeMaps;
    private AccidentMap[] accidentMaps;
    private TrafficMap[] trafficMaps;
    private DisasterMap[] disasterMaps;
    private ClosureMap[] closureMaps;
    private OtherMap[] otherMaps;

    private Marker[] userMarkers;
    private Marker[] cctvMarkers;
    private Marker[] policeMarkers;
    private Marker[] accidentMarkers;
    private Marker[] trafficMarkers;
    private Marker[] disasterMarkers;
    private Marker[] closureMarkers;
    private Marker[] otherMarkers;

    private ActionBar actionBar;

    private static final int FAB_ACTION_DISMISS_ALL = 0;
    private static final int FAB_ACTION_FRAGMENT_SHOW = 1;
    private static final int FAB_ACTION_ADD_REPORT_SHOW = 2;
    private int FAB_STATE = 0;
    private View[] viewsToHide = new View[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semut);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setTitle("Memuat.. ");
        addReportBtn.setVisibility(View.GONE);

        context = this;
        broadcastManager = new BroadcastManager(context);
        loadingIndicator = new LoadingIndicator(context);
        fragmentTransUtility = new FragmentTransUtility(context);
        animationView = new AnimationView(context);
        preferenceManager = new PreferenceManager(context);

        setAnim();

        fragmentTransUtility.setFilterFragment(new FilterFragment(), filterLayout.getId());
        broadcastManager.subscribeToUi(this);

        loadingIndicator.show();



        drawer = new MainDrawer(context, toolbar, 0);
        drawer.initDrawer();

        viewsToHide[0] = filterBtn;
        drawer.hideToolbar(true, viewsToHide);

        locService = new Intent(context, LocationService.class);

        filterBtn.setImageDrawable(new IconicsDrawable(context)
                .color(context.getResources().getColor(R.color.primary_dark))
                .sizeDp(24)
                .icon(GoogleMaterial.Icon.gmd_list));
        filterBtn.setOnClickListener(this);
        addReportBtn.setOnClickListener(this);
        useFabButton(FAB_ACTION_DISMISS_ALL);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        int accessFineLocationPermission = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (accessFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionsResult(REQUEST_ACCESS_FINE_LOCATION,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    new int[]{PackageManager.PERMISSION_GRANTED});
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void setAnim() {
        slideUp = animationView.getAnimation(R.anim.slide_up, null);
        slideDown = animationView.getAnimation(R.anim.slide_down, new AnimationView.AnimationViewListener() {
            @Override
            public void onAnimationEnd(Animation anim) {
                filterLayout.setVisibility(View.GONE);
                addReportBtn.setVisibility(View.VISIBLE);
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
        isMapReady = true;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Access Fine Location Denied");
        }else {
            mMap.setMyLocationEnabled(true);
           // mMap.getMyLocation();
        }
        mMap.setOnMarkerClickListener(this);
        addMarker = new AddMarkerToMap(mMap);
        myLocationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 151)).title("Marker in Sydney"));
    }


    private void moveMyLocation(double latitude, double longitude){
        LatLng myLoc = new LatLng(latitude, longitude);
        myLocationMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));
        myLocationMarker.setPosition(myLoc);
        if(isFirstInit) {
            actionBar.setTitle("Semut");
            addReportBtn.setVisibility(View.VISIBLE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 17.0f));

            loadingIndicator.hide();
            drawer.hideToolbar(false, viewsToHide);
            isFirstInit = false;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        broadcastManager.unSubscribeToUi();
        if(CheckService.isLocationServiceRunning(context)){
            stopService(locService);
        }
        if(mapRipple.isAnimationRunning()){
            mapRipple.stopRippleMapAnimation();
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
                manageMyLocation(msg);
                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:
                populateDataMapView(msg);
                break;
        }
    }

    private void manageMyLocation(String msg){

        try {
            JSONObject object = new JSONObject(msg);
            latitude = object.getDouble(Constants.ENTITY_LATITUDE);
            longitude = object.getDouble(Constants.ENTITY_LONGITUDE);
            if(isMapReady) {
                if(mMap.getMyLocation() != null) {
                    if (mMap.getMyLocation().getLatitude() == latitude && mMap.getMyLocation().getLongitude() == longitude) {

                    } else {
                        latitude = mMap.getMyLocation().getLatitude();
                        longitude = mMap.getMyLocation().getLongitude();
                        preferenceManager.save((float) latitude, Constants.ENTITY_LATITUDE);
                        preferenceManager.save((float) longitude, Constants.ENTITY_LONGITUDE);
                        preferenceManager.apply();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRipple(LatLng latLng){
        mapRipple = new MapRipple(mMap, context);
        mapRipple.setLatLng(latLng);
        mapRipple.withNumberOfRipples(3);
        mapRipple.withFillColor(R.color.rippleBG);
        mapRipple.withStrokeColor(Color.DKGRAY);
        mapRipple.withStrokewidth(2);
        mapRipple.withDistance((preferenceManager.getInt(Constants.MAP_RADIUS, 3)*1000)+200);      // 2000 metres radius
        mapRipple.withRippleDuration(6000);
        mapRipple.withTransparency(0.8f);
        if (!mapRipple.isAnimationRunning()) {
            mapRipple.startRippleMapAnimation();
        }
    }


    private void populateDataMapView(String msg) {
        userMaps = MapViewComponent.getUsers(MapViewComponent.USER_MAP_COMPONENT, msg);
        cctvMaps = MapViewComponent.getCCTVs(MapViewComponent.CCTV_MAP_COMPONENT, msg);
        policeMaps = MapViewComponent.getPolicesPost(MapViewComponent.POLICE_MAP_COMPONENT, msg);
        accidentMaps = MapViewComponent.getAccident(MapViewComponent.ACCIDENT_MAP_COMPONENT, msg);
        trafficMaps = MapViewComponent.getTraffic(MapViewComponent.TRAFFIC_MAP_COMPONENT, msg);
        disasterMaps = MapViewComponent.getDisaster(MapViewComponent.DISASTER_MAP_COMPONENT, msg);
        closureMaps = MapViewComponent.getClosure(MapViewComponent.CLOSURE_MAP_COMPONENT, msg);
        otherMaps = MapViewComponent.getOther(MapViewComponent.OTHER_MAP_COMPONENT, msg);

        mMap.clear();

        userMarkers = new Marker[userMaps.length];
        cctvMarkers = new Marker[cctvMaps.length];
        policeMarkers = new Marker[policeMaps.length];
        accidentMarkers = new Marker[accidentMaps.length];
        trafficMarkers = new Marker[trafficMaps.length];
        disasterMarkers = new Marker[disasterMaps.length];
        closureMarkers = new Marker[closureMaps.length];
        otherMarkers = new Marker[otherMaps.length];

        generateMarker(userMaps, userMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        generateMarker(cctvMaps, cctvMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        generateMarker(policeMaps, policeMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        generateMarker(accidentMaps, accidentMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        generateMarker(trafficMaps, trafficMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        generateMarker(disasterMaps, disasterMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        generateMarker(closureMaps, closureMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        generateMarker(otherMaps, otherMarkers, BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        moveMyLocation(latitude, longitude);
        setRipple(new LatLng(latitude, longitude));

    }


    private void generateMarker(Object[] objects, Marker[] markers, BitmapDescriptor descriptor){
        for (int i = 0; i < objects.length; i ++){
            markers[i] = addMarker.add(objects[i]);
            markers[i].setTag(objects[i]);
            markers[i].setIcon(descriptor);
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
                    addReportBtn.setVisibility(View.GONE);
                }
                else {
                    filterLayout.startAnimation(slideDown);
                    filterBtn.setImageDrawable(new IconicsDrawable(context)
                            .color(context.getResources().getColor(R.color.primary_dark))
                            .sizeDp(24)
                            .icon(GoogleMaterial.Icon.gmd_list));

                }
                break;
            case R.id.addReport:
                checkFabClick();
                break;
        }
    }

    private void checkFabClick() {
        switch (FAB_STATE){
            case FAB_ACTION_FRAGMENT_SHOW:
                markerDetailLayout.setVisibility(View.GONE);
                useFabButton(FAB_ACTION_DISMISS_ALL);
                break;
            case FAB_ACTION_DISMISS_ALL:

                break;
        }
    }

    private void useFabButton(int actionType){
        switch (actionType){
            case FAB_ACTION_FRAGMENT_SHOW:
                addReportBtn.setImageDrawable(new IconicsDrawable(context)
                        .color(context.getResources().getColor(R.color.primary_light))
                        .sizeDp(24)
                        .icon(GoogleMaterial.Icon.gmd_clear));
                drawer.hideToolbar(true, viewsToHide);
                FAB_STATE = FAB_ACTION_FRAGMENT_SHOW;
                break;
            case FAB_ACTION_DISMISS_ALL:
                addReportBtn.setImageDrawable(new IconicsDrawable(context)
                        .color(context.getResources().getColor(R.color.primary_light))
                        .sizeDp(24)
                        .icon(GoogleMaterial.Icon.gmd_add));
                drawer.hideToolbar(false, viewsToHide);
                FAB_STATE = FAB_ACTION_DISMISS_ALL;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTag() instanceof UserMap){
            MapUserFragment mapUserFragment = new MapUserFragment();
            mapUserFragment.setData((UserMap) marker.getTag());
            fragmentTransUtility.setUserMapFragment(mapUserFragment, markerDetailLayout.getId());
            markerDetailLayout.setVisibility(View.VISIBLE);
            useFabButton(FAB_ACTION_FRAGMENT_SHOW);
        }else if(marker.getTag() instanceof CctvMap){
            MapCctvFragment mapCctvFragment = new MapCctvFragment();
            mapCctvFragment.setData((CctvMap) marker.getTag());
            fragmentTransUtility.setCctvMapFragment(mapCctvFragment, markerDetailLayout.getId());
            markerDetailLayout.setVisibility(View.VISIBLE);
            useFabButton(FAB_ACTION_FRAGMENT_SHOW);
        }else if(marker.getTag() instanceof  PoliceMap ||
                marker.getTag() instanceof AccidentMap ||
                marker.getTag() instanceof  TrafficMap ||
                marker.getTag() instanceof DisasterMap ||
                marker.getTag() instanceof  ClosureMap ||
                marker.getTag() instanceof OtherMap){
            MapReportFragment mapReportFragment = new MapReportFragment();
            mapReportFragment.setData(marker.getTag());
            fragmentTransUtility.setReportMapFragment(mapReportFragment, markerDetailLayout.getId());
            markerDetailLayout.setVisibility(View.VISIBLE);
            useFabButton(FAB_ACTION_FRAGMENT_SHOW);
        }
        return false;
    }
}
