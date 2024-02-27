package server;

import spark.*;
import handlers.*;
import dataAccess.*;

public class Server {
    private final GameDAO gameData = new MemoryGameDAO();
    private final DeleteHandler deleteHandler = new DeleteHandler(gameData);
    public Server() {}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> deleteHandler.handleRequest(request, response));
        //Spark.init();
        Spark.awaitInitialization();
        //Spark.post("/user", (request, response) -> (new RegisterHandler()).handleRequest(request, response));

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
