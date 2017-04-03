package project.bsts.semut.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import project.bsts.semut.pojo.mapview.CctvMap;

public class CityCctv {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ProvinceID")
    @Expose
    private Integer provinceID;
    @SerializedName("cctv")
    @Expose
    private ArrayList<CctvMap> cctv = null;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(Integer provinceID) {
        this.provinceID = provinceID;
    }

    public ArrayList<CctvMap> getCctv() {
        return cctv;
    }

    public void setCctv(ArrayList<CctvMap> cctv) {
        this.cctv = cctv;
    }

}