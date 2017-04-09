package project.bsts.semut.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import project.bsts.semut.connections.broker.BrokerCallback;
import project.bsts.semut.connections.broker.Config;
import project.bsts.semut.connections.broker.Consumer;
import project.bsts.semut.connections.broker.Factory;
import project.bsts.semut.connections.broker.Producer;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.helper.JSONRequest;
import project.bsts.semut.helper.PreferenceManager;
import project.bsts.semut.pojo.Profile;
import project.bsts.semut.pojo.Session;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.utilities.GetCurrentDate;
import project.bsts.semut.utilities.LocationUtilities;
import project.bsts.semut.utilities.MapItem;
import project.bsts.semut.utilities.NumUtils;
import project.bsts.semut.utilities.ScheduleTask;


public class GetLocation extends Service implements BrokerCallback {
    double altService;
    double latService;
    double lngService;
    double spdService;
    private LocationManager locationManager;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private String TAG = this.getClass().getSimpleName();
    MyLocationListener listener;

    private BroadcastManager broadcastManager;
    private ScheduleTask task;
    private Factory mqFactory;
    private Consumer mqConsumer;
    private Producer mqProducer;
    private PreferenceManager preferenceManager;
    Session session;
    Profile profile;
    private boolean isFirstInit = true, isMqConnectionError = false, isWithStroring = true;
    private Intent intent;

    @Override
    public IBinder onBind(Intent i) {

        return null;
    }

    @Override
    public void onCreate() {
        Log.i("STATUS", "Get Loc Service Created");

        broadcastManager = new BroadcastManager(getApplicationContext());
        preferenceManager = new PreferenceManager(getApplicationContext());
        session = new Gson().fromJson(preferenceManager.getString(Constants.PREF_SESSION_ID), Session.class);
        profile = new Gson().fromJson(preferenceManager.getString(Constants.PREF_PROFILE), Profile.class);
        if(isWithStroring) connectToRabbit();
        if(isWithStroring) consume();
        handler = new Handler();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = LocationUtilities.getLastBestLocation(locationManager);
        if(loc != null){
            latService = loc.getLatitude();
            lngService = loc.getLongitude();

        }
    }


    private void startCollectingMap(){
        if(isFirstInit){
            startTask();
            isFirstInit = false;
        }
    }


    public int onStartCommand(Intent i, int flags, int startId) {
        this.intent = i;
        isWithStroring = intent.getBooleanExtra(Constants.INTENT_LOCATION_WITH_STORING, true);
        broadCastMessage(Constants.BROADCAST_MY_LOCATION, JSONRequest.myLocation(latService, lngService));
        if(isWithStroring) startCollectingMap();
        Log.i("SHIT", String.valueOf(isWithStroring));

        final Runnable r = new Runnable() {
            public void run() {
                Criteria criteria = new Criteria();
                String best = locationManager.getBestProvider(criteria, true);
                listener = new MyLocationListener();
                locationManager.requestLocationUpdates(best, 400, 0, listener);
                if (latService!=0.0 || lngService!=0.0){
                    if(isWithStroring) startCollectingMap();
                    Log.i("STATUS", "Location changed Alt: " + altService + " Lat: " + latService + " Lon: " + lngService + " Spd: " + spdService);
                    preferenceManager.save((float)latService, Constants.ENTITY_LATITUDE);
                    preferenceManager.save((float)lngService, Constants.ENTITY_LONGITUDE);
                    preferenceManager.apply();
                    broadCastMessage(Constants.BROADCAST_MY_LOCATION, JSONRequest.myLocation(latService, lngService));
                }
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(r, 10000);
        return START_STICKY;
    }


    private void connectToRabbit() {
        this.mqFactory = new Factory(Config.hostName, Config.virtualHostname, Config.username, Config.password, Config.exchange, Config.rotuingkey, Config.port);
        this.mqConsumer = this.mqFactory.createConsumer(this);

    }

    private void consume(){

        mqConsumer.setQueueName(profile.getID()+"-"+session.getSessionID());
        mqConsumer.subsribe();
        mqConsumer.setMessageListner(delivery -> {
            try {
                final String message = new String(delivery.getBody(), "UTF-8");
                Log.i(TAG, "-------------------------------------");
                Log.i(TAG, "incoming message type : "+delivery.getProperties().getType());
                Log.i(TAG, "-------------------------------------");
                broadCastMessage(delivery.getProperties().getType(), message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });
    }

    private void publish(){
        mqProducer = mqFactory.createProducer(GetLocation.this);
        String corrId = java.util.UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .replyTo(mqConsumer.getQueueName())
                .correlationId(corrId)
                .type(Constants.MQ_INCOMING_TYPE_MAPVIEW)
                .build();
        mqProducer.setRoutingkey(Constants.ROUTING_KEY_UPDATE_LOCATION);
        String message = JSONRequest.storeLocation(session.getSessionID(), altService, NumUtils.round(latService, 7), NumUtils.round(lngService,7), spdService,
                GetCurrentDate.now(), preferenceManager.getInt(Constants.MAP_RADIUS, 3) * 1000,
                preferenceManager.getInt(Constants.MAP_LIMIT, 6), MapItem.get(getApplicationContext()), preferenceManager.getInt(Constants.IS_ONLINE, 0));
        Log.i(TAG, message);
        mqProducer.publish(message, props, false);
    }

    private void broadCastMessage(String type, String message){
        switch (type){
            case Constants.BROADCAST_MY_LOCATION:
                broadcastManager.sendBroadcastToUI(type, message);
                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:
                broadcastManager.sendBroadcastToUI(type, message);
                if(isMqConnectionError){
                    broadCastMessage(Constants.BROADCAST_CONNECTION_STABLE, "Connection Stable");
                    isMqConnectionError = false;
                }
                break;
            case Constants.BROADCAST_CONNECTION_ERROR:
                broadcastManager.sendBroadcastToUI(type, message);
                break;
            case Constants.BROADCAST_CONNECTION_STABLE:
                broadcastManager.sendBroadcastToUI(type, message);
        }
    }

    private void startTask() {
        task = new ScheduleTask(30, periode -> {
            Log.i(TAG, "Send no : "+periode);
            publish();
        });
        task.startHandler();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(runnable);
        if(task != null)task.stop();
        if(mqConsumer != null) mqConsumer.stop();
        if(mqProducer != null) mqProducer.stop();
        Log.i("STATUS", "Get Loc Service Stoped");
    }

    @Override
    public void onMQConnectionFailure(String message) {
        isMqConnectionError = true;
        Log.i(TAG, message);
        broadCastMessage(Constants.BROADCAST_CONNECTION_ERROR, "Connection not Stable");
    }

    @Override
    public void onMQDisconnected() {

    }

    @Override
    public void onMQConnectionClosed(String message) {
      //  Log.i(TAG, message);
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
            Criteria criteria = new Criteria();
            String best = locationManager.getBestProvider(criteria, true);
            if (locationManager.isProviderEnabled(best)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            }
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String best = locationManager.getBestProvider(criteria, true);
            if (locationManager.isProviderEnabled(best)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            }
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Log.v(TAG, "Status Changed: Out of Service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.v(TAG, "Status Changed: UNAVAILABLE");
                    break;
                case LocationProvider.AVAILABLE:
                    Log.v(TAG, "Status Changed: Available");
                    break;
            }
        }

    }

}