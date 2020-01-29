package kz.almaty.boombrains.data.models.records_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyRecordsModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("records")
    @Expose
    private List<Record> records = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
