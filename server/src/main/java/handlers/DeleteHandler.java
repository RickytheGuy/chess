package handlers;

import dataAccess.GameDAO;
import spark.Request;
import spark.Response;

public class DeleteHandler {
    private final GameDAO gameData;
    public DeleteHandler(GameDAO gameData){
        this.gameData = gameData;
    }
    public Object handleRequest(Request request, Response response) {
        int x = 2+2;
        return response;
    }
}
