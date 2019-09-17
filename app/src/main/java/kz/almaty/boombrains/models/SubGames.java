package kz.almaty.boombrains.models;

public class SubGames {
    private String name, record;
    private int image, in, out;

    public SubGames(String name, String record, int image, int in, int out) {
        this.name = name;
        this.record = record;
        this.image = image;
        this.in = in;
        this.out = out;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    public String getRecord() {
        return record;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }
}
