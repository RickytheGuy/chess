package server;

import spark.*;
import handlers.*;
import dataAccess.*;

public class Server {
    private final GameDAO gameData = new MemoryGameDAO();
    private final DeleteHandler deleteHandler = new DeleteHandler(gameData);
    private final LoginHandler loginHandler = new LoginHandler(gameData);
    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", deleteHandler::handleRequest);
        Spark.post("/user", loginHandler::handleRequest);
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
