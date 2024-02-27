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

        try {
           RegisterResponse res = registerService.register(req);
           return serializer.toJson(res);
        } catch (DataAccessException e) {
            return serializer.toJson(new ErrorResponse(500, e.toString()));
        } catch (JsonIOException e) {
            return serializer.toJson(new ErrorResponse(400, "Error: bad request"));
        }
    }
}
