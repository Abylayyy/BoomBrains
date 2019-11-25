package kz.almaty.boombrains.models.add_friend_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestListModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("requesterId")
    @Expose
    private String requesterId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
