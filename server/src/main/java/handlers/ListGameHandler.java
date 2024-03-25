package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import requests.*;
import services.ListGameService;
import spark.Request;
import spark.Response;

public class ListGameHandler {
    private final ListGameService listGameService;
    private final Gson serializer = new Gson();
    public ListGameHandler(AuthDAO authData, GameDAO gameData) {
        listGameService = new ListGameService(authData, gameData);
    }
    public Object handleRequest(Request request, Response response) {
        String authToken = request.headers("Authorization");
        ListGameRequest req =  serializer.fromJson(request.body(), ListGameRequest.class);
        ChessResponse res = listGameService.listGames(req, authToken);

        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
