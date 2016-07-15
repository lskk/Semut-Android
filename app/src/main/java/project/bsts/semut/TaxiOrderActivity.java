package project.bsts.semut;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import project.bsts.semut.app.AppConfig;
import project.bsts.semut.app.AppController;
import project.bsts.semut.app.ExceptionHandler;
import project.bsts.semut.helper.SessionManager;
import project.bsts.semut.imgutil.ImageLoader;


public class TaxiOrderActivity extends Activity {
    private static final String TAG = TaxiOrderActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    final Context context = this;
    private ImageView taxiPhoto;
    private TextView taxiDriver;
    private TextView taxiNopol;
    private TextView taxiCompany;
    private TextView taxiPhone;
    private TextView rateDriver;
    private TextView rateCar;
    private TextView ratedDriver;
    private TextView ratedCar;
    private TextView distanceNtime;
    private TextView costEstimate;
    private MapView maptaxi;
    private ImageButton btnBack;
    private IMapController mapController;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_taxi_order);
        Bundle bundle = this.getIntent().getExtras();
        final String ReservID = bundle.getString("reservationid");
        maptaxi = (MapView)findViewById(R.id.maptaxi);
        taxiPhoto = (ImageView)findViewById(R.id.taxi_photo);
        taxiPhone = (TextView)findViewById(R.id.taxi_phone);
        taxiDriver = (TextView)findViewById(R.id.taxi_driver);
        taxiCompany = (TextView)findViewById(R.id.taxi_company);
        taxiNopol = (TextView)findViewById(R.id.taxi_nopol);
        distanceNtime = (TextView)findViewById(R.id.distance_time);
        costEstimate = (TextView)findViewById(R.id.cost_estimate);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnBack = (ImageButton)findViewById(R.id.imageButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rateCar = (TextView)findViewById(R.id.rate_car);
        rateDriver = (TextView)findViewById(R.id.rate_driver);
        ratedCar = (TextView)findViewById(R.id.rated_car);
        ratedDriver = (TextView)findViewById(R.id.rated_driver);
        maptaxi = (MapView) findViewById(R.id.maptaxi);
        maptaxi.setTileSource(TileSourceFactory.MAPQUESTOSM);
        maptaxi.setMultiTouchControls(true);
        mapController = maptaxi.getController();
        handler = new Handler();
        timer = new Timer();

        getData(ReservID);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        //viewingMap();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
    private void getData(final String ReservID) {
        // Tag used to cancel the request
        String tag_string_req = "req_order_detail";

        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.API_ORDERDETAIL+"?ReservationID="+ReservID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Order detail Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        final JSONObject detail = jObj.getJSONObject("TaxiData");
                        taxiDriver.setText(detail.getString("Driver"));
                        taxiNopol.setText(detail.getString("Nopol"));
                        taxiCompany.setText(detail.getString("Company"));
                        taxiPhone.setText(detail.getString("Phone"));
                        int loader = R.drawable.loading;
                        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                        imgLoader.DisplayImage(detail.optString("Photo"), loader, taxiPhoto);
                        final String reservid = detail.getString("ReservationID");
                        GeoPoint startPoint = new GeoPoint(detail.getDouble("Startlat"),detail.getDouble("Startlon"));
                        mapController.setZoom(13);
                        mapController.setCenter(startPoint);
                        Marker startMarker = new Marker(maptaxi);
                        startMarker.setPosition(startPoint);
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_start));
                        maptaxi.getOverlays().add(startMarker);
                        GeoPoint finishPoint = new GeoPoint(detail.getDouble("Directlat"),detail.getDouble("Directlon"));
                        Marker finishMarker = new Marker(maptaxi);
                        finishMarker.setPosition(finishPoint);
                        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        finishMarker.setIcon(getResources().getDrawable(R.drawable.marker_finish));
                        maptaxi.getOverlays().add(finishMarker);
                        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(startPoint);
                        waypoints.add(finishPoint);
                        new DoRouting().execute(waypoints);
                        if (detail.getInt("RateD")==0){
                            ratedDriver.setVisibility(View.GONE);
                            rateDriver.setVisibility(View.VISIBLE);
                            rateDriver.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doRating(reservid, "1");
                                }
                            });
                        }else{
                            rateDriver.setVisibility(View.GONE);
                            ratedDriver.setVisibility(View.VISIBLE);
                        }
                        if (detail.getInt("RateC")==0){
                            ratedCar.setVisibility(View.GONE);
                            rateCar.setVisibility(View.VISIBLE);
                            rateCar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doRating(reservid, "2");
                                }
                            });
                        }else{
                            rateCar.setVisibility(View.GONE);
                            ratedCar.setVisibility(View.VISIBLE);
                        }
                        taxilocation(detail.optString("TaxiID"));

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("Message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Taxi Order detail Error: " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                String deviceid = tm.getDeviceId();

                SessionManager session = new SessionManager(getApplicationContext());
                Integer session_id = session.getSessid();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("API-KEY", "SEMUT_ANDROID");
                headers.put("sessid", String.valueOf(session_id));
                headers.put("deviceid", deviceid);

                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private class DoRouting extends AsyncTask<Object, Void, Road> {
        protected Road doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>)params[0];
            RoadManager roadManager = new OSRMRoadManager();
            //RoadManager roadManager = new MapQuestRoadManager("Fmjtd%7Cluu82q6225%2Cax%3Do5-94zwqu");

            return roadManager.getRoad(waypoints);
        }

        protected void onPostExecute(final Road result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result.mStatus != Road.STATUS_OK)
                        Toast.makeText(maptaxi.getContext(), "Error when loading the road - status=" + result.mStatus, Toast.LENGTH_SHORT).show();

                    Polyline roadOverlay = RoadManager.buildRoadOverlay(result, maptaxi.getContext());
                    maptaxi.getOverlays().add(roadOverlay);
                    if (result.mLength == 0) {
                        costEstimate.setText("Cannot estimating cost.");
                    } else {
                        Integer cost = 7500 + ((int) result.mLength * 3500);
                        costEstimate.setText("Rp. " + String.valueOf(cost));
                    }
                    distanceNtime.setText(String.valueOf(result.mLength)+" km");
                }
            });

        }
    }

    private void taxilocation(final String taxiID){
        final Marker taxiMarker = new Marker(maptaxi);
        taxiMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        taxiMarker.setIcon(getResources().getDrawable(R.drawable.marker_taxi));
        final String tag_string_req = "req_get_taxi_location";
        timerTask = new TimerTask() {
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppController.getInstance().getRequestQueue().cancelAll(tag_string_req);

                        StringRequest strReq = new StringRequest(Request.Method.GET,
                                AppConfig.API_GETTAXILOCATION+"?TaxiID="+taxiID, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "Taxi location Response: " + response);
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    boolean success = jObj.getBoolean("success");

                                    // Check for error node in json
                                    if (success) {
                                        maptaxi.getOverlays().remove(taxiMarker);
                                        JSONObject taxiloc = jObj.getJSONObject("Data");
                                        final GeoPoint taxipoint = new GeoPoint(taxiloc.getDouble("Latitude"),taxiloc.getDouble("Longitude"));
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                taxiMarker.setPosition(taxipoint);
                                                maptaxi.getOverlays().add(taxiMarker);
                                            }
                                        });
                                    } else {
                                        // Error in login. Get the error message
                                        String errorMsg = jObj.getString("Message");
                                        Toast.makeText(getApplicationContext(),
                                                errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // JSON error
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Taxi location Error: " + error.getMessage());
                            }
                        }) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                                String deviceid = tm.getDeviceId();

                                SessionManager session = new SessionManager(getApplicationContext());
                                Integer session_id = session.getSessid();

                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("API-KEY", "SEMUT_ANDROID");
                                headers.put("sessid", String.valueOf(session_id));
                                headers.put("deviceid", deviceid);

                                return headers;
                            }

                        };

                        // Adding request to request queue
                        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000, 5000);

    }

    private void doRating(final String ReservID, final String type){
        final Dialog rankDialog = new Dialog(TaxiOrderActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rate_dialog);
        rankDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        if (type=="1"){
            text.setText("Rate the driver");
        }else{
            text.setText("Rate the taxi car");
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingBar.setRating(rating);

            }
        });

        Button submitButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Rates process"+ReservID+"-"+type+"-"+ratingBar.getRating(), Toast.LENGTH_LONG).show();
                rankDialog.dismiss();

                String tag_string_req = "req_rate";

                pDialog.setMessage("Submitting rating ...");
                showDialog();

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.API_RATETAXI, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean success = jObj.getBoolean("success");
                            String Msg = jObj.getString("Message");
                            // Error in login. Get the error message
                            finish();
                            startActivity(getIntent());
                            Toast.makeText(getApplicationContext(),
                                    Msg, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        hideDialog();
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String deviceid = tm.getDeviceId();

                        SessionManager session = new SessionManager(getApplicationContext());
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
                        params.put("ReservationID", ReservID);
                        params.put("Type", type);
                        params.put("Rating", String.valueOf(ratingBar.getRating()));

                        return params;
                    }

                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }


}
