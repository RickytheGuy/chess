package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    public static Collection<ChessMove> getBishopMoves(ChessBoard board,
                                              ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
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

        col = position.getColumn();
        for (int row = position.getRow() + 1; row < 9; row++) {
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
        return moves;
    }

    public static Collection<ChessMove> getKingMoves(ChessBoard board,
                                                       ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        for (int row = position.getRow() - 1; row < position.getRow() + 2; row++) {
            for (int col = position.getColumn() - 1; col < position.getColumn() + 2; col++) {
                if (row < 1 || col < 1 || row > 8 || col > 8) { break;}
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        return moves;
    }

    public static Collection<ChessMove> getKnightMoves(ChessBoard board,
                                                     ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int row = position.getRow() - 2;
        int col = position.getColumn() - 1;
        if (row > 0) {
            if (col > 0) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
            col += 2;
            if (col < 9) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        row += 1;
        col = position.getColumn() - 2;
        if (row > 0) {
            if (col > 0) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
            col += 4;
            if (col < 9) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        row += 2;
        col = position.getColumn() - 2;
        if (row < 9) {
            if (col > 0) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
            col += 4;
            if (col < 9) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        row += 1;
        col = position.getColumn() - 1;
        if (row < 9) {
            if (col > 0) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
            col += 2;
            if (col < 9) {
                ChessPosition proposedPosition = new ChessPosition(row, col);
                if (board.getPiece(proposedPosition) == null || board.getPiece(proposedPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }
        }
        return moves;
    }

    public static Collection<ChessMove> getPawnMoves(ChessBoard board,
                                                     ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor my_color = board.getPiece(position).getTeamColor();
        int direction = 1;
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {
            direction = -1;
        }
        int row = position.getRow();
        int col = position.getColumn();
        if (row + direction < 9 && row + direction > 0) {
            ChessPosition proposedPosition = new ChessPosition(row + direction, col);
            // Forward
            if (board.getPiece(proposedPosition) == null) {
                // Promotions
                if ((row == 7 && direction == 1) || (row == 2 && direction == -1)) {
                    if (board.getPiece(proposedPosition) == null) {
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.KNIGHT));
                    }
                } else {
                    moves.add(new ChessMove(position, proposedPosition));
                }
            }

            // Captures and promotions
            if ((row == 7 && direction == 1) || (row == 2 && direction == -1)) {
                if (col - 1 > 0) {
                    proposedPosition = new ChessPosition(row + direction, col - 1);
                    if (board.getPiece(proposedPosition) != null && board.getPiece(proposedPosition).getTeamColor() != my_color) {
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.KNIGHT));
                    }
                }
                if (col + 1 < 9) {
                    proposedPosition = new ChessPosition(row + direction, col + 1);
                    if (board.getPiece(proposedPosition) != null && board.getPiece(proposedPosition).getTeamColor() != my_color) {
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, proposedPosition, ChessPiece.PieceType.KNIGHT));
                    }
                }

            } else {
                // Captures
                if (col - 1 > 0) {
                    proposedPosition = new ChessPosition(row + direction, col - 1);
                    if (board.getPiece(proposedPosition) != null && board.getPiece(proposedPosition).getTeamColor() != my_color) {
                        moves.add(new ChessMove(position, proposedPosition));
                    }
                }
                if (col + 1 < 9) {
                    proposedPosition = new ChessPosition(row + direction, col + 1);
                    if (board.getPiece(proposedPosition) != null && board.getPiece(proposedPosition).getTeamColor() != my_color) {
                        moves.add(new ChessMove(position, proposedPosition));
                    }
                }
            }
        }

        // Double move
        if ((row == 7 && direction == -1) || (row == 2 && direction == 1)) {
            ChessPosition proposedPosition = new ChessPosition(row + (direction * 2), col);
            // Forward
            if (board.getPiece(proposedPosition) == null && board.getPiece(new ChessPosition(row + direction, col)) == null) {
                moves.add(new ChessMove(position, proposedPosition));
            }
        }


        return moves;
    }

    public static Collection<ChessMove> getQueenMoves(ChessBoard board,
                                                       ChessPosition position) {
        Collection<ChessMove> moves = getRookMoves(board, position);
        moves.addAll(getBishopMoves(board, position));
        return moves;
    }

    public static Collection<ChessMove> getRookMoves(ChessBoard board,
                                                      ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int col = position.getColumn();
        for (int row = position.getRow() - 1; row > 0; row--) {
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

        for (int row = position.getRow() + 1; row < 9; row++) {
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

        int row = position.getRow();
        for (col = position.getColumn() + 1; col < 9; col++) {
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

        for (col = position.getColumn() -1; col > 0; col--) {
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

}
