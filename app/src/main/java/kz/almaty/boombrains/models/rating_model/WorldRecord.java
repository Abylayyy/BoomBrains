package kz.almaty.boombrains.models.rating_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorldRecord {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("record")
    @Expose
    private Integer record;
    @SerializedName("username")
    @Expose
    private String username;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}