package clientTests;

import ServerFacade.ServerFacade;
import client.Repl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

public class ServerFacadeTests {

    private static ServerFacade serverFacade;
    private static Server server;


    @BeforeAll
    public static void init() {
        server = new Server();
        server.clear();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        Repl repl = new Repl(port);
        try {
            serverFacade = new ServerFacade(port, repl);
        } catch (Exception e) {
            assert(false);
        }
    }

    @BeforeEach
    public void clear() {
        server.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testInitOk() {

        String token = serverFacade.register("testUser", "testPass", "testEmail");
        assert (token != null);

    }

    @Test
    public void testInitFail() {
        serverFacade.register("testUser", "testPass", "testEmail");
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        assert(token == null);

    }

    @Test
    public void testLogout() {
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        assert(serverFacade.logout(token));
    }

    @Test
    public void testLogoutFail() {
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        assert(!serverFacade.logout("notRealToken"));
    }

    @Test
    public void createGame() {

        String token = serverFacade.register("testUser", "testPass", "testEmail");
        int gameId = serverFacade.createGame(token, "testGame");
        assert(gameId != -1);

    }

    @Test
    public void createGameFail() {

        String token = serverFacade.register("testUser", "testPass", "testEmail");
        int gameId = serverFacade.createGame(token, null);
        assert(gameId == -1);

    }
    @Test
    public void listGames() {

        String token = serverFacade.register("testUser", "testPass", "testEmail");
        serverFacade.createGame(token, "testGame");
        int gameId = serverFacade.createGame(token, "otherGame");
        assert(serverFacade.listGames(token));

    }

    @Test
    public void listGamesFail() {

        String token = serverFacade.register("testUser", "testPass", "testEmail");
        serverFacade.createGame(token, "testGame");
        int gameId = serverFacade.createGame(token, "otherGame");
        assert(!serverFacade.listGames("notRealToken"));

    }

    @Test
    public void joinGame() {
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        int gameId = serverFacade.createGame(token, "testGame");
        assert(serverFacade.joinGame(token, gameId, "WHITE"));
    }

    @Test
    public void joinGameFail() {
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        serverFacade.createGame(token, "testGame");
        assert(!serverFacade.joinGame(token, -1, "WHITE"));

    }

    @Test
    public void drawBoardBlack() {
        // serverFacade.repl.drawChessboard(true, game);
    }

    @Test
    public void drawBoardWhite() {
        // serverFacade.repl.drawChessboard(false, game);
    }
}
