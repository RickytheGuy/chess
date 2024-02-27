package handlers;

import dataAccess.*;
import requests.DeleteRequest;
import requests.DeleteResponse;
import requests.ErrorResponse;
import services.DeleteService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class DeleteHandler {
    private final DeleteService deleteService ;
    private Gson serializer = new Gson();

    public DeleteHandler(GameDAO gameData, UserDAO userData, AuthDAO authData){
        this.deleteService = new DeleteService(gameData, userData, authData);
    }
    public Object handleRequest(Request request, Response response) {
        try {
            deleteService.clearAll();
        } catch (Exception e) {
            return serializer.toJson(new ErrorResponse(500, e.toString()));
        }
        return serializer.toJson(new DeleteResponse(200, ""));
    }
}
