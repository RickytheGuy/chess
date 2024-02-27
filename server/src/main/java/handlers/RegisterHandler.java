package handlers;

import dataAccess.*;
import requests.*;
import services.RegisterService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class RegisterHandler {

    private final RegisterService registerService;
    private Gson serializer = new Gson();
    public RegisterHandler(UserDAO userData, AuthDAO authData) {
        registerService = new RegisterService(userData, authData);
    }
    public Object handleRequest(Request request, Response response) {
        RegisterRequest req =  serializer.fromJson(request.body(), RegisterRequest.class);
        ChessResponse res = registerService.register(req);
        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
        }
        return serializer.toJson(res);

    }
}
