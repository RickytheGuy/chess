package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.CreateGameRequest;
import services.*;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private final CreateGameService createGameService;
    private final Gson serializer = new Gson();
    public CreateGameHandler(AuthDAO authData, GameDAO gameData){
        this.createGameService = new CreateGameService(authData, gameData);
    }

    public Object handleRequest(Request request, Response response) {
        String authToken = request.headers("Authorization");
        CreateGameRequest req =  serializer.fromJson(request.body(), CreateGameRequest.class);
        ChessResponse res = createGameService.createGame(req, authToken);
        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
