package services;

import dataAccess.*;

public class ClearService {
    private final GameDAO gameData;
    private final UserDAO userData;
    private final AuthDAO authData;

    public void clearAll() {
        gameData.clearAll();
        userData.clearAll();
        authData.clearAll();

    }

    public ClearService(GameDAO gameData, UserDAO userData, AuthDAO authData) {
        this.gameData = gameData;
        this.userData = userData;
        this.authData = authData;
    }
}
