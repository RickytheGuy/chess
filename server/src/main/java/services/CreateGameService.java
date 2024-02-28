package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.CreateGameRequest;
import requests.CreateGameResponse;

public class CreateGameService {
    private final AuthDAO authData;
    private final GameDAO gameData;

    public CreateGameService(AuthDAO authData, GameDAO gameData) {
        this.authData = authData;
        this.gameData = gameData;
    }

    public ChessResponse createGame(CreateGameRequest req, String authToken) {
        try {
            authData.getUserFromAuth(authToken);
        } catch (DataAccessException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        int gameID;
        try {
            gameID = gameData.addGame(req.gameName());
        } catch (DataAccessException e) {
            return new ErrorResponse(400, "Error: bad request");
        }
        return new CreateGameResponse(gameID);
    }
}
