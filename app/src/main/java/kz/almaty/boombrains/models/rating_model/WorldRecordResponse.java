package kz.almaty.boombrains.models.rating_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorldRecordResponse {
    @SerializedName("game")
    @Expose
    private String game;
    @SerializedName("record")
    @Expose
    private Integer record;
    @SerializedName("worldRecords")
    @Expose
    private List<WorldRecord> worldRecords = null;
    @SerializedName("friendRecords")
    @Expose
    private List<WorldRecord> friendRecords = null;

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

    public List<WorldRecord> getWorldRecords() {
        return worldRecords;
    }

    public void setWorldRecords(List<WorldRecord> worldRecords) {
        this.worldRecords = worldRecords;
    }

    public List<WorldRecord> getFriendRecords() {
        return friendRecords;
    }

    public void setFriendRecords(List<WorldRecord> friendRecords) {
        this.friendRecords = friendRecords;
    }
}
