package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {

    @SerializedName("RelationID")
    @Expose
    private Integer relationID;
    @SerializedName("IsRequest")
    @Expose
    private Boolean isRequest;
    @SerializedName("Status")
    @Expose
    private String status;

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public Boolean getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(Boolean isRequest) {
        this.isRequest = isRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}