package kz.almaty.boombrains.models.game_models;

public class ColorModel {
    private Integer color;
    private String name;

    public ColorModel(Integer color, String name) {
        this.color = color;
        this.name = name;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
