package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    public static Collection<ChessMove> getBishopMoves(ChessBoard board,
                                              ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        /**Left and up**/
        int col = position.getColumn();
        for (int row = position.getRow() - 1; row > 0; row--) {
            col--;
            if (col < 1) {
                break;
            }
            ChessPosition proposedPosition = new ChessPosition(row, col);
            if (board.getPiece(proposedPosition) == null) {
                moves.add(new ChessMove(position, proposedPosition));
            }
        }

        /**Right and up**/
        col = position.getColumn();
        for (int row = position.getRow() - 1; row > 0; row--) {
            col++;
            if (col > 8) {
                break;
            }
            ChessPosition proposedPosition = new ChessPosition(row, col);
            if (board.getPiece(proposedPosition) == null) {
                moves.add(new ChessMove(position, proposedPosition));
            }
        }

        /**Left and down**/
        col = position.getColumn();
        for (int row = position.getRow() + 1; row < 9; row++) {
            col--;
            if (col < 1) {
                break;
            }
            ChessPosition proposedPosition = new ChessPosition(row, col);
            if (board.getPiece(proposedPosition) == null) {
                moves.add(new ChessMove(position, proposedPosition));
            }
        }

        /**Right and down**/
        col = position.getColumn();
        for (int row = position.getRow() + 1; row < 9; row++) {
            col++;
            if (col < 1) {
                break;
            }
            ChessPosition proposedPosition = new ChessPosition(row, col);
            if (board.getPiece(proposedPosition) == null) {
                moves.add(new ChessMove(position, proposedPosition));
            }
        }

        return moves;

    }

}
