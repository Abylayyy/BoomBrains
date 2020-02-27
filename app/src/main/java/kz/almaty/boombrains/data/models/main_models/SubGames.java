package kz.almaty.boombrains.data.models.main_models;

public class SubGames {
    private String record;
    private int image, in, out, name;

    public SubGames(int name, String record, int image, int in, int out) {
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

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }
}
