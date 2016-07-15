package project.bsts.semut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.lang.reflect.Field;
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


public class MainActivity extends Activity implements Runnable, Marker.OnMarkerClickListener, View.OnTouchListener {
    public static int FilterMapValue = 63;
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int MARKER_FRIEND = 0;
    private static final int MARKER_CAMERA = 1;
    private static final int MARKER_POLICE = 2;
    private static final int MARKER_ACCIDENT = 3;
    private static final int MARKER_TRAFFIC = 4;
    private static final int MARKER_OTHER = 5;
    private static final int MARKER_STATION = 6;
    private static final int MARKER_ANGKOT = 7;

    MapView map;
    LinearLayout barNonmember;


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

    String email_p;
    String user_name;
    ImageView bar_icon;

    Drawer result;

    private boolean isPopup = false;

    // GPSTracker class
    GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
		setContentView(R.layout.activity_main);
        // cek gps and network

        gps = new GPSTracker(MainActivity.this);
        // check if GPS enabled
        if(gps.isInternetAvailable(MainActivity.this) == false){
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("No connection");
            alertDialog.setCancelable(false);

            // Setting Dialog Message
            alertDialog.setMessage("Internet connection not found. Do you want to go to settings menu ?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    MainActivity.this.startActivity(intent);
                    finish();
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }else {
            // if conection available, check gps
            if(gps.canGetLocation()){

                double glatitude = gps.getLatitude();
                double glongitude = gps.getLongitude();

                Log.i("GPS TRACKER", "Your Location is - \nLat: " + glatitude + "\nLong: " + glongitude);
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("GPS disabled");
                alertDialog.setCancelable(false);

                // Setting Dialog Message
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ?");

                // On pressing Settings button
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                        finish();
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        }




        FacebookSdk.sdkInitialize(getApplicationContext());



        tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();

        // session manager
        session = new SessionManager(getApplicationContext());

        pinDetail = (LinearLayout) findViewById(R.id.pinDetail);
        map = (MapView) findViewById(R.id.mapfront);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnTag = (Button) findViewById(R.id.btnTag);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnMyLocation = (Button) findViewById(R.id.btn_mylocation);




        barNonmember = (LinearLayout)findViewById(R.id.maintitlebar_nonmember);
        bar_icon = (ImageView)findViewById(R.id.baricon);

        getLocation = new Intent(getApplicationContext(), GetLocation.class);
        storeLocation = new Intent(getApplicationContext(), StoreLocations.class);

        viewingMap();

        continueRequest = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.run();
            }
        }, 5000, 5000);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		if (!session.isSkip()){
            Intent ipre = new Intent(getApplicationContext(), PreloginActivity.class);
            startActivity(ipre);
            finish();
        }

		// Fetching user details from sqlite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("Name");

		// Displaying the user details on the screen
        if(session.isLoggedIn()) {
            getprofile();
        }else{
            barNonmember.setVisibility(View.VISIBLE);
        }

        btnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMenuPopup(v);
            }
        });
        btnTag.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTagView();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepGoing = true;
                Intent intent = new Intent(MainActivity.this, MapFilterActivity.class);
                startActivity(intent);
            }
        });
        map.setOnTouchListener(this);

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewingMap();
                Intent intent = new Intent(getApplicationContext(), FrontActivity.class);
                startActivity(intent);
            }
        });

	}

    // Display anchored popup menu based on view selected
    private void showMenuPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(session.isLoggedIn()?R.menu.main_menu:R.menu.out_menu, popup.getMenu());
        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w("error", "error forcing menu icons to show", e);
            popup.show();
            return;
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        AlertDialog.Builder alertLogoutConfirm = new AlertDialog.Builder(context);
                        // set title
                        alertLogoutConfirm.setTitle("Logout Confirmation");

                        // set dialog message
                        alertLogoutConfirm
                                .setMessage("Are you sure to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        logoutUser();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertLogoutConfirm.create();

                        // show it
                        alertDialog.show();
                        return true;
                    case R.id.menu_login:
                        Intent ilogin = new Intent(getApplicationContext(), PreloginActivity.class);
                        startActivity(ilogin);
                        finish();
                        return true;
                    case R.id.menu_notif:
                        Intent notif = new Intent(getApplicationContext(), NotificationActivity.class);
                        startActivity(notif);
                        return true;
                    case R.id.menu_friend:
                        Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_cctv:
                        Intent cctv = new Intent(getApplicationContext(), CctvActivity.class);
                        startActivity(cctv);
                        return true;
                    case R.id.menu_public_trans:
                        Intent ipublic = new Intent(getApplicationContext(), PublictransActivity.class);
                        startActivity(ipublic);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void showTagView(){
        GeoPoint startPoint = new GeoPoint(session.getLatitude(), session.getLongitude());
        IMapController mapController = map.getController();
        mapController.animateTo(startPoint);

        Intent intent = new Intent(MainActivity.this, TagsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        //viewingMap();
    }
    @Override
    protected void onStart(){
        super.onStart();
        startService(getLocation);
        if (session.getSessid()!=0){
            startService(storeLocation);
        }
    }
    @Override
    protected void onDestroy(){
        timer.cancel();
        super.onDestroy();
        stopService(getLocation);
        stopService(storeLocation);
    }

    void viewingMap(){
        map.setTileSource(TileSourceFactory.MAPQUESTOSM);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(session.getLatitude(), session.getLongitude());
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.setCenter(startPoint);

        Marker usermarker = new Marker(map);
        usermarker.setPosition(startPoint);
        usermarker.setIcon(getResources().getDrawable(R.drawable.marker_user_default));
        map.getOverlays().add(usermarker);

        // test train
    /*    GeoPoint startPointT = new GeoPoint(-6.886169, 107.608236);
        IMapController mapControllerT = map.getController();
        mapControllerT.setZoom(15);
        mapControllerT.setCenter(startPointT);

        Marker usermarkerT = new Marker(map);
        usermarker.setPosition(startPoint);
        usermarker.setIcon(getResources().getDrawable(R.drawable.marker_taxi));
        map.getOverlays().add(usermarkerT); */

        JSONObject json = new JSONObject();
        try {
            json.put("type", MARKER_STATION);
            json.put("Name", "Stasiun nganu");
            json.put("Detail", "Stasiun nganu Stasiun nganu Stasiun nganu Stasiun nganu Stasiun nganu Stasiun nganu Stasiun nganu");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainActivity.this.addMarker(MARKER_STATION, -6.89293, 107.611009, json);

    }

    private void logoutUser() {
		session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, PreloginActivity.class);
		startActivity(intent);
		finish();
	}

    private void getprofile(){
        final Integer Sessid = session.getSessid();

        final String DID = deviceID;
        barNonmember.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_MYPROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Cek Response: " , response);


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        barNonmember.setVisibility(View.VISIBLE);
                        bar_icon.setImageDrawable(getResources().getDrawable(R.drawable.arrow_up));
                        JSONObject myprofile = jObj.getJSONObject("Profile");
                        user_name = myprofile.getString("Name");
                        String user_poin = myprofile.getString("Poin");
                        email_p = myprofile.getString("Email");
                        Integer user_level = myprofile.getInt("Poinlevel");
                        Integer user_avatar = myprofile.getInt("AvatarID");
                        Integer user_reputation = myprofile.getInt("Reputation");



                        // Log.i("WOY", email_p);
                        // Log.i("WOY", user_name);

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

                        setDrawer(nLevel, user_poin.toString());

                    }else{

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

    private void setDrawer(String level, String point){
        // navigation drawer
        //   Log.i("WOY", email_p);
        //   Log.i("WOY", user_name);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg_header_22)
                .addProfiles(
                        new ProfileDrawerItem().withName(user_name).withEmail(email_p).withIcon(getResources().getDrawable(R.drawable.ee1))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(user_name).withIcon(GoogleMaterial.Icon.gmd_account_circle);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Level : "+ level).withIcon(GoogleMaterial.Icon.gmd_layers);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Point : "+ point).withIcon(GoogleMaterial.Icon.gmd_favorite);

//create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .withDisplayBelowStatusBar(false)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if(position == 0){
                            Toast.makeText(MainActivity.this, drawerItem.toString(), Toast.LENGTH_LONG).show();
                        }
                        return  true;

                    }
                })
                .build();
        result.setSelection(item2);
        bar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });

    }

    // method listen activity appearance

    @Override
    protected void onResume() {
        super.onResume();
        keepGoing = false;
        continueRequest = true;
        Log.d("Main #### ", "Resume");

        AppEventsLogger.activateApp(getApplicationContext(), "740525679328657");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(keepGoing)return;

        continueRequest = false;
        Log.d("Main ####", "Pause");

        AppEventsLogger.deactivateApp(this);
    }

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

                        // menambahkan marker sendiri
                        GeoPoint startPoint = new GeoPoint(session.getLatitude(), session.getLongitude());
                        Marker usermarker = new Marker(map);
                        usermarker.setPosition(startPoint);
                        usermarker.setIcon(getResources().getDrawable(R.drawable.marker_user_default));
                        map.getOverlays().add(usermarker);

                        //marker parking lskk
