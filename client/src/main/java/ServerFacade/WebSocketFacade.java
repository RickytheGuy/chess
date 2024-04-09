package ServerFacade;

import chess.ChessGame;
import com.google.gson.Gson;
import passoffTests.testClasses.TestException;
import requests.ErrorResponse;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    private Session session;
    private ServerFacade serverFacade;

    public WebSocketFacade(String url, ServerFacade s) throws Exception {
        // Initialize your WebSocket here
        try {
            serverFacade = s;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    // Handle message
                    ServerMessage.ServerMessageType type = new Gson().fromJson(message, ServerMessage.class).getServerMessageType();
                    switch (type) {
                        case ERROR:
                            ErrorResponse error = new Gson().fromJson(message, ErrorResponse.class);
                            serverFacade.repl.printError(error);
                        case LOAD_GAME:
                            // Implement this method
                            LoadGameMessage m = new Gson().fromJson(message, LoadGameMessage.class);
                            ChessGame game = m.getGame();
                            serverFacade.repl.drawChessboard(game);
                            break;
                    }
                }
            }
            );

        } catch (Exception e) {
            throw new TestException("500: Error: Failed to connect to WebSocket");
        }
    }

    public void send(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new TestException("500: Error: Failed to send message");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
    }


}
