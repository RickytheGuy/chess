package handlers;

import dataAccess.*;
import requests.DeleteResponse;
import services.ClearService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class ClearHandler {
    private final ClearService clearService;
    private final Gson serializer = new Gson();

    public ClearHandler(GameDAO gameData, UserDAO userData, AuthDAO authData){
        this.clearService = new ClearService(gameData, userData, authData);
    }
    public Object handleRequest(Request request, Response response) {
            clearService.clearAll();
            return serializer.toJson(new DeleteResponse(200, ""));
    }
}
