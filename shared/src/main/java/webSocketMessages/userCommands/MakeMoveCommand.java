package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private Integer gameID;
    private ChessMove move;
    private ChessGame.TeamColor playerColor;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
