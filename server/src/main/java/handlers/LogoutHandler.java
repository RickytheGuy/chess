package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.LogoutRequest;
import requests.RegisterRequest;
import services.LogoutService;
import services.RegisterService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private final LogoutService logoutService;
    private Gson serializer = new Gson();
    public LogoutHandler(AuthDAO authData) {
        logoutService = new LogoutService(authData);
    }
    public Object handleRequest(Request request, Response response) {
        LogoutRequest req =  serializer.fromJson(request.body(), LogoutRequest.class);
        ChessResponse res = logoutService.logout(req);
        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
