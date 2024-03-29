package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
    ChessPiece[][] chessboard = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    @Override
    protected ChessBoard clone() {
        ChessBoard clone = new ChessBoard();
        // Perform deep copy of the 2D array
        clone.chessboard = new ChessPiece[this.chessboard.length][];
        for (int i = 0; i < this.chessboard.length; i++) {
            clone.chessboard[i] = this.chessboard[i].clone();
        }
        return clone;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece){
        this.chessboard[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public ChessPiece popPiece(ChessPosition position){
        ChessPiece piece = getPiece(position);
        this.chessboard[position.getRow() - 1][position.getColumn() - 1] = null;
        return piece;
    }

    public void remove_piece(ChessPosition pos) {
        this.chessboard[pos.getRow() - 1][pos.getColumn() - 1] = null;
    }
    public void addPieceUsingRowCol(int row, int col, ChessPiece piece){
        this.chessboard[row][col] = piece;
    }

    public ChessPiece getPieceUsingRowCol(int row, int col){
        return this.chessboard[row][col];
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.chessboard[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Pawns
        for (int i = 0; i < 8; i++) {
            addPieceUsingRowCol(1, i, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPieceUsingRowCol(6, i, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // Rooks
        addPieceUsingRowCol(0, 0, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPieceUsingRowCol(0, 7, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPieceUsingRowCol(7, 0, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPieceUsingRowCol(7, 7, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        // Knights
        addPieceUsingRowCol(0, 1, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPieceUsingRowCol(0, 6, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPieceUsingRowCol(7, 1, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPieceUsingRowCol(7, 6, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        // Bishops
        addPieceUsingRowCol(0, 2, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPieceUsingRowCol(0, 5, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPieceUsingRowCol(7, 2, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPieceUsingRowCol(7, 5, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        // Queens
        addPieceUsingRowCol(0, 3, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPieceUsingRowCol(7, 3, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        // Kings
        addPieceUsingRowCol(0, 4, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPieceUsingRowCol(7, 4, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(chessboard, that.chessboard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessboard);
    }

    public void to_string() {
        System.out.print("|");
        for (int i = chessboard.length - 1; i >= 0; i--) {
            System.out.print("|");
            for (int j = 0; j < chessboard[i].length; j++) {
                ChessPiece value = chessboard[i][j];
                if (value == null) {
                    System.out.print(" |");
                } else {
                    System.out.print(value + "|");
                }
            }
            System.out.println(); // Move to the next line after each row
        }
    }
}
