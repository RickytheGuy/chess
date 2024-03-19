package clientTests;

import ServerFacade.ServerFacade;
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
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        try {
            serverFacade = new ServerFacade(8080);
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
        try {
            serverFacade.register("testUser", "testPass", "testEmail");
        }
        catch (Exception e) {
            assert(false);
        }
    }

}
