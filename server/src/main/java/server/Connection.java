package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public final String visitorName;
    public final Session session;

    public final int gameID;

    public Connection(String visitorName, Session session, int GameID) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameID = GameID;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}