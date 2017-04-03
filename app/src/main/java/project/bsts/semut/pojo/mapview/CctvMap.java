package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CctvMap implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("ItemID")
    @Expose
    private String itemID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Stream")
    @Expose
    private String stream;
    @SerializedName("urlVideo")
    @Expose
    private String urlVideo;
    @SerializedName("urlImage")
    @Expose
    private String urlImage;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("location")
    @Expose
    private CCTVLocation location;
    @SerializedName("City")
    @Expose
    private String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public CCTVLocation getLocation() {
        return location;
    }

    public void setLocation(CCTVLocation location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}