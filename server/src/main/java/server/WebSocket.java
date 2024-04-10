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
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;


@org.eclipse.jetty.websocket.api.annotations.WebSocket
public class WebSocket {
    private final GameDAO gameData;
    private final AuthDAO authData;
    public WebSocket(GameDAO gameData, UserDAO userData, AuthDAO authData) {
        this.gameData = gameData;
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
                join_observer(session, new Gson().fromJson(message, JoinPlayerCommand.class));
                break;
            case MAKE_MOVE:
                make_move(session, new Gson().fromJson(message, MakeMoveCommand.class));
                break;
            case LEAVE:
                leave(session, new Gson().fromJson(message, LeaveCommand.class));
                break;
            case RESIGN:
                resign(session, new Gson().fromJson(message, ResignCommand.class));
                break;
        }
    }

    private void leave(Session session, LeaveCommand command) throws IOException {
        try {authData.getUserFromAuth(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }

        String username;
        try {
            username = authData.getUserFromAuth(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }

        connectionManager.broadcast(command.getAuthString(), new ServerNotification(username + " left the game"), command.getGameID());
        connectionManager.remove(command.getAuthString());


    }

    private void resign(Session session, ResignCommand command) throws IOException {
        try {authData.getUserFromAuth(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }

        ChessGame game = gameData.getGame(command.getGameID());
        if (game == null) {
            connectionManager.send(command.getAuthString(), new SeverError("Error: Game does not exist."));
            return;
        }
        String username;
        try {
            username = authData.getUserFromAuth(command.getAuthString());
            ArrayList<GameData> games = gameData.listGames();
            for (GameData data : games) {
                if (data.gameID() == command.getGameID()) {
                    if (data.blackUsername().equals(username)) {
                        if (data.whiteUsername().isEmpty()) {
                            throw new Exception("Error: Cannot resign from a game with no opponent");
                        }
                        gameData.updateGame(command.getGameID(), null, "", "black");
                        break;
                    } else if (data.whiteUsername().equals(username)) {
                        if (data.blackUsername().isEmpty()) {
                            throw new Exception("Error: Cannot resign from a game with no opponent");
                        }
                        gameData.updateGame(command.getGameID(), null, "", "white");
                        break;
                    } else {
                        throw new Exception("Error: Observer cannot resign");
                    }
                }
            }

            connectionManager.send(command.getAuthString(), new ServerNotification(username + " has resigned"));
            connectionManager.broadcast(command.getAuthString(), new ServerNotification(username + " has resigned"), command.getGameID());

            connectionManager.remove(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
        }
    }

    private void make_move(Session session, MakeMoveCommand command) throws IOException {
        String username;
        try {authData.getUserFromAuth(command.getAuthString());
            username = authData.getUserFromAuth(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }

        ChessGame game = gameData.getGame(command.getGameID());
        if (game == null) {
            connectionManager.send(command.getAuthString(), new SeverError("Error: Game does not exist."));
            return;
        }
        GameData gd = new GameData(0, null, null, null, null);
        ArrayList<GameData> games = gameData.listGames();
        for (GameData data : games) {
            if (data.gameID() == command.getGameID()) {
                gd = data;
                if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && data.blackUsername().equals(username)) {
                    break;

                } else if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && data.whiteUsername().equals(username)) {
                    break;
                } else {
                    connectionManager.send(command.getAuthString(), new SeverError("Cannot make a move for the opponent."));
                    return;
                }

            }
        }

        try {
            game.makeMove(command.getMove());
            gameData.removeGame(command.getGameID());
            gameData.addGame(gd.gameName());
            gameData.updateGame(command.getGameID(), game, gd.whiteUsername(), "white");
            gameData.updateGame(command.getGameID(), game, gd.blackUsername(), "black");
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK) || game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                gameData.removeGame(command.getGameID());
                LoadGameMessage game_response = new LoadGameMessage(game);
                connectionManager.send(command.getAuthString(), game_response);
                connectionManager.broadcast(command.getAuthString(), game_response, command.getGameID());
                connectionManager.broadcast(command.getAuthString(), new ServerNotification(username + " won the game"), command.getGameID());
                return;
            }
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError("Invalid move"));
            return;
        }
        LoadGameMessage game_response = new LoadGameMessage(game);
        connectionManager.send(command.getAuthString(), game_response);
        connectionManager.broadcast(command.getAuthString(), game_response, command.getGameID());
        connectionManager.broadcast(command.getAuthString(), new ServerNotification(username + " made a move"), command.getGameID());
    }

    private void join_observer(Session session, JoinPlayerCommand command) throws IOException {
        connectionManager.add(command.getAuthString(), session, command.getGameID());
        try {authData.getUserFromAuth(command.getAuthString());
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }

        ChessGame game = gameData.getGame(command.getGameID());
        if (game == null) {
            connectionManager.send(command.getAuthString(), new SeverError("Error: Game does not exist."));
            return;
        }
        LoadGameMessage game_response = new LoadGameMessage(game);
        connectionManager.send(command.getAuthString(), game_response);
        connectionManager.broadcast(command.getAuthString(), new ServerNotification("An observer has joined the game"), command.getGameID());
    }

    private void join_player(Session session, JoinPlayerCommand command) throws IOException {
        ChessGame game = gameData.getGame(command.getGameID());
        connectionManager.add(command.getAuthString(), session, command.getGameID());
        if (game == null) {
            connectionManager.send(command.getAuthString(), new SeverError("Error: Game does not exist."));
            return;
        }
        String user;
        try {
            user = authData.getUserFromAuth(command.getAuthString());
            ArrayList<GameData> games = gameData.listGames();
            for (GameData data : games) {
                if (data.gameID() == command.getGameID()) {
                    if ((command.getPlayerColor() == ChessGame.TeamColor.WHITE && data.blackUsername().equals(user)) || (command.getPlayerColor() == ChessGame.TeamColor.BLACK && data.whiteUsername().equals(user))) {
                        throw new Exception("Error: you are already in this game.");
                    }
                }
            }
        } catch (Exception e) {
            connectionManager.send(command.getAuthString(), new SeverError(e.getMessage()));
            return;
        }
        LoadGameMessage game_response = new LoadGameMessage(game);
        connectionManager.send(command.getAuthString(), game_response);
        connectionManager.broadcast(command.getAuthString(), new ServerNotification(user + " joined the game"), command.getGameID());
    }
}
