package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastLocation {

    @SerializedName("Timespan")
    @Expose
    private String timespan;
    @SerializedName("Altitude")
    @Expose
    private Double altitude;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Speed")
    @Expose
    private Double speed;

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}