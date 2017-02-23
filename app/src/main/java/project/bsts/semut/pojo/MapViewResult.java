package project.bsts.semut.pojo;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Users",
        "CCTV",
        "Polices",
        "Accidents",
        "Traffics",
        "Disasters",
        "Closures",
        "Other",
        "Stasiun",
        "Tracker"
})
public class MapViewResult {

    public static int USERS = 0;
    public static int CCTV = 1;
    public static int POLICE = 2;
    public static int ACCIDENTS = 3;
    public static int TRAFFICS = 4;
    public static int DISASTERS = 5;
    public static int CLOSURES = 6;
    public static int OTHER = 7;
    public static int STASIUN = 8;
    public static int TRACKER = 9;

    @JsonProperty("Users")
    private String users;
    @JsonProperty("CCTV")
    private String cCTV;
    @JsonProperty("Polices")
    private String polices;
    @JsonProperty("Accidents")
    private String accidents;
    @JsonProperty("Traffics")
    private String traffics;
    @JsonProperty("Disasters")
    private String disasters;
    @JsonProperty("Closures")
    private String closures;
    @JsonProperty("Other")
    private String other;
    @JsonProperty("Stasiun")
    private String stasiun;
    @JsonProperty("Tracker")
    private String tracker;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Users")
    public String getUsers() {
        return users;
    }

    @JsonProperty("Users")
    public void setUsers(String users) {
        this.users = users;
    }

    @JsonProperty("CCTV")
    public String getCCTV() {
        return cCTV;
    }

    @JsonProperty("CCTV")
    public void setCCTV(String cCTV) {
        this.cCTV = cCTV;
    }

    @JsonProperty("Polices")
    public String getPolices() {
        return polices;
    }

    @JsonProperty("Polices")
    public void setPolices(String polices) {
        this.polices = polices;
    }

    @JsonProperty("Accidents")
    public String getAccidents() {
        return accidents;
    }

    @JsonProperty("Accidents")
    public void setAccidents(String accidents) {
        this.accidents = accidents;
    }

    @JsonProperty("Traffics")
    public String getTraffics() {
        return traffics;
    }

    @JsonProperty("Traffics")
    public void setTraffics(String traffics) {
        this.traffics = traffics;
    }

    @JsonProperty("Disasters")
    public String getDisasters() {
        return disasters;
    }

    @JsonProperty("Disasters")
    public void setDisasters(String disasters) {
        this.disasters = disasters;
    }

    @JsonProperty("Closures")
    public String getClosures() {
        return closures;
    }

    @JsonProperty("Closures")
    public void setClosures(String closures) {
        this.closures = closures;
    }

    @JsonProperty("Other")
    public String getOther() {
        return other;
    }

    @JsonProperty("Other")
    public void setOther(String other) {
        this.other = other;
    }

    @JsonProperty("Stasiun")
    public String getStasiun() {
        return stasiun;
    }

    @JsonProperty("Stasiun")
    public void setStasiun(String stasiun) {
        this.stasiun = stasiun;
    }

    @JsonProperty("Tracker")
    public String getTracker() {
        return tracker;
    }

    @JsonProperty("Tracker")
    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}