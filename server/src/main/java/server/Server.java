package server;

import spark.*;
import handlers.*;
import dataAccess.*;

public class Server {
    private final GameDAO gameData = new MemoryGameDAO();
    private final UserDAO userData = new MemoryUserDAO();
    private final AuthDAO authData = new MemoryAuthDAO();
    private final DeleteHandler deleteHandler = new DeleteHandler(gameData, userData, authData);
    private final LoginHandler loginHandler = new LoginHandler(userData, authData);
    private final RegisterHandler registerHandler = new RegisterHandler(userData, authData);
    private final LogoutHandler logoutHandler = new LogoutHandler(authData);
    private final JoinGameHandler joinGameHandler = new JoinGameHandler(userData, authData, gameData);
    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", deleteHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);

        Spark.post("/user", registerHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.post("/game", joinGameHandler::handleRequest);
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
