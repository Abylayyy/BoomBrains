package kz.almaty.boombrains.data.models.profile_model;

import android.content.Intent;

public class PotionModel {
    private String name;
    private String desc;
    private Integer image;
    private Integer cost;

    public PotionModel(String name, Integer image, String desc, Integer cost) {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.cost = cost;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
