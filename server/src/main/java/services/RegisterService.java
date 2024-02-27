package services;

import com.google.gson.Gson;
import dataAccess.*;
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
    public RegisterResponse register(RegisterRequest req) throws DataAccessException {

        if (userData.getUser(req.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        userData.addUser(req.username(), req.password(), req.email());
        authData.addAuth(req.username());
        return new RegisterResponse(req.username(), authData.getAuth(req.username()));
    }
}
