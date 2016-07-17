package project.bsts.semut.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import project.bsts.semut.helper.SQLiteHandler;
import project.bsts.semut.helper.SessionManager;

public class GetLocation extends Service {
    double altService;
    double latService;
    double lngService;
    double spdService;
    private LocationManager locationManager;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private SQLiteHandler db;
    private SessionManager session;
    MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        db = new SQLiteHandler(context);
        Log.i("STATUS", "Get Loc Service Created");
        handler = new Handler();
        // Initialize the location fields
        session = new SessionManager(context);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        final Runnable r = new Runnable() {
            public void run() {
                listener = new MyLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0, listener);
                if (latService!=0.0 || lngService!=0.0){
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    Date currentLocalTime = cal.getTime();
                    DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    String timespan = date.format(currentLocalTime);
                    Log.i("STATUS", "Location changed Alt: " + altService
                            + " Lat: " + latService
                            + " Lon: " + lngService
                            + " Spd: " + spdService);

                    session.setLocation(latService, lngService);
                    db.addLocation(altService, latService, lngService, spdService, timespan);
                }
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(r, 10000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(runnable);
        Log.i("STATUS", "Get Loc Service Stoped");
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc)
        {
            altService = loc.getAltitude();
            latService = loc.getLatitude();
            lngService = loc.getLongitude();
            spdService = loc.getSpeed();
        }

        public void onProviderDisabled(String provider)
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            }
         //   Log.i("STATUS", "Gps Disabled");
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        //    Log.i("STATUS", "Gps Enabled");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            }
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }

}