//                        GeoPoint lskkPoint = new GeoPoint(-6.8908740231233825, 107.6110321842134);
//                        Marker lskkmarker = new Marker(map);
//                        lskkmarker.setPosition(lskkPoint);
//                        lskkmarker.setIcon((getResources().getDrawable(R.drawable.marker_parking)));
//                        map.getOverlays().add(lskkmarker);

                        JSONArray arr = jObj.optJSONArray("Users");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            MainActivity.this.addMarker(MARKER_FRIEND, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Cameras");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            MainActivity.this.addMarker(MARKER_CAMERA, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
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

                            MainActivity.this.addMarker(MARKER_POLICE, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Accidents");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            MainActivity.this.addMarker(MARKER_ACCIDENT, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Traffics");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            MainActivity.this.addMarker(MARKER_TRAFFIC, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }

                        arr = jObj.optJSONArray("Others");
                        for(int i=0; i<arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);

                            MainActivity.this.addMarker(MARKER_OTHER, obj.optDouble("Latitude"), obj.optDouble("Longitude"), obj);
                        }
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
        }

        map.getOverlays().add(marker);
    }

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
        }else if(obj.optInt("type") == MARKER_CAMERA){
            PinDetailCCTVFragment fragment = new PinDetailCCTVFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
        }else if(obj.optInt("type") == MARKER_STATION){
            PinDetailStationFragment fragment = new PinDetailStationFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
        }
        else {
            PinDetailTagFragment fragment = new PinDetailTagFragment();
            fragment.setData(obj);
            fragmentTransaction.replace(R.id.pinDetail, fragment);
        }

        fragmentTransaction.commit();

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pinDetail.setVisibility(View.GONE);
        btnMenu.setVisibility(View.VISIBLE);
        btnFilter.setVisibility(View.VISIBLE);
        btnTag.setVisibility(View.VISIBLE);
        btnMyLocation.setVisibility(View.VISIBLE);

        return false;
    }
}
