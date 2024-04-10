package webSocketMessages.serverMessages;

public class SeverError extends ServerMessage{
    public SeverError(String errorMessage) {
        super(ServerMessageType.ERROR);
    }
}
