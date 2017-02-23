package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastLocation {

    @SerializedName("Timespan")
    @Expose
    private String timespan;
    @SerializedName("Altitude")
    @Expose
    private Integer altitude;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Speed")
    @Expose
    private Integer speed;

    public String getTimespan() {
        return timespan;
    }

    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
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

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

}