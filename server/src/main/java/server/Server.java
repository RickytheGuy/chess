package server;

import spark.*;
import handlers.*;
import dataAccess.*;

public class Server {
    private GameDAO gameData;
    private UserDAO userData;
    private AuthDAO authData;

    public Server() {
        SqlDAO db = new SqlDAO();
        gameData = db;
        userData = db;
        authData = db;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(gameData, userData, authData)::handleRequest);
        Spark.delete("/session", new LogoutHandler(authData)::handleRequest);

        Spark.post("/user", new RegisterHandler(userData, authData)::handleRequest);
        Spark.post("/session", new LoginHandler(userData, authData)::handleRequest);
        Spark.post("/game", new CreateGameHandler(authData, gameData)::handleRequest);

        Spark.put("/game", new JoinGameHandler(authData, gameData)::handleRequest);

        Spark.get("/game", new ListGameHandler(authData, gameData)::handleRequest);
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public void clear() {
        gameData.clearAll();
        userData.clearAll();
        authData.clearAll();
    }
}
