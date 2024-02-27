package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.JoinGameRequest;
import requests.JoinGameResponse;

public class JoinGameService {
    private final AuthDAO authData;
    private final GameDAO gameData;

    public JoinGameService(AuthDAO authData, GameDAO gameData) {
        this.authData = authData;
        this.gameData = gameData;
    }

    public ChessResponse joinGame(JoinGameRequest req, String authToken) {
        String username;
        int gameID;
        try {
            username = authData.getUserFromAuth(authToken);
        } catch (DataAccessException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        if (!gameData.gameExists(req.gameID())) {
            return new ErrorResponse(400, "Error: bad request");
        }

        try {
            gameData.addPlayerToGame(req.gameID(), username, req.playerColor());
        } catch (DataAccessException e) {
            return new ErrorResponse(403, e.getMessage());
        }
        return new JoinGameResponse();
    }
}
