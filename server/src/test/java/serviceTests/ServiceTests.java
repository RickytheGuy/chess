package serviceTests;

import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.*;
import dataAccess.MemoryGameDAO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import requests.ChessResponse;
import requests.*;
import services.*;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
    @Test
    @Order(1)
    public void clear() {
        // Setup
        GameDAO gameData = new MemoryGameDAO();
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        try {
            gameData.addGame("testGame");
            userData.addUser("testUser", "testPassword", "testEmail");
            authData.addAuth("testUser");
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        ClearService clearGameService = new ClearService(gameData, userData, authData);
        clearGameService.clearAll();
        assert(gameData.listGames().size() == 0);
        assert(userData.size() == 0);
        assert(authData.size() == 0);

    }

    @Test
    @Order(2)
    public void registerSuccess() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        assert (registerResponse instanceof RegisterResponse);
        assert (((RegisterResponse) registerResponse).username() != null);
        assert (((RegisterResponse) registerResponse).authToken() != null);
        assert (((RegisterResponse) registerResponse).username().equals("testUser"));
        assert (((RegisterResponse) registerResponse).authToken().length() > 0);
    }

    @Test
    @Order(3)
    public void registerFailAlreadyExists() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // Try to register with same username
        registerResponse = registerService.register(new RegisterRequest("testUser", "newPassword", "whatever"));

        assert (registerResponse instanceof ErrorResponse);
        assert (((ErrorResponse) registerResponse).status() == 403);
    }

    @Test
    @Order(4)
    public void registerBadEntry() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService(userData, authData);

        // Try to register with null username
        ChessResponse registerResponse = registerService.register(new RegisterRequest(null, "testPassword", "testEmail"));
        assert (registerResponse instanceof ErrorResponse);
        assert (((ErrorResponse) registerResponse).status() == 400);

        // Try to register with null password
        registerResponse = registerService.register(new RegisterRequest("null", null, "testEmail"));
        assert (registerResponse instanceof ErrorResponse);
        assert (((ErrorResponse) registerResponse).status() == 400);
    }

    @Test
    @Order(5)
    public void loginSuccess() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        LoginService loginService = new LoginService(userData, authData);
        ChessResponse loginResponse = loginService.login(new LoginRequest("testUser", "testPassword"));
        assert(loginResponse instanceof LoginResponse);
        assert(((LoginResponse) loginResponse).username() != null);
        assert(((LoginResponse) loginResponse).authToken() != null);
        assert (((LoginResponse) loginResponse).username().equals("testUser"));
        assert (((LoginResponse) loginResponse).authToken().length() > 0);

    }

    @Test
    @Order(6)
    public void loginFail() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        LoginService loginService = new LoginService(userData, authData);
        ChessResponse loginResponse = loginService.login(new LoginRequest("notTheUser", "testPassword"));
        assert(loginResponse instanceof ErrorResponse);
        assert (((ErrorResponse) loginResponse).status() == 401);

    }

    @Test
    @Order(7)
    public void createGameSuccess() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();
        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest("testGame"), ((RegisterResponse) registerResponse).authToken());

        assert(createGameResponse instanceof CreateGameResponse);
        assert(((CreateGameResponse) createGameResponse).gameID() == 1);
    }

    @Test
    @Order(8)
    public void createGameFail() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();
        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        // Try to create game with null game name
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest(null), ((RegisterResponse) registerResponse).authToken());

        assert(createGameResponse instanceof ErrorResponse);
        assert (((ErrorResponse) createGameResponse).status() == 400);

    }

    @Test
    @Order(9)
    public void joinGameSuccess() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();
        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest("testGame"), ((RegisterResponse) registerResponse).authToken());

        JoinGameService joinGameService = new JoinGameService(authData, gameData);
        ChessResponse joinGameResponse = joinGameService.joinGame(new JoinGameRequest("BLACK", ((CreateGameResponse) createGameResponse).gameID()), ((RegisterResponse) registerResponse).authToken());
        assert(joinGameResponse instanceof JoinGameResponse);
    }

    @Test
    @Order(10)
    public void joinGameFail() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();
        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse = registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest("testGame"), ((RegisterResponse) registerResponse).authToken());

        JoinGameService joinGameService = new JoinGameService(authData, gameData);
        // Try to join game that doesn't exist
        ChessResponse joinGameResponse = joinGameService.joinGame(new JoinGameRequest("BLACK", 23), ((RegisterResponse) registerResponse).authToken());
        assert(joinGameResponse instanceof ErrorResponse);
        assert (((ErrorResponse) joinGameResponse).status() == 400);

    }



    @Test
    @Order(13)
    public void listSuccess() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse =  registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest("testGame"), ((RegisterResponse) registerResponse).authToken());
        createGameResponse = createGameService.createGame(new CreateGameRequest("Game2"), ((RegisterResponse) registerResponse).authToken());


        JoinGameService joinGameService = new JoinGameService(authData, gameData);
        joinGameService.joinGame(new JoinGameRequest("WHITE", ((CreateGameResponse) createGameResponse).gameID()), ((RegisterResponse) registerResponse).authToken());

        ListGameService listGamesService = new ListGameService(authData, gameData);
        ChessResponse listGamesResponse = listGamesService.listGames(new ListGameRequest(), ((RegisterResponse) registerResponse).authToken());
        assert(listGamesResponse instanceof ListGameResponse);
        ArrayList<GameData> games = ((ListGameResponse) listGamesResponse).games();
        assert(games.size() == 2);
        assert(games.get(0).gameId() == 1);
        assert (games.get(0).gameName().equals("testGame"));
        assert(games.get(0).whiteUsername() == null);
        assert(games.get(0).blackUsername() == null);
        assert(games.get(1).gameId() == 2);
        assert (games.get(1).gameName().equals("Game2"));
        assert(games.get(1).whiteUsername().equals("testUser"));
        assert(games.get(1).blackUsername() == null);
        }

    @Test
    @Order(14)
    public void listFail() {
        // Setup
        UserDAO userData = new MemoryUserDAO();
        AuthDAO authData = new MemoryAuthDAO();
        GameDAO gameData = new MemoryGameDAO();

        RegisterService registerService = new RegisterService(userData, authData);
        ChessResponse registerResponse =  registerService.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameService createGameService = new CreateGameService(userData, authData, gameData);
        ChessResponse createGameResponse = createGameService.createGame(new CreateGameRequest("testGame"), ((RegisterResponse) registerResponse).authToken());
        createGameResponse = createGameService.createGame(new CreateGameRequest("Game2"), ((RegisterResponse) registerResponse).authToken());


        JoinGameService joinGameService = new JoinGameService(authData, gameData);
        joinGameService.joinGame(new JoinGameRequest("WHITE", ((CreateGameResponse) createGameResponse).gameID()), ((RegisterResponse) registerResponse).authToken());

        ListGameService listGamesService = new ListGameService(authData, gameData);
        // Try to list games with bad auth token
        ChessResponse listGamesResponse = listGamesService.listGames(new ListGameRequest(), "badAuthToken");
        assert(listGamesResponse instanceof ErrorResponse);
        assert (((ErrorResponse) listGamesResponse).status() == 401);

    }

}
