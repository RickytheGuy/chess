package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        ChessBoard board = game.getBoard();
    }

    @Override
    public ServerMessageType getServerMessageType() {
        return ServerMessageType.LOAD_GAME;
    }

    public ChessGame getGame() {
        return game;
    }
}
