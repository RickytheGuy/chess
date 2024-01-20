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
            } else if (board.getPiece(proposedPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                // If a piece of the same color is in the way, stop movement
                break;
            } else {
                // Allow this piece to be captured, and no more
                moves.add(new ChessMove(position, proposedPosition));
                break;
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
            } else if (board.getPiece(proposedPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                // If a piece of the same color is in the way, stop movement
                break;
            } else {
                // Allow this piece to be captured, and no more
                moves.add(new ChessMove(position, proposedPosition));
                break;
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
            } else if (board.getPiece(proposedPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                // If a piece of the same color is in the way, stop movement
                break;
            } else {
                // Allow this piece to be captured, and no more
                moves.add(new ChessMove(position, proposedPosition));
                break;
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
            } else if (board.getPiece(proposedPosition).getTeamColor() == board.getPiece(position).getTeamColor()) {
                // If a piece of the same color is in the way, stop movement
                break;
            } else {
                // Allow this piece to be captured, and no more
                moves.add(new ChessMove(position, proposedPosition));
                break;
            }
        }
        return moves;
    }

    public static Collection<ChessMove> getKingMoves(ChessBoard board,
                                                       ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        /**Top row**/
        for (int row = position.getRow() - 1; row < position.getRow() + 2; row++) {
            for (int col = position.getColumn() - 1; col < position.getColumn() + 2; col++) {
                if (row < 0 || col < 0 || row > 8 || col > 8) { break;}
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        return moves;

    }

}
