package webSocketMessages.serverMessages;

public class SeverError extends ServerMessage{
    private String message;
    public SeverError(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }
}
