package kz.almaty.boombrains.models;

import java.util.List;

public class GameTypes {
    private String type;
    private List<SubGames> gamesList;

    public GameTypes(String type, List<SubGames> gamesList) {
        this.type = type;
        this.gamesList = gamesList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SubGames> getGamesList() {
        return gamesList;
    }

    public void setGamesList(List<SubGames> gamesList) {
        this.gamesList = gamesList;
    }
}
