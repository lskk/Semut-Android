
package project.bsts.semut.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("CountryCode")
    @Expose
    private int countryCode;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Gender")
    @Expose
    private int gender;
    @SerializedName("Birthday")
    @Expose
    private String birthday;
    @SerializedName("Joindate")
    @Expose
    private String joindate;
    @SerializedName("Poin")
    @Expose
    private int poin;
    @SerializedName("PoinLevel")
    @Expose
    private int poinLevel;
    @SerializedName("AvatarID")
    @Expose
    private int avatarID;
    @SerializedName("Verified")
    @Expose
    private int verified;
    @SerializedName("Visibility")
    @Expose
    private int visibility;
    @SerializedName("Reputation")
    @Expose
    private int reputation;
    @SerializedName("deposit")
    @Expose
    private Object deposit;

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
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

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public int getPoinLevel() {
        return poinLevel;
    }

    public void setPoinLevel(int poinLevel) {
        this.poinLevel = poinLevel;
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public Object getDeposit() {
        return deposit;
    }

    public void setDeposit(Object deposit) {
        this.deposit = deposit;
    }

}
