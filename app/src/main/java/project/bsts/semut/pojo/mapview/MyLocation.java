package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyLocation {

    @SerializedName("my.latitude")
    @Expose
    private Double myLatitude;
    @SerializedName("my.longitude")
    @Expose
    private Double myLongitude;

    public Double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(Double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public Double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(Double myLongitude) {
        this.myLongitude = myLongitude;
    }

}