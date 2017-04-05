package project.bsts.semut.pojo.mapview;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracker {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Mac")
    @Expose
    private String mac;
    @SerializedName("Speed")
    @Expose
    private Double speed;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Data")
    @Expose
    private List<Double> data = null;
    @SerializedName("Lokasi")
    @Expose
    private String lokasi;
    @SerializedName("Keterangan")
    @Expose
    private String keterangan;
    @SerializedName("Type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}