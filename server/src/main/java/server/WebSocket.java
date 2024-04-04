package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    private final GameDAO gameData;
    private final UserDAO userData;
    private final AuthDAO authData;
    public WebSocket(GameDAO gameData, UserDAO userData, AuthDAO authData) {
        this.gameData = gameData;
        this.userData = userData;
        this.authData = authData;
    }
    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                join_player(session, new Gson().fromJson(message, JoinPlayerCommand.class));
                break;
            case JOIN_OBSERVER:
                connectionManager.add(command.getAuthString(), session);
                break;
            case MAKE_MOVE:
                // Implement this method
                break;
            case LEAVE:
                connectionManager.remove(command.getAuthString());
                break;
            case RESIGN:
                // Implement this method
                break;
        }
        // Implement this method
    }

    private void join_player(Session session, JoinPlayerCommand command) throws IOException {
        connectionManager.add(command.getAuthString(), session);
        ChessGame game = gameData.getGame(command.getGameID());
        if ( game != null) {
            LoadGameMessage game_response = new LoadGameMessage(game);
            connectionManager.send(command.getAuthString(), game_response);
        }
        connectionManager.broadcast(command.getAuthString(), null);
    }
}
