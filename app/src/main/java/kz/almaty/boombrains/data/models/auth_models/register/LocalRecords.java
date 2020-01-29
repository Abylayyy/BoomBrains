package kz.almaty.boombrains.data.models.auth_models.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalRecords {
    @SerializedName("shulteTable")
    @Expose
    private Integer shulteTable;

    @SerializedName("findNumber")
    @Expose
    private Integer findNumbers;

    @SerializedName("rememberNumber")
    @Expose
    private Integer rememberNum;

    @SerializedName("calculation")
    @Expose
    private Integer calculation;

    @SerializedName("equation")
    @Expose
    private Integer equation;

    @SerializedName("shulteLetters")
    @Expose
    private Integer shulteLetters;

    @SerializedName("rememberWords")
    @Expose
    private Integer rememberWords;

    @SerializedName("memorySquare")
    @Expose
    private Integer memorySquare;

    @SerializedName("coloredFigures")
    @Expose
    private Integer colorFigures;

    @SerializedName("coloredWords")
    @Expose
    private Integer colorWords;

    public Integer getShulteTable() {
        return shulteTable;
    }

    public void setShulteTable(Integer shulteTable) {
        this.shulteTable = shulteTable;
    }

    public Integer getFindNumbers() {
        return findNumbers;
    }

    public void setFindNumbers(Integer findNumbers) {
        this.findNumbers = findNumbers;
    }

    public Integer getRememberNum() {
        return rememberNum;
    }

    public void setRememberNum(Integer rememberNum) {
        this.rememberNum = rememberNum;
    }

    public Integer getCalculation() {
        return calculation;
    }

    public void setCalculation(Integer calculation) {
        this.calculation = calculation;
    }

    public Integer getEquation() {
        return equation;
    }

    public void setEquation(Integer equation) {
        this.equation = equation;
    }

    public Integer getShulteLetters() {
        return shulteLetters;
    }

    public void setShulteLetters(Integer shulteLetters) {
        this.shulteLetters = shulteLetters;
    }

    public Integer getRememberWords() {
        return rememberWords;
    }

    public void setRememberWords(Integer rememberWords) {
        this.rememberWords = rememberWords;
    }

    public Integer getMemorySquare() {
        return memorySquare;
    }

    public void setMemorySquare(Integer memorySquare) {
        this.memorySquare = memorySquare;
    }

    public Integer getColorFigures() {
        return colorFigures;
    }

    public void setColorFigures(Integer colorFigures) {
        this.colorFigures = colorFigures;
    }

    public Integer getColorWords() {
        return colorWords;
    }

    public void setColorWords(Integer colorWords) {
        this.colorWords = colorWords;
    }
}
