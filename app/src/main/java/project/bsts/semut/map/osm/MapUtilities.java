package project.bsts.semut.map.osm;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import project.bsts.semut.map.MapViewComponent;
import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.Tracker;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.TranspostMap;
import project.bsts.semut.pojo.mapview.UserMap;
import project.bsts.semut.setup.Constants;

public class MapUtilities {

    MapView mapView;
    private IMapController mapController;
    private UserMap[] userMaps;
    OsmMarker osmMarker;

    public OtherMap[] getOtherMaps() {
        return otherMaps;
    }

    public UserMap[] getUserMaps() {
        return userMaps;
    }

    public CctvMap[] getCctvMaps() {
        return cctvMaps;
    }

    public PoliceMap[] getPoliceMaps() {
        return policeMaps;
    }

    public AccidentMap[] getAccidentMaps() {
        return accidentMaps;
    }

    public TrafficMap[] getTrafficMaps() {
        return trafficMaps;
    }

    public DisasterMap[] getDisasterMaps() {
        return disasterMaps;
    }

    public ClosureMap[] getClosureMaps() {
        return closureMaps;
    }

    private Marker[] userMarkers;

    public Marker[] getOtherMarkers() {
        return otherMarkers;
    }

    public Marker[] getUserMarkers() {
        return userMarkers;
    }

    public Marker[] getCctvMarkers() {
        return cctvMarkers;
    }

    public Marker[] getPoliceMarkers() {
        return policeMarkers;
    }

    public Marker[] getAccidentMarkers() {
        return accidentMarkers;
    }

    public Marker[] getTrafficMarkers() {
        return trafficMarkers;
    }

    public Marker[] getDisasterMarkers() {
        return disasterMarkers;
    }

    public Marker[] getClosureMarkers() {
        return closureMarkers;
    }

    private Marker[] cctvMarkers;
    private Marker[] policeMarkers;
    private Marker[] accidentMarkers;
    private Marker[] trafficMarkers;
    private Marker[] disasterMarkers;
    private Marker[] closureMarkers;
    private Marker[] otherMarkers;
    private Marker[] trackerMarkers;
    private Marker[] transpostMarkers;


    public void setMapObjectsMarkers(String msg){
        userMaps = MapViewComponent.getUsers(MapViewComponent.USER_MAP_COMPONENT, msg);
        cctvMaps = MapViewComponent.getCCTVs(MapViewComponent.CCTV_MAP_COMPONENT, msg);
        policeMaps = MapViewComponent.getPolicesPost(MapViewComponent.POLICE_MAP_COMPONENT, msg);
        accidentMaps = MapViewComponent.getAccident(MapViewComponent.ACCIDENT_MAP_COMPONENT, msg);
        trafficMaps = MapViewComponent.getTraffic(MapViewComponent.TRAFFIC_MAP_COMPONENT, msg);
        disasterMaps = MapViewComponent.getDisaster(MapViewComponent.DISASTER_MAP_COMPONENT, msg);
        closureMaps = MapViewComponent.getClosure(MapViewComponent.CLOSURE_MAP_COMPONENT, msg);
        otherMaps = MapViewComponent.getOther(MapViewComponent.OTHER_MAP_COMPONENT, msg);
        trackers = MapViewComponent.getTrackers(MapViewComponent.TRACKER_MAP_COMPONENT, msg);
        transpostMaps = MapViewComponent.getTransPost(MapViewComponent.TRANSPORTATION_POST_MAP_COMPONENT, msg);

        userMarkers = new Marker[userMaps.length];
        cctvMarkers = new Marker[cctvMaps.length];
        policeMarkers = new Marker[policeMaps.length];
        accidentMarkers = new Marker[accidentMaps.length];
        trafficMarkers = new Marker[trafficMaps.length];
        disasterMarkers = new Marker[disasterMaps.length];
        closureMarkers = new Marker[closureMaps.length];
        otherMarkers = new Marker[otherMaps.length];
        trackerMarkers = new Marker[trackers.length];
        transpostMarkers = new Marker[transpostMaps.length];

        generateMarker(userMaps, userMarkers);
        generateMarker(cctvMaps, cctvMarkers);
        generateMarker(policeMaps, policeMarkers);
        generateMarker(accidentMaps, accidentMarkers);
        generateMarker(trafficMaps, trafficMarkers);
        generateMarker(disasterMaps, disasterMarkers);
        generateMarker(closureMaps, closureMarkers);
        generateMarker(otherMaps, otherMarkers);
        generateMarker(trackers, trackerMarkers);
        generateMarker(transpostMaps, transpostMarkers);

    }


    private void generateMarker(Object[] objects, Marker[] markers){
        for (int i = 0; i < objects.length; i ++){
            markers[i] = osmMarker.add(objects[i]);

            //markers[i].setIcon(descriptor);
        }
    }

    private CctvMap[] cctvMaps;
    private PoliceMap[] policeMaps;
    private AccidentMap[] accidentMaps;
    private TrafficMap[] trafficMaps;
    private DisasterMap[] disasterMaps;
    private ClosureMap[] closureMaps;
    private OtherMap[] otherMaps;
    private Tracker[] trackers;
    private TranspostMap[] transpostMaps;

    public boolean isReady() {
        return isReady;
    }

    private boolean isReady = false;

    public GeoPoint getMyLocationGeo() {
        return myLocationGeo;
    }

    public void setMyLocationGeo(String jsonStr) {
        try {
            JSONObject object = new JSONObject(jsonStr);
            myLocationGeo = new GeoPoint(object.getDouble(Constants.ENTITY_LATITUDE), object.getDouble(Constants.ENTITY_LONGITUDE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private GeoPoint myLocationGeo;

    public MapUtilities(MapView mapView){
        this.mapView = mapView;
        osmMarker = new OsmMarker(mapView);
    }

    public IMapController init(){
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(25);
        GeoPoint geoPoint = myLocationGeo;
        mapController.animateTo(geoPoint);
        isReady = true;
        return mapController;
    }


    public static BoundingBox computeArea(ArrayList<GeoPoint> points) {
        double nord = 0, sud = 0, ovest = 0, est = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) == null) continue;
            double lat = points.get(i).getLatitude();
            double lon = points.get(i).getLongitude();
            if ((i == 0) || (lat > nord)) nord = lat;
            if ((i == 0) || (lat < sud)) sud = lat;
            if ((i == 0) || (lon < ovest)) ovest = lon;
            if ((i == 0) || (lon > est)) est = lon;
        }

        return new BoundingBox(nord, est, sud, ovest);

    }

}
