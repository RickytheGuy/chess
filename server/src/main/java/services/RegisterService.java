package services;

import dataAccess.*;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.RegisterRequest;
import requests.RegisterResponse;


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
        if (userData.getUser(req.username()) != null) {
            return new ErrorResponse(403, "Error: User already exists");
        }


        userData.addUser(req.username(), req.password(), req.email());
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
