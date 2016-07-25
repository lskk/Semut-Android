package project.bsts.semut;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;
import project.bsts.semut.service.GPSTracker;
import project.bsts.semut.service.GetLocation;
import project.bsts.semut.service.StoreLocations;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.location.Address;

public class SemutActivity extends AppCompatActivity implements Runnable, Marker.OnMarkerClickListener, View.OnTouchListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
        NavigationView.OnNavigationItemSelectedListener{
    public static int FilterMapValue = 255;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MARKER_FRIEND = 0;
    private static final int MARKER_CAMERA = 1;
    private static final int MARKER_POLICE = 2;
    private static final int MARKER_ACCIDENT = 3;
    private static final int MARKER_TRAFFIC = 4;
    private static final int MARKER_OTHER = 5;
    private static final int MARKER_STATION = 6;
    private static final int MARKER_ANGKOT = 7;
    private static final int MARKER_ME = 8;

    MapView map;


    private Button btnMenu;
    private Button btnTag;
    private Button btnFilter;
    private Button btnMyLocation;
    final Context context = this;
    private LinearLayout pinDetail;

    private SQLiteHandler db;
    private SessionManager session;
    Intent getLocation;
    Intent storeLocation;
    TelephonyManager tm;
    String deviceID;
    private Timer timer;
    private boolean continueRequest;
    private boolean keepGoing;

    String email_p = "h@h.com";
    String user_name = "Blah";

    Drawer result;
    Toolbar toolbar, toolbar_bottom;

    IMapController mapController;

    // GPSTracker class
    GPSTracker gps;
    private boolean isPopup;
    FloatingActionButton fab;
    GeoPoint startPoint;

    Double nativeLat, nativeLon;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    DrawerLayout drawer;
    NavigationView navigationView;

    ImageButton closeFragment;


    //---------------- below this line : onResume Activity

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() == false)
            mGoogleApiClient.connect();
    }

    //---------------- below this line : onDestroy Activity
    @Override
    protected  void onDestroy(){
        super.onDestroy();

        timer.cancel();
        stopService(getLocation);
        stopService(storeLocation);

        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    //---------------- below this line : onPause Activity
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            iniMap(currentLatitude, currentLongitude);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_front);


        // binding content layout
        pinDetail = (LinearLayout) findViewById(R.id.pinDetail);
        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnTag = (Button) findViewById(R.id.btnTag);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnMyLocation = (Button) findViewById(R.id.btn_mylocation);
        closeFragment = (ImageButton)findViewById(R.id.closeFragment);


        // click listener close fragment
        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinDetail.setVisibility(View.GONE);
                isPopup = false;
                btnMenu.setVisibility(View.INVISIBLE);
                btnFilter.setVisibility(View.INVISIBLE);
                btnTag.setVisibility(View.INVISIBLE);
                btnMyLocation.setVisibility(View.INVISIBLE);
                toolbar_bottom.setVisibility(View.VISIBLE);
            }
        });


        checkGps();
        bindingToolbar();
        bindingFab();
        bindingMap();

        //---------------- below this line : android get current location builder

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        //--------------- setup some stuff
        FacebookSdk.sdkInitialize(getApplicationContext());
        tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
        session = new SessionManager(getApplicationContext());
        getLocation = new Intent(getApplicationContext(), GetLocation.class);
        storeLocation = new Intent(getApplicationContext(), StoreLocations.class);
        continueRequest = true;


        //-------------- Timer for RUN
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SemutActivity.this.run();
            }
        }, 5000, 5000);

        // load local db
        db = new SQLiteHandler(getApplicationContext());

        if (!session.isSkip()){
            Intent ipre = new Intent(getApplicationContext(), PreloginActivity.class);
            startActivity(ipre);
            finish();
        }
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("Name");


        //------------- check session
        if(session.isLoggedIn()) {

            getprofile();
        } else {
            iniMap(currentLatitude, currentLongitude);
            setMenudrawer(0, "blah", "blah");
        }




    }


    //------------------------ drawer things
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(session.isLoggedIn()) {

            if (id == R.id.nav_notif) {
                Intent notif = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(notif);
            } else if (id == R.id.nav_friend) {
                Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_cctv) {
                Intent cctv = new Intent(getApplicationContext(), CctvActivity.class);
                startActivity(cctv);

            } else if (id == R.id.nav_transport) {
                Intent ipublic = new Intent(getApplicationContext(), PublictransActivity.class);
                startActivity(ipublic);

            } else if (id == R.id.nav_log) {
                logoutUser();
            }

        }else{
            if (id == R.id.nav_notif || id == R.id.nav_friend || id == R.id.nav_cctv || id == R.id.nav_transport ) {
                showAuthAlert();

            } else if (id == R.id.nav_log) {
                Intent in = new Intent(SemutActivity.this, PreloginActivity.class);
                startActivity(in);
                finish();
            }


        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //------------------------ below this line : Binding toolbar
    private void bindingToolbar(){
        toolbar             = (Toolbar) findViewById(R.id.toolbar);
        toolbar_bottom      = (Toolbar)findViewById(R.id.toolbar_bottom);
        Button tagBtn       = (Button) toolbar_bottom.findViewById(R.id.button1);
        Button filterBtn    = (Button) toolbar_bottom.findViewById(R.id.button2);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Semut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_menu)
                .color(Color.WHITE)
                .sizeDp(24));

        tagBtn.setCompoundDrawables(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_add_location)
                .color(Color.parseColor("#317589"))
                .sizeDp(14), null, null, null);
        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTagView();
            }
        });

        filterBtn.setCompoundDrawables(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_filter_none)
                .color(Color.parseColor("#317589"))
                .sizeDp(14), null, null, null);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });

        toolbar_bottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        // TODO
                        break;
                    // TODO: Other cases
                }
                return true;
            }
        });
        // Inflate a menu to be displayed in the toolbar
        toolbar_bottom.inflateMenu(R.menu.menu_front);
    }



    //--------------------- below this line : binding Fab
    private void bindingFab(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_my_location)
                .color(Color.WHITE)
                .sizeDp(14));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  map.invalidate();
                toMyLocation();
            }
        });
    }


    //-------------------- below this line : binding map
    private void bindingMap(){
        pinDetail = (LinearLayout) findViewById(R.id.pinDetail);
        map = (MapView) findViewById(R.id.mapfront);
    //    map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(25);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        pinDetail.setVisibility(View.GONE);
        isPopup = false;
        btnMenu.setVisibility(View.INVISIBLE);
        btnFilter.setVisibility(View.INVISIBLE);
        btnTag.setVisibility(View.INVISIBLE);
        btnMyLocation.setVisibility(View.INVISIBLE);
        toolbar_bottom.setVisibility(View.VISIBLE);

        return true;
    }


    //--------------------------------- below this line : set Start location
    private void iniMap(double lat, double lon) {
        startPoint = new GeoPoint(lat, lon);
        Marker initMarker = new Marker(map);
        initMarker.setIcon(getResources().getDrawable(R.drawable.marker_user_default));
        initMarker.setPosition(startPoint);
        map.getOverlays().clear();
        map.getOverlays().add(initMarker);
        map.invalidate();
        mapController.setCenter(startPoint);
    }


    //--------------------------------- below this line : cek gps and internet
    private void checkGps(){
        gps = new GPSTracker(SemutActivity.this);
        // check if internet found
        if(gps.isInternetAvailable(SemutActivity.this) == false){
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(SemutActivity.this);
            alertDialog.setTitle("No connection");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Internet connection not found. Do you want to go to settings menu ?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    SemutActivity.this.startActivity(intent);
                    finish();
                }
            });
            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }else {
            // if conection available, check gps
            if(gps.canGetLocation()){
                double glatitude = gps.getLatitude();
                double glongitude = gps.getLongitude();
                Log.i("GPS TRACKER", "Your Location is - \nLat: " + glatitude + "\nLong: " + glongitude);
            }else{
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(SemutActivity.this);
                alertDialog.setTitle("GPS disabled");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ?");
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        SemutActivity.this.startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }


    //-------------------------------- below this line : logoutuser
    private void logoutUser() {
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(SemutActivity.this, PreloginActivity.class);
        startActivity(intent);
        finish();
    }

    //-------------------------------- below this line : getProfile
    private void getprofile(){

        final Integer Sessid = session.getSessid();

        final String DID = deviceID;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_MYPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Cek Response: " , response);


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    if (success) {

                        iniMap(currentLatitude, currentLongitude);
                        JSONObject myprofile = jObj.getJSONObject("Profile");
                        user_name = myprofile.getString("Name");
                        String user_poin = myprofile.getString("Poin");
                        email_p = myprofile.getString("Email");
                        Integer user_level = myprofile.getInt("Poinlevel");
                        Integer user_avatar = myprofile.getInt("AvatarID");
                        Integer user_reputation = myprofile.getInt("Reputation");
                        String nLevel = "";

                        if(user_level <=1000 ){
                            nLevel = "Newbie";
                        }
                        if(user_level <=10000 && user_level > 1000){
                            nLevel = "Addict";
                        }
                        if(user_level <=50000 && user_level > 10000){
                            nLevel = "Geek";
                        }
                        if(user_level >50000){
                            nLevel = "Freak";
                        }

                        setMenudrawer(1, nLevel, user_poin.toString());




                    }else{
                        if(Msg.equals("Session Time Out")){
                            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(SemutActivity.this);
                            alertDialog.setTitle("Session Time Out");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Session time out, it seems you signin on other devices.");
                            alertDialog.setPositiveButton("Signin", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {
                                    logoutUser();
                                }
                            });
                            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(Sessid));
                headers.put("deviceid",DID);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "myprofile");
    }

    //-------------------------------- below this line : setMenudrawer
    private void setMenudrawer(int state, String level, String point){

        //--------- drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_test_drawer, null);
        navigationView.addHeaderView(header);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        LinearLayout linearHeader     = (LinearLayout)header.findViewById(R.id.linearHeader);
        ImageView profic              = (ImageView)header.findViewById(R.id.imageView);
        TextView nameText             = (TextView)header.findViewById(R.id.nameText);
        TextView emailText            = (TextView)header.findViewById(R.id.emailText);
        TextView levelText            = (TextView)header.findViewById(R.id.levelText);
        TextView pointText            = (TextView)header.findViewById(R.id.pointText);
        ImageView levelIco            = (ImageView)header.findViewById(R.id.levelIco);
        ImageView pointIco            = (ImageView)header.findViewById(R.id.pointIco);

        profic.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .sizeDp(48)
                .color(Color.WHITE));
        levelIco.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_layers)
                .sizeDp(18)
                .color(Color.WHITE));
        pointIco.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_favorite)
                .sizeDp(18)
                .color(Color.WHITE));


        nameText.setText(user_name);
        emailText.setText(email_p);
        levelText.setText("Level : "+level);
        pointText.setText("Point : " + point);

        navigationView.setNavigationItemSelectedListener(this);

        MenuItem notifItem = navigationView.getMenu().findItem(R.id.nav_notif);
        notifItem.setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_notifications)
                .sizeDp(18)
                .color(getResources().getColor(R.color.primary)));

        MenuItem friendItem = navigationView.getMenu().findItem(R.id.nav_friend);
        friendItem.setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_people)
                .sizeDp(18)
                .color(getResources().getColor(R.color.primary)));

        MenuItem cctvItem = navigationView.getMenu().findItem(R.id.nav_cctv);
        cctvItem.setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_switch_video)
                .sizeDp(18)
                .color(getResources().getColor(R.color.primary)));

        MenuItem transportItem = navigationView.getMenu().findItem(R.id.nav_transport);
        transportItem.setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_directions_bus)
                .sizeDp(18)
                .color(getResources().getColor(R.color.primary)));

        MenuItem logItem = navigationView.getMenu().findItem(R.id.nav_log);
        logItem.setIcon(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_exit_to_app)
                .sizeDp(18)
                .color(getResources().getColor(R.color.primary)));

        if(state == 0){
            linearHeader.setVisibility(View.GONE);
            logItem.setTitle("Login");
        }
    }


    private void showAuthAlert(){
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(SemutActivity.this);
        alertDialog.setTitle("Authentication required!");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Authentication required to access this features, do you want to Signup or Signin ?");
        alertDialog.setPositiveButton("Signin / Signup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(SemutActivity.this, PreloginActivity.class);
                SemutActivity.this.startActivity(intent);
                finish();
            }
        });
        alertDialog.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    //-------------------------------------------------- Menu ---------------------------------------------//

    //-------------- animate to my location
    private void toMyLocation(){
        startPoint = new GeoPoint(currentLatitude, currentLongitude);
        mapController.animateTo(startPoint);
    }

    //-------------- show tag
    private void showTagView(){
        toMyLocation();
        if(session.isLoggedIn()){
            Intent intent = new Intent(SemutActivity.this, TagsActivity.class);
            startActivity(intent);
        } else {
            showAuthAlert();
        }
    }

    //------------- show filetr
    private void showFilter(){
        keepGoing = true;
        Intent intent = new Intent(SemutActivity.this, MapFilterActivity.class);
        startActivity(intent);
    }

    //----------- runnable, show tag on map
    @Override
    public void run() {
        if(!continueRequest)return;
        if(map == null)return;

        continueRequest = false;

        String tag_string_req = "mapview";

        String params = "?Radius=" + (map.getBoundingBox().getDiagonalLengthInMeters() * 0.5) + "&Limit=10&Latitude=" + map.getMapCenter().getLatitude() + "&Longitude=" + map.getMapCenter().getLongitude() + "&Item=" + FilterMapValue;

        Log.d("Mapview params ", params);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_MAPVIEW + params, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Map ", "MapView Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // bersih bersih marker lalu ditambahin ulang
                        map.getOverlays().clear();




                        //marker parking lskk
//                        GeoPoint lskkPoint = new GeoPoint(-6.8908740231233825, 107.6110321842134);
//                        Marker lskkmarker = new Marker(map);
//                        lskkmarker.setPosition(lskkPoint);
//                        lskkmarker.setIcon((getResources().getDrawable(R.drawable.marker_parking)));
//                        map.getOverlays().add(lskkmarker);

                        JSONArray arr = jObj.optJSONArray("Users");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_FRIEND, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Cameras");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_CAMERA, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }


                        arr = jObj.optJSONArray("Commuter");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_STATION, obj.optDouble("stop_lat"), obj.optDouble("stop_lon"), obj);
                        }


                        arr = jObj.optJSONArray("Angkot");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_ANGKOT, obj.optDouble("LAT"), obj.optDouble("LON"), obj);
                        }




