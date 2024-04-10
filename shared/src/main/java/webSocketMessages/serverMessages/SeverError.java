package webSocketMessages.serverMessages;

public class SeverError extends ServerMessage{
    private String errorMessage;
    public SeverError(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
}