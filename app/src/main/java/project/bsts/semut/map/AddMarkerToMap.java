package project.bsts.semut.map;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.bsts.semut.pojo.mapview.AccidentMap;
import project.bsts.semut.pojo.mapview.CctvMap;
import project.bsts.semut.pojo.mapview.DisasterMap;
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
                                    ((UserMap) objectMap).getLastLocation().getLongitude()))
                            .title(((UserMap) objectMap).getEmail()));
        }else if(objectMap instanceof  CctvMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((CctvMap) objectMap).getLatitude(),
                                    ((CctvMap) objectMap).getLongitude()))
                            .title(((CctvMap) objectMap).getName()));
        }else if(objectMap instanceof PoliceMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((PoliceMap) objectMap).getLatitude(),
                                    ((PoliceMap) objectMap).getLongitude()))
                            .title(((PoliceMap) objectMap).getDescription()));
        }else if(objectMap instanceof AccidentMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((AccidentMap) objectMap).getLatitude(),
                                    ((AccidentMap) objectMap).getLongitude()))
                            .title(((AccidentMap) objectMap).getDescription()));
        }else if(objectMap instanceof TrafficMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((TrafficMap) objectMap).getLatitude(),
                                    ((TrafficMap) objectMap).getLongitude()))
                            .title(((TrafficMap) objectMap).getDescription()));
        }else if(objectMap instanceof DisasterMap){
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(((DisasterMap) objectMap).getLatitude(),
                                    ((DisasterMap) objectMap).getLongitude()))
                            .title(((DisasterMap) objectMap).getDescription()));
        }

        return marker;
    }
}
