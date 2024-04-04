package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;


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

        ChessGame game = gameData.getGame(command.getGameID());
        String user;
        try {
            user = authData.getUserFromAuth(command.getAuthString());
            ArrayList<GameData> games = gameData.listGames();
            for (int i = 0; i < games.size(); i++) {
                if (games.get(i).gameID() == command.getGameID() && games.get(i).whiteUsername().equals(user) || games.get(i).blackUsername().equals(user)) {
                    connectionManager.add(command.getAuthString(), session);
                    connectionManager.send(command.getAuthString(), new SeverError("Error: you cannot join this game."));
                    return;
                }
            }
        } catch (Exception e) {
            // connectionManager.remove(command.getAuthString());
            connectionManager.add(command.getAuthString(), session);
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }
        if ( game != null) {
            LoadGameMessage game_response = new LoadGameMessage(game);
            connectionManager.add(command.getAuthString(), session);
            connectionManager.send(command.getAuthString(), game_response);
        }
        connectionManager.broadcast(command.getAuthString(), new ServerNotification(user + " joined the game"));
    }
}
