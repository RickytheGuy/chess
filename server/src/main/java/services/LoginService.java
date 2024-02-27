package services;

import dataAccess.GameDAO;

public class LoginService {
    private final GameDAO gameData;

    public LoginService(GameDAO gameData) {
        this.gameData = gameData;
    }
}
