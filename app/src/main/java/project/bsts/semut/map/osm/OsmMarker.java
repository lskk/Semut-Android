package project.bsts.semut.map.osm;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import project.bsts.semut.R;
import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.MyLocation;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.UserMap;

public class OsmMarker {

    private MapView mapView;

    public OsmMarker(MapView mapView){
        this.mapView = mapView;
    }

    public Marker add(Object objectMap){
        Marker marker = null;
        if(objectMap instanceof UserMap){
            GeoPoint point = new GeoPoint(((UserMap) objectMap).getLastLocation().getLatitude(), ((UserMap) objectMap).getLastLocation().getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof CctvMap){
            GeoPoint point = new GeoPoint(((CctvMap) objectMap).getLatitude(), ((CctvMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.cctv_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof PoliceMap){
            GeoPoint point = new GeoPoint(((PoliceMap) objectMap).getLatitude(), ((PoliceMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.police_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof AccidentMap){
            GeoPoint point = new GeoPoint(((AccidentMap) objectMap).getLatitude(), ((AccidentMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.accident_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof TrafficMap){
            GeoPoint point = new GeoPoint(((TrafficMap) objectMap).getLatitude(), ((TrafficMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.traffic_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof DisasterMap){
            GeoPoint point = new GeoPoint(((DisasterMap) objectMap).getLatitude(), ((DisasterMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.disaster_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof ClosureMap){
            GeoPoint point = new GeoPoint(((ClosureMap) objectMap).getLatitude(), ((ClosureMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.closure_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof OtherMap){
            GeoPoint point = new GeoPoint(((OtherMap) objectMap).getLatitude(), ((OtherMap) objectMap).getLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.other_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }else if(objectMap instanceof MyLocation){
            GeoPoint point = new GeoPoint(((MyLocation) objectMap).getMyLatitude(), ((MyLocation) objectMap).getMyLongitude());
            marker = new Marker(mapView);
            marker.setPosition(point);
        //    marker.setIcon(mapView.getContext().getResources().getDrawable(R.drawable.other_icon));
            marker.setRelatedObject(objectMap);
            mapView.getOverlays().add(marker);
        }

        return marker;
    }
}