//                        arr = jObj.optJSONArray("CommuterTrain");
//                        for(int i=0; i<arr.length(); i++){
//                            JSONObject obj = arr.getJSONObject(i);
//
//                            MainActivity.this.addMarker(MARKER_OTHER, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
//                        }
//
//                        arr = jObj.optJSONArray("Angkot");
//                        for(int i=0; i<arr.length(); i++){
//                            JSONObject obj = arr.getJSONObject(i);
//
//                            MainActivity.this.addMarker(MARKER_OTHER, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
//                        }

                        arr = jObj.optJSONArray("Polices");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_POLICE, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Accidents");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_ACCIDENT, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Traffics");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_TRAFFIC, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Others");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            SemutActivity.this.addMarker(MARKER_OTHER, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }



                        JSONObject json = new JSONObject();
                        try {
                            json.put("type", MARKER_ME);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SemutActivity.this.addMarker(MARKER_ME, currentLatitude, currentLongitude, json );
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

                continueRequest = true;

                map.invalidate();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "MapView Error: " + error.getMessage());
                Snackbar.make(getCurrentFocus(), "Connection not stable. Please check your connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                continueRequest = true;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //------------------------ void to add marker on map
    private void addMarker(int type, double Latitude, double Longitude, JSONObject info){
        GeoPoint startPoint = new GeoPoint(Latitude, Longitude);

        Marker marker = new Marker(map);
        marker.setPosition(startPoint);
        try {
            info.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        marker.setRelatedObject(info);
        marker.setOnMarkerClickListener(this);

        switch (type){
            case MARKER_FRIEND:
                marker.setIcon(info.optInt("Gender") == 1?getResources().getDrawable(R.drawable.pin_male):getResources().getDrawable(R.drawable.pin_female));
                break;
            case MARKER_CAMERA:
                marker.setIcon(getResources().getDrawable(R.drawable.pin_cctv));
                break;
            case MARKER_POLICE:
                marker.setIcon(getResources().getDrawable(R.drawable.pin_police));
                break;
            case MARKER_ACCIDENT:
                marker.setIcon(getResources().getDrawable(R.drawable.pin_caution));
                break;
            case MARKER_TRAFFIC:
                marker.setIcon(getResources().getDrawable(R.drawable.pin_vlc));
                break;
            case MARKER_OTHER:
                marker.setIcon(getResources().getDrawable(R.drawable.pin_close));
                break;
            case MARKER_STATION:
                marker.setIcon(getResources().getDrawable(R.drawable.marker_station));
                break;
            case MARKER_ANGKOT:
                marker.setIcon(getResources().getDrawable(R.drawable.marker_angkot));
                break;
            case MARKER_ME:
                marker.setIcon(getResources().getDrawable(R.drawable.marker_user_default));
                break;
        }

        map.getOverlays().add(marker);
    }

    //--------------------------- void to markerclick / override
    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        pinDetail.setVisibility(View.VISIBLE);

        btnMenu.setVisibility(View.GONE);
        btnFilter.setVisibility(View.GONE);
        btnTag.setVisibility(View.GONE);
        btnMyLocation.setVisibility(View.GONE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        JSONObject obj = (JSONObject)marker.getRelatedObject();

        if(obj.optInt("type") == MARKER_FRIEND) {
            PinDetailUserFragment fragment = new PinDetailUserFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
            toolbar_bottom.setVisibility(View.INVISIBLE);
        }else if(obj.optInt("type") == MARKER_CAMERA){
            PinDetailCCTVFragment fragment = new PinDetailCCTVFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
            toolbar_bottom.setVisibility(View.INVISIBLE);
        }else if(obj.optInt("type") == MARKER_STATION){
            PinDetailStationFragment fragment = new PinDetailStationFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
            toolbar_bottom.setVisibility(View.INVISIBLE);
        } else if(obj.optInt("type") == MARKER_ANGKOT){
            PinDetailAngkotFragment fragment = new PinDetailAngkotFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
            toolbar_bottom.setVisibility(View.INVISIBLE);
        }else if(obj.optInt("type") == MARKER_ME){
          //  Toast.makeText(SemutActivity.this, "Nah, just me", Toast.LENGTH_LONG).show();
            pinDetail.setVisibility(View.GONE);
        }
        else {
            PinDetailTagFragment fragment = new PinDetailTagFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
            toolbar_bottom.setVisibility(View.INVISIBLE);
        }

        fragmentTransaction.commit();

        return true;
    }
}