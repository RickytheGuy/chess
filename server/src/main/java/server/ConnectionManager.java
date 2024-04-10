package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, int gameID) {
        var connection = new Connection(visitorName, session, gameID);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName) && c.gameID == gameID) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void send(String visitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(visitorName)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}