package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.fragments.FilterFragment;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.helper.PermissionHelper;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.map.osm.MapUtilities;
import project.bsts.semut.map.osm.MarkerClick;
import project.bsts.semut.map.osm.OsmMarker;
import project.bsts.semut.pojo.mapview.MyLocation;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.utilities.CheckService;
import project.bsts.semut.utilities.CustomDrawable;
import project.bsts.semut.utilities.FragmentTransUtility;

public class SocialReportActivity extends AppCompatActivity implements BroadcastManager.UIBroadcastListener,
        Marker.OnMarkerClickListener,View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.maposm)
    MapView mapset;
    @BindView(R.id.markerdetail_layout)
    RelativeLayout markerDetailLayout;
    @BindView(R.id.filter_layout)
    RelativeLayout filterLayout;
    @BindView(R.id.filter_button)
    ImageButton filterBtn;
    @BindView(R.id.addReport)
    FloatingActionButton addReportBtn;

    MapUtilities mapUitilities;
    Context context;

    private IMapController mapController;
    private BroadcastManager broadcastManager;
    private PermissionHelper permissionHelper;
    private PreferenceManager preferenceManager;
    private String TAG = this.getClass().getSimpleName();
    private Intent locService;
    private MarkerClick markerClick;
    private FragmentTransUtility fragmentTransUtility;
    private AnimationView animationView;
    private Animation slideUp, slideDown;
    private static final int FAB_STATE_CLOSE = 0;
    private static final int FAB_STATE_ADD = 1;
    private OsmMarker osmMarker;
    private Marker markerMyLocation;
    private MyLocation myLocationObject;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        broadcastManager.unSubscribeToUi();
        preferenceManager.save(0, Constants.IS_ONLINE);
        preferenceManager.apply();
        if(CheckService.isLocationServiceRunning(context)){
            stopService(locService);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialreport);
        ButterKnife.bind(this);
        toolbar.setTitle("Social Report");
        toolbar.setTitleTextColor(getResources().getColor(R.color.lynchLight));
        setSupportActionBar(toolbar);

        context = this;
        mapUitilities = new MapUtilities(mapset);
        osmMarker = new OsmMarker(mapset);
        broadcastManager = new BroadcastManager(context);
        preferenceManager = new PreferenceManager(context);
        broadcastManager.subscribeToUi(this);
        locService = new Intent(context, LocationService.class);
        permissionHelper = new PermissionHelper(context);
        animationView = new AnimationView(context);
        filterBtn.setOnClickListener(this);
        addReportBtn.setOnClickListener(this);
        addReportBtn.setTag(FAB_STATE_ADD);

        setAnim();
        fragmentTransUtility = new FragmentTransUtility(context);
        fragmentTransUtility.setFilterFragment(new FilterFragment(), filterLayout.getId());

        markerClick = new MarkerClick(context, markerDetailLayout);
        if (permissionHelper.requestFineLocation()) startService(locService);

        markerDetailLayout.setTag(markerDetailLayout.getVisibility());
        markerDetailLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int newVis = markerDetailLayout.getVisibility();
                if((int)markerDetailLayout.getTag() != newVis) {
                    markerDetailLayout.setTag(markerDetailLayout.getVisibility());
                    if(newVis == View.VISIBLE) fabState(FAB_STATE_CLOSE);
                    else fabState(FAB_STATE_ADD);
                }
            }
        });

        preferenceManager.save(1, Constants.IS_ONLINE);
        preferenceManager.apply();

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
                }
                break;
        }
    }

    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "Receive on UI : Type : "+type);
        Log.i(TAG, msg);
        switch (type){
            case Constants.BROADCAST_MY_LOCATION:
                mapUitilities.setMyLocationGeo(msg);
                myLocationObject = new Gson().fromJson(msg, MyLocation.class);
                if(!mapUitilities.isReady()) {
                    mapController = mapUitilities.init();
                    markerMyLocation = osmMarker.add(myLocationObject);
                }else {

                    markerMyLocation.setPosition(new GeoPoint(myLocationObject.getMyLatitude(), myLocationObject.getMyLongitude()));
                    mapset.invalidate();
                }

                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:
                List<Marker> markersToRemove = new ArrayList<Marker>();

                for(int i = 0; i < mapset.getOverlays().size(); i++){
                    if(mapset.getOverlays().get(i) instanceof Marker ){
                        if(!(((Marker) mapset.getOverlays().get(i)).getRelatedObject() instanceof MyLocation)){
                            markersToRemove.add((Marker) mapset.getOverlays().get(i));
                        }
                    }
                }

                for(int i = 0; i < markersToRemove.size(); i++){
                    mapset.getOverlays().remove(markersToRemove.get(i));
                }
                markersToRemove.clear();
                mapset.invalidate();


                mapUitilities.setMapObjectsMarkers(msg);
                for(int i = 0 ; i < mapset.getOverlays().size(); i++){
                    if (mapset.getOverlays().get(i) instanceof Marker) ((Marker) mapset.getOverlays().get(i)).setOnMarkerClickListener(this);
                }
                mapset.invalidate();
                break;
        }
    }

    private void setAnim() {
        filterBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_keyboard_arrow_up, 24, R.color.primary_dark));
        addReportBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_add, 24, R.color.primary_light));
        slideUp = animationView.getAnimation(R.anim.slide_up, new AnimationView.AnimationViewListener() {
            @Override
            public void onAnimationEnd(Animation anim) {

            }
        });
        slideDown = animationView.getAnimation(R.anim.slide_down, new AnimationView.AnimationViewListener() {
            @Override
            public void onAnimationEnd(Animation anim) {
                if(filterLayout.getVisibility() == View.VISIBLE) filterLayout.setVisibility(View.GONE);
                if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markerClick.checkMarker(marker);
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.filter_button:
                if(filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    filterBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_keyboard_arrow_down, 24, R.color.primary_dark));
                    filterLayout.startAnimation(slideUp);
                    fabState(FAB_STATE_CLOSE);
                }
                else {
                    filterLayout.startAnimation(slideDown);
                    filterBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_keyboard_arrow_up, 24, R.color.primary_dark));
                    hideLayouts();
                    fabState(FAB_STATE_ADD);

                }
                break;
            case R.id.addReport:
                if((int)addReportBtn.getTag()== FAB_STATE_CLOSE) {
                    hideLayouts();
                    fabState(FAB_STATE_ADD);
                }
                else startActivity(new Intent(context, TagsActivity.class));
                break;
        }
    }

    private void fabState(int state){
        switch (state){
            case FAB_STATE_ADD:
                addReportBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_add, 24, R.color.primary_light));
                addReportBtn.setTag(FAB_STATE_ADD);
                break;
            case FAB_STATE_CLOSE:
                addReportBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_close, 24, R.color.primary_light));
                addReportBtn.setTag(FAB_STATE_CLOSE);
                break;
        }
    }

    private void hideLayouts(){
        //markerDetailLayout.setVisibility(View.GONE);
        if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.startAnimation(slideDown);
        if(filterLayout.getVisibility() == View.VISIBLE)filterLayout.startAnimation(slideDown);
       // filterLayout.setVisibility(View.GONE);
    }
}
