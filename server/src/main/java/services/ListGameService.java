package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import requests.*;

public class ListGameService {
    private final AuthDAO authData;
    private final GameDAO gameData;
    public ListGameService(AuthDAO authData, GameDAO gameData) {
        this.authData = authData;
        this.gameData = gameData;
    }

    public ChessResponse listGames(ListGameRequest req, String authToken) {
        try {
            authData.getUserFromAuth(authToken);
        } catch (DataAccessException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        return new ListGameResponse(gameData.listGames());
    }
}
