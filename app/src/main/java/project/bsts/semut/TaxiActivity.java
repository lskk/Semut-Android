package project.bsts.semut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.bean.AutoCompleteBean;
import project.bsts.semut.helper.SessionManager;
import project.bsts.semut.imgutil.ImageLoader;


public class TaxiActivity extends Activity {
    private ArrayList<AutoCompleteBean> resultList;
    private ArrayList<Double> locationResult;

    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_SEARCH = "/search";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyA65-eqSvIefv4lY3vARmN4fwVc1d4lPaE";
    ImageButton btnBack;
    ImageButton btnHistory;
    Button orderTaxi;
    Button cancelOrder;
    double latService,lngService;
    TextView ordertext;
    TextView searchtext;
    TextView introtext;
    JSONObject reservID;
    private ProgressDialog pDialog;
    final Context context = this;
    private DashedCircularProgress cekProgress;
    public Handler handler = null;
    SessionManager session;
    TelephonyManager tm;
    String deviceID;
    Timer timer;
    TimerTask timerTask;
    Timer timercektaker;
    TimerTask cektakerTask;
    boolean setFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_taxi);
        ordertext = (TextView)findViewById(R.id.ordertaxi);
        searchtext = (TextView)findViewById(R.id.searchtaxi);
        introtext = (TextView)findViewById(R.id.taxi_intro);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnHistory = (ImageButton)findViewById(R.id.btn_taxi_his);
        orderTaxi = (Button)findViewById(R.id.btnOrdertaxi);
        cancelOrder = (Button) findViewById(R.id.btncancelOrdertaxi);
        cekProgress = (DashedCircularProgress)findViewById(R.id.cekprogress);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        handler = new Handler();

        tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tm.getDeviceId();
        session = new SessionManager(context);
        if (session.getSessid()==0){
            btnHistory.setVisibility(View.GONE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(context,PublictransActivity.class);
                startActivity(iback);
                finish();
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ihistory = new Intent(context,TaxiOrderListActivity.class);
                startActivity(ihistory);
            }
        });
        orderTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer sessionid = session.getSessid();
                final String deviceid = deviceID;
                final String sLat = String.valueOf(session.getLatitude());
                final String sLon = String.valueOf(session.getLongitude());
                final String Direct = "Unnamed Address";
                final String fLat = sLat;
                final String fLon = sLon;
                Log.d("Request params", String.valueOf(sessionid) + " | " + sLat + " | " + sLon + " | " + Direct + " | " + fLat + " | " + fLon + " | " + deviceid);
                if (session.getSessid()!=0){
                    pDialog.setMessage("Preparing data....");
                    showDialog();
                    StringRequest strReq = new StringRequest(Request.Method.GET,
                            AppConfig.API_CEKORDEREXIST, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("Cek Response: " , response);
                            hideDialog();
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean success = jObj.getBoolean("success");
                                String Msg = jObj.getString("Message");

                                // Check for error node in json
                                if (success) {
                                    JSONObject orderdata = jObj.getJSONObject("Orderdata");
                                    final String reservID = orderdata.getString("ReservationID");
                                    AlertDialog.Builder alertnotfound = new AlertDialog.Builder(context);
                                    // set title
                                    alertnotfound.setTitle("Alert");

                                    // set dialog message
                                    alertnotfound
                                            .setMessage("You have active order right now! Wait till your order finished.")
                                            .setCancelable(true)
                                            .setPositiveButton("Show Detail Order", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent idetail = new Intent(context,TaxiOrderActivity.class);
                                                    idetail.putExtra("reservationid", reservID);
                                                    startActivity(idetail);
                                                }
                                            })
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertnotfound.create();
                                    alertDialog.show();
                                }else {
                                    if (sLat!="0.0" && sLon!="0.0" ) {

                                        AlertDialog.Builder asklocation = new AlertDialog.Builder(context);
                                        // set title
                                        asklocation.setTitle("Your Destination");

                                        // set dialog message
                                        asklocation
                                                .setMessage("Do you want to set your destionation so we can estimate the cost?")
                                                .setCancelable(true)
                                                .setPositiveButton("Set Destination", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        final Dialog setdestination = new Dialog(context);
                                                        setdestination.setContentView(R.layout.taxi_set_direction);
                                                        final AutoCompleteTextView autoCompView = (AutoCompleteTextView)setdestination.findViewById(R.id.search_destination);
                                                        final EditText textaddress = (EditText)setdestination.findViewById(R.id.setaddress);
                                                        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(R.layout.auto_place_list));
                                                        autoCompView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                                                System.out.println("click");
                                                                Log.e("Address: ", resultList.get(position).getDescription());
                                                                setFrom = true;
                                                                textaddress.setText(resultList.get(position).getDescription());
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        locationResult = Details(resultList.get(position).getDescription(), resultList.get(position).getReference());
                                                                        System.out.println(locationResult.get(0) + " - " + locationResult.get(1));
                                                                    }
                                                                }).start();
                                                            }
                                                        });

                                                        setdestination.setTitle("Set Your Destination");
                                                        setdestination.setCancelable(false);
                                                        MapView mapset = (MapView)setdestination.findViewById(R.id.mapsetdestination);
                                                        mapset.setTileSource(TileSourceFactory.MAPQUESTOSM);
                                                        mapset.setMultiTouchControls(true);

                                                        GeoPoint startPoint = new GeoPoint(session.getLatitude(), session.getLongitude());
                                                        IMapController mapController = mapset.getController();
                                                        mapController.setZoom(15);
                                                        mapController.setCenter(startPoint);

                                                        Marker smarker = new Marker(mapset);
                                                        smarker.setPosition(startPoint);
                                                        smarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                                        smarker.setIcon(getResources().getDrawable(R.drawable.marker_start));
                                                        mapset.getOverlays().add(smarker);

                                                        final Marker fmarker = new Marker(mapset);
                                                        fmarker.setPosition(startPoint);
                                                        fmarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                                        fmarker.setIcon(getResources().getDrawable(R.drawable.marker_finish));
                                                        mapset.getOverlays().add(fmarker);
                                                        fmarker.setDraggable(true);
                                                        fmarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                                                            @Override
                                                            public void onMarkerDrag(Marker marker) {

                                                            }

                                                            @Override
                                                            public void onMarkerDragEnd(Marker marker) {
                                                                setFrom = false;
                                                                Log.d("new position", String.valueOf(fmarker.getPosition()));

                                                            }

                                                            @Override
                                                            public void onMarkerDragStart(Marker marker) {
                                                            }
                                                        });

                                                        Button cancelset = (Button) setdestination.findViewById(R.id.btn_cancel_set_destination);
                                                        cancelset.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                setdestination.dismiss();
                                                            }
                                                        });
                                                        Button doneset = (Button) setdestination.findViewById(R.id.btn_done_set_destination);
                                                        doneset.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (setFrom){
                                                                    requesttaxi(String.valueOf(sessionid), sLat, sLon, String.valueOf(textaddress.getText()), String.valueOf(locationResult.get(0)), String.valueOf(locationResult.get(1)), deviceid);
                                                                    setdestination.dismiss();
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Location: "+locationResult.get(0)+" - "+locationResult.get(1)+" Address: "+textaddress.getText(), Toast.LENGTH_LONG)
                                                                            .show();
                                                                }else {
                                                                    requesttaxi(String.valueOf(sessionid), sLat, sLon, "Unknown Address", String.valueOf(fmarker.getPosition().getLatitude()), String.valueOf(fmarker.getPosition().getLongitude()), deviceid);
                                                                    setdestination.dismiss();
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Location: "+fmarker.getPosition().getLatitude()+" - "+fmarker.getPosition().getLongitude()+" Address: Unknown Address", Toast.LENGTH_LONG)
                                                                            .show();
                                                                }

                                                            }
                                                        });
                                                        setdestination.show();
                                                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                        Window window = setdestination.getWindow();
                                                        lp.copyFrom(window.getAttributes());
                                                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                                                        window.setAttributes(lp);
                                                    }
                                                })
                                                .setNegativeButton("Order Now", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        requesttaxi(String.valueOf(sessionid), sLat, sLon, Direct, fLat, fLon, deviceid);
                                                        dialog.cancel();
                                                    }
                                                });

                                        // create alert dialog
                                        AlertDialog alertDialog = asklocation.create();
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
                            headers.put("sessid", String.valueOf(sessionid));
                            headers.put("deviceid",deviceid);
                            return headers;
                        }

                    };
                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(strReq, "cek_taxi");

                }else{
                    AlertDialog.Builder alertnotfound = new AlertDialog.Builder(context);
                    // set title
                    alertnotfound.setTitle("Oops, sorry.");

                    // set dialog message
                    alertnotfound
                            .setMessage("You must login to access this feature!")
                            .setCancelable(false)
                            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent tologin = new Intent(getApplicationContext(),PreloginActivity.class);
                                    startActivity(tologin);
                                    finish();
                                }
                            })
                            .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertnotfound.create();
                    alertDialog.show();
                }
            }
        });

    }

    private void animate() {
        cekProgress.setValue(599);
    }
    @Override
    public void onBackPressed() {
        Intent ipublictrans = new Intent(context, PublictransActivity.class);
        startActivity(ipublictrans);
        finish();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    private void alerttimeup(final String Sessid,
                             final String ReservID,
                             final String DID){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder alertnotfound = new AlertDialog.Builder(context);
                        // set title
                        alertnotfound.setTitle("Cancel Confirmation");

                        // set dialog message
                        alertnotfound
                                .setMessage("Cancel Order message!")
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        cekProgress.reset();
                                        timer.cancel();
                                        timer = null;
                                        timercektaker.cancel();
                                        timercektaker = null;
                                        animate();
                                        alerttimeup(Sessid,ReservID,DID);
                                        cektaker(Sessid,ReservID,DID);
                                    }
                                })
                                .setNegativeButton("Don't Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        timer.cancel();
                                        timer = null;
                                        timercektaker.cancel();
                                        timercektaker = null;
                                        cancelorder(Sessid,ReservID,DID);
                                        //dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertnotfound.create();
                        alertDialog.show();
                    }
                });
            }
        };
        timer.schedule(timerTask, 60000);
    }

    private void requesttaxi(final String Sessid,
                             final String LocationLat,
                             final String LocationLon,
                             final String Direction,
                             final String DirectionLat,
                             final String DirectionLon,
                             final String DID){
        pDialog.setMessage("Submiting Reservation....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_RESERVATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Reservation Response: " , response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        hideDialog();
                        final String reservationID = jObj.getString("ReservationID");
                        orderTaxi.setVisibility(View.GONE);
                        ordertext.setVisibility(View.GONE);
                        introtext.setVisibility(View.GONE);
                        btnBack.setClickable(false);
                        cekProgress.setVisibility(View.VISIBLE);
                        searchtext.setVisibility(View.VISIBLE);
                        cekProgress.reset();
                        animate();
                        alerttimeup(Sessid, reservationID, DID);
                        cektaker(Sessid, reservationID, DID);

                        cancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder alertCancelConfirm = new AlertDialog.Builder(context);
                                // set title
                                alertCancelConfirm.setTitle("Cancel Confirmation");

                                // set dialog message
                                alertCancelConfirm
                                        .setMessage("Cancel Order message!")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                timer.cancel();
                                                timercektaker.cancel();
                                                timer = null;
                                                timercektaker = null;
                                                cancelorder(Sessid, reservationID, DID);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                // create alert dialog
                                AlertDialog alertDialog = alertCancelConfirm.create();

                                // show it
                                alertDialog.show();
                            }
                        });

                    } else { //success = false;
                        hideDialog();
                        Log.e("Error request: ", Msg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error volley request: " , error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid",Sessid);
                headers.put("deviceid",DID);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("LocationLat", LocationLat);
                params.put("LocationLon", LocationLon);
                params.put("Direction", Direction);
                params.put("DirectionLat",DirectionLat);
                params.put("DirectionLon",DirectionLon);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "request_taxi");
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void cektaker(final String Sessid,
                          final String ReservationID,
                          final String DID){
        timercektaker = new Timer();
        cektakerTask = new TimerTask() {
            @Override
            public void run() {
                AppController.getInstance().getRequestQueue().cancelAll("cek_taxi");
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_CEKRESERVATION+"?ReservationID="+ReservationID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Cek Response: " , response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        JSONObject taker = jObj.getJSONObject("TaxiData");
                        timer.cancel();
                        timercektaker.cancel();
                        timer = null;
                        timercektaker = null;
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.taxi_confirm);
                        dialog.setCancelable(false);
                        TextView takerdriver = (TextView)dialog.findViewById(R.id.driver_name);
                        takerdriver.setText(taker.optString("Driver"));
                        TextView takernopol = (TextView)dialog.findViewById(R.id.driver_nopol);
                        takernopol.setText(taker.optString("Nopol"));
                        TextView takerphone = (TextView)dialog.findViewById(R.id.driver_phone);
                        takerphone.setText(taker.optString("Phone"));
                        ImageView takerphoto = (ImageView)dialog.findViewById(R.id.driver_photo);
                        int loader = R.drawable.loading;
                        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                        imgLoader.DisplayImage(taker.optString("Photo"), loader, takerphoto);
                        final EditText inputmark = (EditText)dialog.findViewById(R.id.inputmarker);
                        Button cancel_order = (Button) dialog.findViewById(R.id.btn_cancel_order);
                        Button confirmorder = (Button) dialog.findViewById(R.id.btn_confirm_order);
                        RatingBar takerrating = (RatingBar)dialog.findViewById(R.id.taxirating);
                        takerrating.setRating(taker.getInt("DRating"));
                        confirmorder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String Mark = inputmark.getText().toString();
                                confirmtaxi(Sessid, Mark, ReservationID, DID);
                            }
                        });
                        cancel_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelorder(Sessid, ReservationID, DID);
                            }
                        });
                        dialog.show();
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        Window window = dialog.getWindow();
                        lp.copyFrom(window.getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        window.setAttributes(lp);

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
                headers.put("sessid",Sessid);
                headers.put("deviceid",DID);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "cek_taxi");
            }
        };
        timercektaker.schedule(cektakerTask, 1000, 5000);
    }

    private void confirmtaxi(final String Sessid,
                             final String Usermark,
                             final String ReservID,
                             final String DID){
        pDialog.setMessage("Sending confirmation...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_CONFIRM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Confirmation Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        hideDialog();
                        //to detail order activity (with) map
                        Intent iorder = new Intent(TaxiActivity.this, TaxiOrderActivity.class);
                        iorder.putExtra("reservationid",ReservID);
                        startActivity(iorder);
                        finish();
                        Toast.makeText(getApplicationContext(),"Taxi Confirmed",Toast.LENGTH_LONG).show();
                    } else {
                        hideDialog();
                        Log.e("Error request: ", Msg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error volley request: " , error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid",Sessid);
                headers.put("deviceid",DID);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ReservationID", ReservID);
                params.put("Mark", Usermark);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "confirm_taxi");
    }

    private void cancelorder(final String Sessid,
                             final String ReservID,
                             final String DID){
        pDialog.setMessage("Canceling order...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.API_CANCEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Canceling Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");
                    String Msg = jObj.getString("Message");

                    // Check for error node in json
                    if (success) {
                        hideDialog();
                        TaxiActivity.this.recreate();
                        Toast.makeText(getApplicationContext(),"Taxi Canceled",Toast.LENGTH_LONG).show();
                    } else {
                        hideDialog();
                        Log.e("Error request: ", Msg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error volley request: " , error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid",Sessid);
                headers.put("deviceid",DID);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ReservationID", ReservID);

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "cancel_taxi");
    }
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        //        private ArrayList<AutoCompleteBean> resultList;
        private ArrayList<String> result;

        public PlacesAutoCompleteAdapter(int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).getDescription();
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        result = new ArrayList<String>(resultList.size());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};


            return filter;
        }
    }
    private ArrayList<AutoCompleteBean> autocomplete(String input) {

        ArrayList<AutoCompleteBean> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?input=" + URLEncoder.encode(input, "utf8"));
            sb.append("&sensor=true&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<AutoCompleteBean>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new AutoCompleteBean(predsJsonArray.getJSONObject(i).getString("description"), predsJsonArray.getJSONObject(i).getString("reference")));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private ArrayList<Double> Details(String description, String reference ) {

        ArrayList<Double> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?reference=" + URLEncoder.encode(reference, "utf8"));
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject jsonObjResult = jsonObj.getJSONObject("result");
            JSONObject jsonObjGemmetry = jsonObjResult.getJSONObject("geometry");
            JSONObject jsonObjLocation = jsonObjGemmetry.getJSONObject("location");

            System.out.println("jsonObj.toString() :::: " + jsonObj.toString());
            System.out.println("jsonObjLocation.toString() :::: " + jsonObjLocation.toString());

            resultList = new ArrayList<Double>(2);
            resultList.add(jsonObjLocation.getDouble("lat"));
            resultList.add(jsonObjLocation.getDouble("lng"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

}
