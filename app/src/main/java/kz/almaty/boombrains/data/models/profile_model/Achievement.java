package kz.almaty.boombrains.data.models.profile_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Achievement {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("translate")
    @Expose
    private String translate;
    @SerializedName("verity")
    @Expose
    private Boolean verity;

    private Integer resource;

    public Integer getResource() {
        return resource;
    }

    public void setResource(Integer resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public Boolean getVerity() {
        return verity;
    }

    public void setVerity(Boolean verity) {
        this.verity = verity;
    }
}
