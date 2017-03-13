package project.bsts.semut.map.osm;


import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import project.bsts.semut.setup.Constants;

public class MapUtilities {

    MapView mapView;
    private IMapController mapController;

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


}
