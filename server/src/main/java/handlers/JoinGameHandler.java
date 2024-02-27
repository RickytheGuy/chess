package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.JoinGameRequest;
import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private final JoinGameService joinGameService;
    private Gson serializer = new Gson();
    public JoinGameHandler(AuthDAO authData, GameDAO gameData) {
        this.joinGameService = new JoinGameService(authData, gameData);
    }

    public Object handleRequest(Request request, Response response) {
        String authToken = request.headers("Authorization");
        JoinGameRequest req =  serializer.fromJson(request.body(), JoinGameRequest.class);
        ChessResponse res = joinGameService.joinGame(req, authToken);
        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
