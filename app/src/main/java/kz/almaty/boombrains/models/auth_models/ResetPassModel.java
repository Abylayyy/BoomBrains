package kz.almaty.boombrains.models.auth_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResetPassModel {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private List<ErrorModel> errors;

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
