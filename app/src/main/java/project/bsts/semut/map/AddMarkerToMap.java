package project.bsts.semut.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import project.bsts.semut.pojo.mapview.UserMap;

public class AddMarkerToMap {

    private GoogleMap googleMap;

    public AddMarkerToMap(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public Marker add(UserMap userMap){
        Marker marker = googleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(userMap.getLastLocation().getLatitude(), userMap.getLastLocation().getLongitude()))
                        .title(userMap.getEmail()));

        return marker;
    }
}
