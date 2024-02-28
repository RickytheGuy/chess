package handlers;

import com.google.gson.Gson;
import dataAccess.*;
import requests.*;
import services.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private final LoginService loginService ;
    private final Gson serializer = new Gson();

    public LoginHandler(UserDAO userData, AuthDAO authData){
        this.loginService = new LoginService(userData, authData);
    }
    public Object handleRequest(Request request, Response response) {
        LoginRequest req =  serializer.fromJson(request.body(), LoginRequest.class);
        ChessResponse res = loginService.login(req);
        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
