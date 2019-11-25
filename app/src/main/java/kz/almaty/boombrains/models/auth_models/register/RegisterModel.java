package kz.almaty.boombrains.models.auth_models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterModel {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("localRecords")
    @Expose
    private LocalRecords localRecords;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalRecords getLocalRecords() {
        return localRecords;
    }

    public void setLocalRecords(LocalRecords localRecords) {
        this.localRecords = localRecords;
    }
}
