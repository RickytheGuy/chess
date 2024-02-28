package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.LogoutRequest;
import services.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private final LogoutService logoutService;
    private final Gson serializer = new Gson();
    public LogoutHandler(AuthDAO authData) {
        logoutService = new LogoutService(authData);
    }
    public Object handleRequest(Request request, Response response) {
        LogoutRequest req = new LogoutRequest(request.headers("Authorization"));
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
