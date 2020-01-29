package kz.almaty.boombrains.data.models.profile_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileWorldRecord {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("totalRecord")
    @Expose
    private Integer totalRecord;
    @SerializedName("position")
    @Expose
    private Integer position;
    private boolean isMe;

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
