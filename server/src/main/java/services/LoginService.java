package services;

import dataAccess.*;
import requests.*;

public class LoginService {
    private final UserDAO userData;
    private final AuthDAO authData;

    public LoginService(UserDAO userData, AuthDAO authData) {
        this.userData = userData;
        this.authData = authData;
    }


    public ChessResponse login(LoginRequest req)  {
        String username = req.username();
        try {
            if (!userData.getUser(username).password().equals(req.password())) {
                return new ErrorResponse(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        } catch (NullPointerException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        }

        authData.addAuth(username);
        String token;
        try {
            token = authData.getAuth(username);
        } catch (DataAccessException e) {
            return new ErrorResponse(500, "Error: Somehow auth token was not added when logging in.");
        }

        return new LoginResponse(username, token);


    }
}
