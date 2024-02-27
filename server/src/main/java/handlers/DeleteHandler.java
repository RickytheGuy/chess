package handlers;

import dataAccess.GameDAO;
import requests.DeleteRequest;
import requests.DeleteResponse;
import services.DeleteService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class DeleteHandler {
    private final DeleteService deleteService ;
    private final GameDAO gameData;
    private Gson serializer = new Gson();

    public DeleteHandler(GameDAO gameData){
        this.gameData = gameData;
        this.deleteService = new DeleteService(gameData);
    }
    public Object handleRequest(Request request, Response response) {
        try {
            deleteService.clearAll();
        } catch (Exception e) {
            return serializer.toJson(new DeleteResponse(500, e.toString()));
        }
        return serializer.toJson(new DeleteResponse(200, ""));
    }
}
