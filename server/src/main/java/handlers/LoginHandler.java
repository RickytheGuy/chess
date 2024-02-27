package handlers;

import com.google.gson.Gson;
import dataAccess.GameDAO;
import requests.DeleteResponse;
import services.DeleteService;
import services.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private final LoginService loginService ;
    private final GameDAO gameData;
    private Gson serializer = new Gson();

    public LoginHandler(GameDAO gameData){
        this.gameData = gameData;
        this.loginService = new LoginService(gameData);
    }
    public Object handleRequest(Request request, Response response) {
        return response;
//        try {
//            loginService.clearAll();
//        } catch (Exception e) {
//            return serializer.toJson(new LoginResponse(500, e.toString()));
//        }
//        return serializer.toJson(new LoginResponse(200, ""));
    }
}
