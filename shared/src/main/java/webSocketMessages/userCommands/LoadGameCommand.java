package webSocketMessages.userCommands;

public class LoadGameCommand extends UserGameCommand {
    private final int gameID;
    public LoadGameCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LOAD_GAME;

    }

    public int getGameID() {
        return gameID;
    }
}
