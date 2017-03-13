package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.map.osm.MapUtilities;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;

public class MainActivity extends AppCompatActivity implements BroadcastManager.UIBroadcastListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.maposm)

    MapView mapset;
    MapUtilities mapUitilities;
    Context context;

    private IMapController mapController;
    private BroadcastManager broadcastManager;
    private String TAG = this.getClass().getSimpleName();
    private Intent locService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        context = this;
        mapUitilities = new MapUtilities(mapset);
        broadcastManager = new BroadcastManager(context);
        broadcastManager.subscribeToUi(this);
        locService = new Intent(context, LocationService.class);

    }


    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "Receive on UI : Type : "+type);
        Log.i(TAG, msg);
        switch (type){
            case Constants.BROADCAST_MY_LOCATION:
                mapUitilities.setMyLocationGeo(msg);
                if(!mapUitilities.isReady()) mapController = mapUitilities.init();
                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:

                break;
        }
    }
}
