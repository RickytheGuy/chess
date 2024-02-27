package services;

import com.google.gson.Gson;
import dataAccess.*;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.RegisterRequest;
import requests.RegisterResponse;
import spark.Request;
import spark.Response;

public class RegisterService {
    private final UserDAO userData;
    private final AuthDAO authData;

    public RegisterService(UserDAO userData, AuthDAO authData) {
        this.userData = userData;
        this.authData = authData;
    }
    public ChessResponse register(RegisterRequest req) {
        if (req.username() == null || req.password() ==null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        try {
            if (userData.getUser(req.username()) != null) {
                return new ErrorResponse(403, "Error: User already exists");
            }
        } catch (DataAccessException e) {
            return new ErrorResponse(403, "Error: User already exists");
        }


        try {
            userData.addUser(req.username(), req.password(), req.email());
        } catch (DataAccessException e) {
            return new ErrorResponse(500, "Error: Internal server error");
        }
        authData.addAuth(req.username());
        String token;
        try {
            token = authData.getAuth(req.username());
        } catch (DataAccessException e) {
            return new ErrorResponse(500, "Error: Internal server error");
        }
        return new RegisterResponse(req.username(), token);
    }
}
