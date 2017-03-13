package project.bsts.semut.map;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.ClosureMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
import project.bsts.semut.pojo.mapview.OtherMap;
import project.bsts.semut.pojo.mapview.PoliceMap;
import project.bsts.semut.pojo.mapview.TrafficMap;
import project.bsts.semut.pojo.mapview.UserMap;

public class AddMarkerToMap {

    private GoogleMap googleMap;

    public AddMarkerToMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public Marker add(Object objectMap){
        Marker marker = null;
        if(objectMap instanceof  UserMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((UserMap) objectMap).getLastLocation().getLatitude(),
                                    ((UserMap) objectMap).getLastLocation().getLongitude())));
        }else if(objectMap instanceof  CctvMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((CctvMap) objectMap).getLatitude(),
                                    ((CctvMap) objectMap).getLongitude())));
        }else if(objectMap instanceof PoliceMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((PoliceMap) objectMap).getLatitude(),
                                    ((PoliceMap) objectMap).getLongitude())));
        }else if(objectMap instanceof AccidentMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((AccidentMap) objectMap).getLatitude(),
                                    ((AccidentMap) objectMap).getLongitude())));
        }else if(objectMap instanceof TrafficMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((TrafficMap) objectMap).getLatitude(),
                                    ((TrafficMap) objectMap).getLongitude())));
        }else if(objectMap instanceof DisasterMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((DisasterMap) objectMap).getLatitude(),
                                    ((DisasterMap) objectMap).getLongitude())));
        }else if(objectMap instanceof ClosureMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((ClosureMap) objectMap).getLatitude(),
                                    ((ClosureMap) objectMap).getLongitude())));
        }else if(objectMap instanceof OtherMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((OtherMap) objectMap).getLatitude(),
                                    ((OtherMap) objectMap).getLongitude())));
        }

        return marker;
    }
}
