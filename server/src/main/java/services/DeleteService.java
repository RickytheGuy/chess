package services;

import dataAccess.GameDAO;

public class DeleteService {
    private final GameDAO gameData;

    public void clearAll() {
        gameData.clearAll();
    }

    public DeleteService(GameDAO gameData) {
        this.gameData = gameData;
    }
}
