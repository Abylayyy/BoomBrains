package kz.almaty.boombrains.models.game_models;

public class FigureModel {
    private Integer tint, resource;
    private boolean selected;

    public FigureModel(Integer tint, Integer resource) {
        this.tint = tint;
        this.resource = resource;
    }

    public FigureModel() {

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getTint() {
        return tint;
    }

    public void setTint(Integer tint) {
        this.tint = tint;
    }

    public Integer getResource() {
        return resource;
    }

    public void setResource(Integer resource) {
        this.resource = resource;
    }
}
