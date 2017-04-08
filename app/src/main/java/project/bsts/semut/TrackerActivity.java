package project.bsts.semut;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.adapters.TrackerAdapter;
import project.bsts.semut.connections.broker.BrokerCallback;
import project.bsts.semut.connections.broker.Config;
import project.bsts.semut.connections.broker.Consumer;
import project.bsts.semut.connections.broker.Factory;
import project.bsts.semut.map.MarkerBearing;
import project.bsts.semut.map.osm.MarkerClick;
import project.bsts.semut.map.osm.OSMarkerAnimation;
import project.bsts.semut.pojo.mapview.Tracker;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.ui.AnimationView;
import project.bsts.semut.ui.CommonAlerts;
import project.bsts.semut.utilities.CustomDrawable;

public class TrackerActivity extends AppCompatActivity implements BrokerCallback, TrackerAdapter.MarkerPositionListener, Marker.OnMarkerClickListener {


    @BindView(R.id.maposm)
    MapView mapset;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.sortLayout)
    RelativeLayout sortLayout;
    @BindView(R.id.sort_fab)
    FloatingActionButton sortFab;
    @BindView(R.id.markerdetail_layout)
    RelativeLayout markerDetailLayout;


    private Switch mSwitchTrack;

    private Factory mqFactory;
    private Consumer mqConsumer;
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private boolean isConnected = true, isMessageReceived = false;
    private ProgressDialog mProgressDialog;
    private IMapController mapController;
    private Tracker[] trackers;
    private String[] trackerMacs;
    private Marker[] markers;
    private boolean isFirsInit = true, isTracked = true;
    private TrackerAdapter adapter;
    private int checkedState = 0;
    private MarkerClick markerClick;
    private Animation slideDown;
    private AnimationView animationView;
    private final static int FAB_STATE_OPEN = 1;
    private final static int FAB_STATE_CLOSE = 0;
    private int fabState = FAB_STATE_CLOSE;
    private Intent intent;
    private Drawable mMarkerDrawable;
    private String ROUTING_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwitchTrack = (Switch)findViewById(R.id.switch_track);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lynchLight));
        intent = getIntent();
        ROUTING_KEY = intent.getStringExtra(Constants.INTENT_TRACKER_TYPE);
        if(ROUTING_KEY.equals(Constants.MQ_ROUTES_BROADCAST_TRACKER_ANGKOT)) {
            toolbar.setTitle("Angkot");
            mMarkerDrawable = getResources().getDrawable(R.drawable.tracker_angkot);
        } else {
            toolbar.setTitle("Bus");
            mMarkerDrawable = getResources().getDrawable(R.drawable.tracker_bus);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);



        mSwitchTrack.setChecked(true);
        mSwitchTrack.setOnCheckedChangeListener((compoundButton, b) -> isTracked = b);

        context = this;
        mProgressDialog = new ProgressDialog(context);
        markerClick = new MarkerClick(context, markerDetailLayout);
        mProgressDialog.setMessage("Memuat...");
        mProgressDialog.show();
        connectToRabbit();
        mapset.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapset.setMultiTouchControls(true);
        mapset.setMaxZoomLevel(20);
        mapController = mapset.getController();
        mapController.setZoom(20);
        sortFab.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_sort, 24, R.color.primary_light));
        sortFab.setOnClickListener(view -> doFab());

        animationView = new AnimationView(context);
        slideDown = animationView.getAnimation(R.anim.slide_down, anim -> {
            if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.setVisibility(View.GONE);
        });
       // markerDetailLayout.setOnClickListener(v-> markerDetailLayout.startAnimation(slideDown));
    }

    private void doFab(){
        if(markerDetailLayout.getVisibility() == View.VISIBLE) fabState = FAB_STATE_OPEN;
        if(fabState == FAB_STATE_CLOSE){
            if(sortLayout.getVisibility() == View.GONE) sortLayout.setVisibility(View.VISIBLE);
            fabState = FAB_STATE_OPEN;
            sortFab.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_close, 24, R.color.primary_light));
        }else {
            if(sortLayout.getVisibility() == View.VISIBLE) sortLayout.setVisibility(View.GONE);
            if(markerDetailLayout.getVisibility() == View.VISIBLE) markerDetailLayout.startAnimation(slideDown);
            fabState = FAB_STATE_CLOSE;
            sortFab.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_sort, 24, R.color.primary_light));
        }
    }


    private void setListView() {

        adapter = new TrackerAdapter(context, trackers, checkedState, sortLayout, this);
        listView.setAdapter(adapter);
    }

    private void connectToRabbit() {
        mqFactory = new Factory(Config.hostName, Config.virtualHostname, Config.username, Config.password, Config.exchange, Config.rotuingkey, Config.port);
        mqConsumer = this.mqFactory.createConsumer(this);
        consume();
    }

    private void consume(){

        mqConsumer.setQueueName("");
        mqConsumer.setExchange(Constants.MQ_EXCHANGE_NAME_MULTIPLE_BROADCAST);
        mqConsumer.setRoutingkey(ROUTING_KEY);
        mqConsumer.subsribe();
        mqConsumer.setMessageListner(delivery -> {
            try {
                final String message = new String(delivery.getBody(), "UTF-8");
                Log.i(TAG, "-------------------------------------");
                Log.i(TAG, "incoming message");
                Log.i(TAG, "-------------------------------------");
                Log.i(TAG, message);
                if(!isMessageReceived){
                    isMessageReceived = true;
                    mProgressDialog.dismiss();
                }
                getMessage(message);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });

    }

    private void getMessage(String msg){
        try {
            JSONObject object = new JSONObject(msg);
            JSONArray jsonArray = object.getJSONArray("data");
            if(object.getBoolean("success")){
                if(isFirsInit) {
                    Log.i("test", "Init");
                    trackers = new Tracker[jsonArray.length()];
                    trackerMacs = new String[jsonArray.length()];
                    isFirsInit = false;
                    // add markers
                    markers = new Marker[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        trackers[i] = new Gson().fromJson(jsonArray.get(i).toString(), Tracker.class);
                        trackerMacs[i] = trackers[i].getMac();
                        markers[i] = new Marker(mapset);
                        markers[i].setPosition(new GeoPoint(trackers[i].getData().get(0), trackers[i].getData().get(1)));
                        markers[i].setIcon(mMarkerDrawable);
                        markers[i].setRelatedObject(trackers[i]);
                        markers[i].setOnMarkerClickListener(this);
                        mapset.getOverlays().add(markers[i]);
                    }
                    setListView();
                    animateToSelected();
                }else {
                    if(jsonArray.length() == trackers.length){
                        OSMarkerAnimation markerAnimation = new OSMarkerAnimation();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject entity = jsonArray.getJSONObject(i);
                            if(trackers[i].getMac().equals(entity.getString("Mac"))){ // update markers
                                trackers[i] = new Gson().fromJson(entity.toString(), Tracker.class);
                                if(markers[i].getPosition().getLatitude() != trackers[i].getData().get(0) ||
                                        markers[i].getPosition().getLongitude() != trackers[i].getData().get(1)) {
                                    double bearing = MarkerBearing.bearing(markers[i].getPosition().getLatitude(), markers[i].getPosition().getLongitude(),
                                            trackers[i].getData().get(0), trackers[i].getData().get(1));
                                    markers[i].setRelatedObject(trackers[i]);
                                    markers[i].setRotation((float) bearing);

                                    markerAnimation.animate(mapset, markers[i],
                                            new GeoPoint(trackers[i].getData().get(0), trackers[i].getData().get(1)),
                                            1500);
                                    mapController.setZoom(20);
                                }else {
                                    // same position
                                }
                            }
                        }
                        if(listView.getVisibility() == View.GONE) setListView();
                        //updateListView();
                        if(isTracked) animateToSelected();

                    }else {
                        // found new data
                        isFirsInit = true;
                    }
                }
            }else {
                // success == false
                CommonAlerts.commonError(context, "Terjadi kesalahan pada server, silahkan coba beberapa saat lagi");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void animateToSelected(){
        mapController.animateTo(markers[checkedState].getPosition());
    }


    @Override
    public void onMQConnectionFailure(String message) {
        Log.i(TAG, message);
        if(isConnected){
            isConnected = false;
            if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
            try {
                CommonAlerts.commonError(context, "Server tidak merespon atau koneksi internet Anda tidak stabil, coba beberapa saat lagi");
            }catch (IllegalStateException e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMQDisconnected() {

    }

    @Override
    public void onMQConnectionClosed(String message) {
        Log.i(TAG, message);
        if(isConnected){
            isConnected = false;
            if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
         //   if(!isFirsInit)
            try {
                CommonAlerts.commonError(context, "Server tidak merespon atau koneksi internet Anda tidak stabil, coba beberapa saat lagi");
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMarkerSelected(int position) {
        checkedState = position;
        animateToSelected();
        setListView();
        doFab();


    }




    @Override
    public void onDestroy(){
        super.onDestroy();
        mqConsumer.stop();
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markerClick.checkMarker(marker);
        sortFab.setImageDrawable(CustomDrawable.create(context, GoogleMaterial.Icon.gmd_close, 24, R.color.primary_light));
        return false;
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
