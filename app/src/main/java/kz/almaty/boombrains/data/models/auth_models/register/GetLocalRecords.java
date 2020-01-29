package kz.almaty.boombrains.data.models.auth_models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLocalRecords {
    @SerializedName("localRecords")
    @Expose
    private LocalRecords localRecords;

    public LocalRecords getLocalRecords() {
        return localRecords;
    }

    public void setLocalRecords(LocalRecords localRecords) {
        this.localRecords = localRecords;
    }
}
