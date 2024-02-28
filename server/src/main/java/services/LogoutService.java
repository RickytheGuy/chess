package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.LogoutRequest;
import requests.LogoutResponse;

public class LogoutService {
    private final AuthDAO authData;

    public LogoutService(AuthDAO authData) {
        this.authData = authData;
    }

    public ChessResponse logout(LogoutRequest req) {
        if (req.authToken() == null) {
            return new ErrorResponse(400, "Error: bad request");
        }
        try {
            authData.removeAuth(req.authToken());
        } catch (DataAccessException e) {
            return new ErrorResponse(401, "Error: unauthorized");
        }
        return new LogoutResponse("");
    }
}
