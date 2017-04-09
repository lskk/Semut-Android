package project.bsts.semut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
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
import project.bsts.semut.map.MarkerBearing;
import project.bsts.semut.map.osm.MapUtilities;
import project.bsts.semut.map.osm.MarkerClick;
import project.bsts.semut.map.osm.OSMarkerAnimation;
import project.bsts.semut.map.osm.OsmMarker;
import project.bsts.semut.pojo.mapview.MyLocation;
import project.bsts.semut.services.GetLocation;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.ui.CommonAlerts;
import project.bsts.semut.ui.ShowSnackbar;
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
    @BindView(R.id.switch_track)
    Switch switchTrack;

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
    private ProgressDialog progressDialog;
    private OSMarkerAnimation markerAnimation;
    private boolean isTracked = true;
    TSnackbar mSnackbarError;

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
        toolbar.setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.lynchLight));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        mSnackbarError = ShowSnackbar.errorSnackbar(context);
        mapUitilities = new MapUtilities(mapset);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Memuat...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        osmMarker = new OsmMarker(mapset);
        broadcastManager = new BroadcastManager(context);
        preferenceManager = new PreferenceManager(context);
        broadcastManager.subscribeToUi(this);
        locService = new Intent(context, GetLocation.class);
        permissionHelper = new PermissionHelper(context);
        animationView = new AnimationView(context);
        filterBtn.setOnClickListener(this);
        addReportBtn.setOnClickListener(this);
        addReportBtn.setTag(FAB_STATE_ADD);
        markerAnimation = new OSMarkerAnimation();

        setAnim();
        fragmentTransUtility = new FragmentTransUtility(context);
        fragmentTransUtility.setFilterFragment(new FilterFragment(), filterLayout.getId());

        markerClick = new MarkerClick(context, markerDetailLayout);
        if (permissionHelper.requestFineLocation()) startService(locService);

        markerDetailLayout.setTag(markerDetailLayout.getVisibility());
        markerDetailLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int newVis = markerDetailLayout.getVisibility();
            if((int)markerDetailLayout.getTag() != newVis) {
                markerDetailLayout.setTag(markerDetailLayout.getVisibility());
                if(newVis == View.VISIBLE) fabState(FAB_STATE_CLOSE);
                else fabState(FAB_STATE_ADD);
            }
        });

        switchTrack.setOnCheckedChangeListener((compoundButton, b) -> {
            isTracked = b;
            if(isTracked && markerMyLocation.getPosition() != null) mapController.animateTo(markerMyLocation.getPosition());
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
                    CommonAlerts.commonError(context, "Untuk melanjutkan menggunakan aplikasi, Anda harus mengizinkan aplikasi menggunakan lokasi Anda");
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
           //     Toast.makeText(context, "Location fired", Toast.LENGTH_SHORT).show();
                mapUitilities.setMyLocationGeo(msg);
                myLocationObject = new Gson().fromJson(msg, MyLocation.class);
                if(!mapUitilities.isReady()) {
                    mapController = mapUitilities.init();
                    progressDialog.dismiss();
                    markerMyLocation = osmMarker.add(myLocationObject);
                    if(isTracked) mapController.animateTo(markerMyLocation.getPosition());


                }else {
                    GeoPoint currPoint = new GeoPoint(myLocationObject.getMyLatitude(), myLocationObject.getMyLongitude());
                    markerMyLocation.setRotation((float) MarkerBearing.bearing(markerMyLocation.getPosition().getLatitude(),
                            markerMyLocation.getPosition().getLongitude(), currPoint.getLatitude(), currPoint.getLongitude()));
                    if(isTracked) mapController.animateTo(markerMyLocation.getPosition());
                    markerAnimation.animate(mapset, markerMyLocation, currPoint, 1500);
                 //   markerMyLocation.setPosition(new GeoPoint(myLocationObject.getMyLatitude(), myLocationObject.getMyLongitude()));

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
                if(isTracked) mapController.animateTo(markerMyLocation.getPosition());
                mapset.invalidate();
                break;
            case Constants.BROADCAST_CONNECTION_ERROR:
                mSnackbarError.show();
                break;
            case Constants.BROADCAST_CONNECTION_STABLE:
                mSnackbarError.dismiss();
                Toast.makeText(context, "Koneksi Anda stabil kembali", Toast.LENGTH_LONG).show();
        }
    }

    private void setAnim() {
        filterBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_keyboard_arrow_up, 24, R.color.primary_dark));
        addReportBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_add, 24, R.color.primary_light));
        slideUp = animationView.getAnimation(R.anim.slide_up, anim -> {

        });
        slideDown = animationView.getAnimation(R.anim.slide_down, anim -> {
            if(filterLayout.getVisibility() == View.VISIBLE) filterLayout.setVisibility(View.GONE);
            if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.setVisibility(View.GONE);
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
                    filterBtn.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_keyboard_arrow_up, 24, R.color.primary_dark));
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
        if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.startAnimation(slideDown);
        if(filterLayout.getVisibility() == View.VISIBLE)filterLayout.startAnimation(slideDown);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
