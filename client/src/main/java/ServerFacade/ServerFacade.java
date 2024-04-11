package ServerFacade;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import client.Repl;
import com.google.gson.Gson;
import requests.*;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.LeaveCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.ResignCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final URI uri;
    public Repl repl;

    private final WebSocketFacade webSocketFacade;
    public ServerFacade(int port, Repl repl)  throws Exception{
        uri = new URI("http://localhost:" + port);
        this.repl = repl;
        webSocketFacade = new WebSocketFacade("http://localhost:"+ port, this);
    }
    public String register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResponse response;
        try {
            response = makeRequest("POST", "/user", request, RegisterResponse.class, null);
        }
        catch (Exception e) {
            repl.printRegisterFail();
            return null;
        }
        return response.authToken();
    }

    public String login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response;
        try {
            response =  makeRequest("POST", "/session", request, LoginResponse.class, null);
        } catch (Exception e) {
            repl.printLoginFail();
            return null;
        }
        // Login a user
        return response.authToken();
    }

    public boolean logout(String authToken) {
        LogoutRequest request = new LogoutRequest(authToken);
        try {
            makeRequest("DELETE", "/session", request, null, authToken);
        } catch (Exception e) {
            repl.printLogoutFail();
            return false;
        }
        // Logout a user
        return true;
    }

    public int createGame(String authToken, String gameName) {
        CreateGameRequest request = new CreateGameRequest(gameName);
        CreateGameResponse response;
        try {
            response = makeRequest("POST", "/game", request, CreateGameResponse.class, authToken);
        } catch (Exception e) {
            repl.printCreateGameFail();
            return -1;
        }
        repl.printCreateGameSuccess(gameName, response.gameID());
        return response.gameID();
    }

    public boolean listGames(String authToken) {
        ListGameResponse response;
        try {
            response = makeRequest("GET", "/game", null, ListGameResponse.class, authToken);
        } catch (Exception e) {
            repl.printListGamesFail();
            return false;
        }
        repl.printListGamesSuccess(response.games());
        return true;
    }

    public boolean joinGame(String authToken, int gameID, String playerColor) {
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        JoinGameResponse response;
        ChessGame.TeamColor p = ChessGame.TeamColor.WHITE;;
        if (playerColor == null) {
            try {
                webSocketFacade.send(new Gson().toJson(new JoinPlayerCommand(authToken, gameID, null)));
                response = makeRequest("PUT", "/game", request, JoinGameResponse.class, authToken);
                repl.printObserverJoinGameSuccess(gameID);
                return true;
            } catch (Exception e) {
                repl.printJoinGameFail(gameID);
                return false;
            }
        } else if (playerColor.equals("WHITE")) {
            p = ChessGame.TeamColor.WHITE;
        }  else if (playerColor.equals("BLACK")) {
            p = ChessGame.TeamColor.BLACK;
        }

        try {
            webSocketFacade.send(new Gson().toJson(new JoinPlayerCommand(authToken, gameID, p)));
            response = makeRequest("PUT", "/game", request, JoinGameResponse.class, authToken);
        } catch (Exception e) {
            repl.printJoinGameFail(gameID);
            return false;
        }

        repl.printJoinGameSuccess(playerColor, gameID);
        return true;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String token) throws Exception {
        try {
            URL url = URI.create(uri + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (token != null) {
                http.addRequestProperty("Authorization", token);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception("Failed to make request: " + ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception(String.valueOf(status));
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    public void move(String token, int gameID, int row, String col, int destRow, String destCol, ChessGame.TeamColor playerColor) throws InvalidMoveException {
        if (row >= 0 && row <= 7 && destRow >= 0 && destRow <= 7 && col.length() == 1 && destCol.length() == 1) {
            MakeMoveCommand command = new MakeMoveCommand(token,gameID, new ChessMove(new ChessPosition(row, col.charAt(0)), new ChessPosition(destRow, destCol.charAt(0))), playerColor);
            try {
                webSocketFacade.send(new Gson().toJson(command));
                //return true;
            } catch (Exception e) {
                //return false;
            }
        } else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    public void resign(String token, int gameID) {
        // Resign from a game
        webSocketFacade.send(new Gson().toJson(new ResignCommand(token, gameID)));
    }

    public void leaveGame(String token, int currentGameID) {
        webSocketFacade.send(new Gson().toJson(new LeaveCommand(token, currentGameID)));
        repl.setGame(null);
    }

    public void clearGames(String authToken) {
        // Make a http request to clear all games
        try {
            makeRequest("DELETE", "/session", null, null, authToken);
        } catch (Exception e) {

        }

    }

    public void loadGame(String token, int gameID) {
        // Load a game
        webSocketFacade.send(new Gson().toJson(new webSocketMessages.userCommands.LoadGameCommand(token, gameID)));
    }
}
