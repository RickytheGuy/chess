package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor teamColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceType = type;
        this.teamColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.getPieceType() == PieceType.BISHOP) {
            return PieceMovesCalculator.getBishopMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.KING) {
            return PieceMovesCalculator.getKingMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.KNIGHT) {
            return PieceMovesCalculator.getKnightMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.PAWN) {
                return PieceMovesCalculator.getPawnMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.QUEEN) {
            return PieceMovesCalculator.getQueenMoves(board, myPosition);
        } else if (this.getPieceType() == PieceType.ROOK) {
            return PieceMovesCalculator.getRookMoves(board, myPosition);
        } else {
            throw new RuntimeException("Not implemented");
        }
    }

    @Override
    public String toString() {
        if (pieceType == PieceType.BISHOP) {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "B";
            } else { return "b"; }
        } else if (pieceType == PieceType.PAWN) {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "P";
            } else { return "p"; }
        } else if (pieceType == PieceType.ROOK) {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "R";
            } else { return "r"; }
        } else if (pieceType == PieceType.QUEEN) {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "Q";
            } else { return "q"; }
        } else if (pieceType == PieceType.KNIGHT) {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "N";
            } else { return "n"; }
        } else {
            if (teamColor == ChessGame.TeamColor.WHITE ) {
                return "K";
            } else { return "k"; }
        }
    }
}
