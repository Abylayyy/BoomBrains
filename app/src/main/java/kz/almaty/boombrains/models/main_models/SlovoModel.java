package kz.almaty.boombrains.models.main_models;

public class SlovoModel {

    private String text;
    private boolean isSelected;

    public SlovoModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
