package clientTests;

import ServerFacade.ServerFacade;
import client.Repl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

public class ServerFacadeTests {

    private static ServerFacade serverFacade;
    private static Server server;


    @BeforeAll
    public static void init() {
        server = new Server();
        server.clear();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        Repl repl = new Repl(8080);
        try {
            serverFacade = new ServerFacade(8080, repl);
        } catch (Exception e) {
            assert(false);
        }
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
        serverFacade.logout(token);
        assert(true);
    }

    @Test
    public void testLogoutFail() {
        String token = serverFacade.register("testUser", "testPass", "testEmail");
        serverFacade.logout("notRealToken");
        assert(false);
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
        int gameId = serverFacade.createGame(token, "testGame");
        serverFacade.listGames(token);

    }

}
