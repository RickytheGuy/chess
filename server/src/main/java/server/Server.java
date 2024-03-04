package server;

import spark.*;
import handlers.*;
import dataAccess.*;

public class Server {
    private GameDAO gameData;
    private UserDAO userData;
    private AuthDAO authData;
    private final ClearHandler clearHandler = new ClearHandler(gameData, userData, authData);
    private final LoginHandler loginHandler = new LoginHandler(userData, authData);
    private final RegisterHandler registerHandler = new RegisterHandler(userData, authData);
    private final LogoutHandler logoutHandler = new LogoutHandler(authData);
    private final CreateGameHandler createGameHandler = new CreateGameHandler(authData, gameData);
    private final JoinGameHandler joinGameHandler = new JoinGameHandler(authData, gameData);
    public Server() {}

    public int run(int desiredPort) {
        SqlDAO db = new SqlDAO();
        gameData = db;
        userData = db;
        authData = db;
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);

        Spark.post("/user", registerHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.post("/game", createGameHandler::handleRequest);

        Spark.put("/game", joinGameHandler::handleRequest);

        Spark.get("/game", new ListGameHandler(authData, gameData)::handleRequest);
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
