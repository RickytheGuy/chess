package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private ChessGame game;
    private ChessBoard board;
    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.board = game.getBoard();
    }

    @Override
    public ServerMessageType getServerMessageType() {
        return ServerMessageType.LOAD_GAME;
    }

    public ChessGame getGame() {
        return game;
    }
}
