package project.bsts.semut.pojo.mapview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMap {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("CountryCode")
    @Expose
    private Object countryCode;
    @SerializedName("PhoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("Gender")
    @Expose
    private Integer gender;
    @SerializedName("Birthday")
    @Expose
    private Object birthday;
    @SerializedName("Joindate")
    @Expose
    private String joindate;
    @SerializedName("Poin")
    @Expose
    private Integer poin;
    @SerializedName("PoinLevel")
    @Expose
    private Integer poinLevel;
    @SerializedName("AvatarID")
    @Expose
    private Integer avatarID;
    @SerializedName("Verified")
    @Expose
    private Integer verified;
    @SerializedName("Visibility")
    @Expose
    private Integer visibility;
    @SerializedName("Reputation")
    @Expose
    private Integer reputation;
    @SerializedName("deposit")
    @Expose
    private Object deposit;
    @SerializedName("LastLocation")
    @Expose
    private LastLocation lastLocation;
    @SerializedName("Friend")
    @Expose
    private Boolean friend;
    @SerializedName("Relation")
    @Expose
    private Relation relation;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Object countryCode) {
        this.countryCode = countryCode;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public Integer getPoin() {
        return poin;
    }

    public void setPoin(Integer poin) {
        this.poin = poin;
    }

    public Integer getPoinLevel() {
        return poinLevel;
    }

    public void setPoinLevel(Integer poinLevel) {
        this.poinLevel = poinLevel;
    }

    public Integer getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(Integer avatarID) {
        this.avatarID = avatarID;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Object getDeposit() {
        return deposit;
    }

    public void setDeposit(Object deposit) {
        this.deposit = deposit;
    }

    public LastLocation getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LastLocation lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Boolean getFriend() {
        return friend;
    }

    public void setFriend(Boolean friend) {
        this.friend = friend;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

}