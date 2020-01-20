package kz.almaty.boombrains.models.profile_model;

public class PotionModel {
    private String name;
    private Integer image;
    private boolean enabled;

    public PotionModel(String name, Integer image, boolean enabled) {
        this.name = name;
        this.image = image;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
