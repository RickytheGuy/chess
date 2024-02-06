package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teams_turn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return this.teams_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        this.teams_turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Get all moves this piece could make
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> valid_moves = new ArrayList<>();

        for (ChessMove move : moves) {
            ChessBoard board_copy = (ChessBoard) this.board.clone();
            ChessPiece piece = board_copy.popPiece(move.getStartPosition());
            board_copy.addPiece(move.getEndPosition(), piece);
            // If the king is out of check, add to valid moves list
            if (!isInBoardCheck(board.getPiece(startPosition).getTeamColor(), board_copy)) {
                valid_moves.add(move);
            }
        }

        return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // If this move is for the other team, stop
        if (board.getPiece(move.getStartPosition()).getTeamColor() != this.teams_turn) {
            throw new InvalidMoveException();
        }
        // check if the piece can move to that square with no other restrictions...
        if (board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition()).contains(move) ) {
            // If the king is in check...
            if (isInCheck(this.teams_turn)) {
                // Create a new board with this move done...
                ChessBoard board_copy = (ChessBoard) this.board.clone();
                ChessPiece piece = board_copy.popPiece(move.getStartPosition());
                board_copy.addPiece(move.getEndPosition(), piece);

                // ... and check if the new board's king is in check
                if (isInBoardCheck(this.teams_turn, board_copy)) {
                    throw new InvalidMoveException();
                }
            }

            // If this is a promotion...
            if (move.getPromotionPiece() != null) {
                ChessPiece.PieceType type = move.getPromotionPiece();
                board.remove_piece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), new ChessPiece(teams_turn, type));

            } else {
                ChessPiece piece = board.popPiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), piece);
            }
            if (TeamColor.WHITE == this.teams_turn) {
                this.teams_turn = TeamColor.BLACK;
            } else {
                this.teams_turn = TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException();
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kings_pos = findKing(teamColor, this.board);
        if (kings_pos == null) {
            return false;
        }
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    // Check this enemy piece and see if its valid moves include capturing the king
                    Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(row+1, col+1));
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().getRow() == kings_pos.getRow() && move.getEndPosition().getColumn() == kings_pos.getColumn()) {
                            // If there is a move in the enemies moves that involve capturing this teams king, this king is in check
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // checks is
    public boolean isInBoardCheck(TeamColor teamColor, ChessBoard a_board) {
        ChessPosition kings_pos = findKing(teamColor, a_board);
        if (kings_pos == null) {
            return false;
        }
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = a_board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    // Check this enemy piece and see if its valid moves include capturing the king
                    Collection<ChessMove> moves = piece.pieceMoves(a_board, new ChessPosition(row+1, col+1));
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().getRow() == kings_pos.getRow() && move.getEndPosition().getColumn() == kings_pos.getColumn()) {
                            // If there is a move in the enemies moves that involve capturing this teams king, this king is in check
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**Finds the king of the given color on the board **/
    public ChessPosition findKing(TeamColor color, ChessBoard a_board) {
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = a_board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return new ChessPosition(row+1, col+1);
                }
            }
        }
        return null;
    }

    // Check if any pieces of the opposing color can capture this color's king
    public boolean isKingCapturable(TeamColor color) {
        for (int row=0; row < 8; row++) {
            for (int col=0; col < 8; col++) {
                ChessPiece piece = board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() != color) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row+1, col+1));
                    for (ChessMove move: moves) {
                        piece = board.getPiece(move.getEndPosition());
                        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public boolean isKingCapturableOnBoard(TeamColor color, ChessBoard a_board) {
        for (int row=0; row < 8; row++) {
            for (int col=0; col < 8; col++) {
                ChessPiece piece = a_board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() != color) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row+1, col+1));
                    for (ChessMove move: moves) {
                        piece = a_board.getPiece(move.getEndPosition());
                        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // For each piece on our team...
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    // If this piece has any valid moves...
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row+1, col+1));
                    if (moves != null) {
                        // ...make sure king cannot be immediately captured
                        if (!isKingCapturable(teamColor)) {
                            return false;
                        }
                    }
                    }
                }
            }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPieceUsingRowCol(row, col);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    // If this piece has any valid moves...
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row+1, col+1));
                    if (moves != null) {
                        for (ChessMove move: moves) {
                            // Create a new board with this move done...
                            ChessBoard board_copy = (ChessBoard) this.board.clone();
                            ChessPiece a_piece = board_copy.popPiece(move.getStartPosition());
                            board_copy.addPiece(move.getEndPosition(), a_piece);
                            // ...make sure king cannot be immediately captured
                            if (!isKingCapturableOnBoard(teamColor, board_copy)) {
                                return false;
                            }
                        }
                    }

                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public void to_string() { board.to_string();}

}
