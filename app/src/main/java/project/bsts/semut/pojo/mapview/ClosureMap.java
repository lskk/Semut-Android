package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClosureMap {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("IDtype")
    @Expose
    private Integer iDtype;
    @SerializedName("IDsub")
    @Expose
    private Integer iDsub;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("SubType")
    @Expose
    private String subType;
    @SerializedName("Times")
    @Expose
    private String times;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("Exp")
    @Expose
    private String exp;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("PostedBy")
    @Expose
    private PostedBy postedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIDtype() {
        return iDtype;
    }

    public void setIDtype(Integer iDtype) {
        this.iDtype = iDtype;
    }

    public Integer getIDsub() {
        return iDsub;
    }

    public void setIDsub(Integer iDsub) {
        this.iDsub = iDsub;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PostedBy getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PostedBy postedBy) {
        this.postedBy = postedBy;
    }

}