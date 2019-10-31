package kz.almaty.boombrains.models.game_models;

public class SquareModel {

    private String number;
    private boolean isSelected;

    public String getNumber() {
        return number;
    }

    public SquareModel(String number) {
        this.number = number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
