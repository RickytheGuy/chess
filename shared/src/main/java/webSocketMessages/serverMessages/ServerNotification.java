package webSocketMessages.serverMessages;

public class ServerNotification extends ServerMessage {
    public ServerNotification(String message) {
        super(ServerMessageType.NOTIFICATION);
    }
}
