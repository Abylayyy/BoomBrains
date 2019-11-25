package kz.almaty.boombrains.models.profile_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kz.almaty.boombrains.models.auth_models.ErrorModel;

public class ProfileRatingModel {
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("star")
    @Expose
    private Integer star;
    @SerializedName("achievements")
    @Expose
    private List<Achievement> achievements = null;
    @SerializedName("worldRecords")
    @Expose
    private List<ProfileWorldRecord> worldRecords = null;
    @SerializedName("myWorldRecord")
    @Expose
    private ProfileWorldRecord myWorldRecord;
    @SerializedName("friendRecords")
    @Expose
    private List<ProfileWorldRecord> friendRecords = null;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errors")
    @Expose
    private List<ErrorModel> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public List<ProfileWorldRecord> getWorldRecords() {
        return worldRecords;
    }

    public void setWorldRecords(List<ProfileWorldRecord> worldRecords) {
        this.worldRecords = worldRecords;
    }

    public ProfileWorldRecord getMyWorldRecord() {
        return myWorldRecord;
    }

    public void setMyWorldRecord(ProfileWorldRecord myWorldRecord) {
        this.myWorldRecord = myWorldRecord;
    }

    public List<ProfileWorldRecord> getFriendRecords() {
        return friendRecords;
    }

    public void setFriendRecords(List<ProfileWorldRecord> friendRecords) {
        this.friendRecords = friendRecords;
    }

}
