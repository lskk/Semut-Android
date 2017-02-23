package project.bsts.semut.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.bsts.semut.pojo.mapview.CctvMap;
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
        }

        return marker;
    }
}
