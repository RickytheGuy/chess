package chess;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    public Collection<ChessMove> getKingMoves(ChessBoard board,
                                              ChessPosition position) {
        List<ChessMove> moves = new List<>();
        /**Left and right**/
        int col = position.getColumn();
        for (int row = position.getRow(); row > 0; row--) {
            col--;
            ChessPosition proposedPosition = new ChessPosition(row, col);
            if board.getPiece(proposedPosition) != NULL {
                moves.add(ChessMove(position, ));
            }
        }

    }

}
