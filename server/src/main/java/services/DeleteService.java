package services;

import dataAccess.*;

public class DeleteService {
    private final GameDAO gameData;
    private final UserDAO userData;
    private final AuthDAO authData;

    public void clearAll() {
        try {
            gameData.clearAll();
            userData.clearAll();
            authData.clearAll();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

    }

    public DeleteService(GameDAO gameData, UserDAO userData, AuthDAO authData) {
        this.gameData = gameData;
        this.userData = userData;
        this.authData = authData;
    }
}
