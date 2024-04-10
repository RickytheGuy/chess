package client;

import ServerFacade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import requests.ErrorResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private ServerFacade sf;
    private String token;
    private boolean white = true;
    private ChessGame.TeamColor playerColor;
    private int currentGameID;
    public Repl (int port) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        try {
            sf = new ServerFacade(port, this);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showLoginOptions() {
        System.out.println("1. Register\t2. Login");
        System.out.println("3. Exit\t\t4. Help");
        System.out.println("Please enter the number your choice: ");
    }

    public void loginScreen() {
        showLoginOptions();
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                            System.out.println("Invalid choice. Please try again.");
                            scanner.nextLine();
                            continue;
                        }
            if (choice == 1) {
                System.out.println("Enter your username: ");
                String username = scanner.next();
                System.out.println("Enter your password: ");
                String password = scanner.next();
                System.out.println("Enter your email: ");
                String email = scanner.next();
                token = sf.register(username, password, email);
                if (token != null) {
                    loggedInScreen();
                }

            } else if (choice == 2) {
                System.out.println("Enter your username: ");
                String username = scanner.next();
                System.out.println("Enter your password: ");
                String password = scanner.next();
                token = sf.login(username, password);
                if (token != null) {
                    loggedInScreen();
                    showLoginOptions();
                }
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                if (token != null) {
                    sf.logout(token);
                }
                break;
            } else if (choice == 4) {
                showLoginOptions();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void printRegisterFail() {
        System.out.println("User already exists. Please try again.");
        showLoginOptions();
    }

    public void loggedInScreen() {
        show_logged_in_options();
        Scanner scanner = new Scanner(System.in);
        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice == 1) {
                    // Create Game
                    System.out.println("Enter the name of the game: ");
                    String gameName = scanner.next();
                    int gameID = sf.createGame(token, gameName);
                } else if (choice == 2) {
                    sf.listGames(token);
                    // List Games
                } else if (choice == 3) {
                    // Join Game
                    System.out.println("Enter the ID of the game you want to join: ");
                    int gameID = scanner.nextInt();
                    System.out.println("Enter the color you want to play as (w/b): ");
                    String playerColor = scanner.next();
                    boolean success = false;
                    if (playerColor.equals("w") ) {
                        success = sf.joinGame(token, gameID, "WHITE");
                    } else if (playerColor.equals("b")) {
                        success = sf.joinGame(token, gameID, "BLACK");
                        white = false;
                    } else {
                        throw new Exception();
                    }
                    if (success) {
                        gameScreen(true);
                        show_logged_in_options();
                        if (playerColor.equals("w")) {
                            this.playerColor = ChessGame.TeamColor.WHITE;
                        } else {
                            this.playerColor = ChessGame.TeamColor.BLACK;
                        }
                        currentGameID = gameID;
                    }
                } else if (choice == 4) {
                    // Join as Observer
                    System.out.println("Enter the ID of the game you want to observe: ");
                    int gameID = scanner.nextInt();
                    boolean success = sf.joinGame(token, gameID, null);
                    if (success) {
                        gameScreen(false);
                    }
                } else if (choice == 5) {
                    // Logout
                    if (sf.logout(token)) {
                        break;
                    }
                } else if (choice == 6) {
                    // Help
                    show_logged_in_options();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid choice. Please try again.");
                show_logged_in_options();
                scanner.nextLine();
            }
        }

    }

    public void printLoginFail() {
        System.out.println("Invalid username or password. Please try again.");
        showLoginOptions();
    }

    public void show_logged_in_options() {
        System.out.println("1. Create Game\t2. List Games");
        System.out.println("3. Join Game\t4. Join as Observer");
        System.out.println("5. Logout\t\t6. Help");
        System.out.println("Please enter the number of your choice: ");
    }

    public void printCreateGameFail() {
        System.out.println("Failed to create game. Please try again.");
        show_logged_in_options();
    }

    public void printCreateGameSuccess(String gameName, int gameID) {
        System.out.println("Game " + gameName + " created with ID " + gameID);
        show_logged_in_options();
    }

    public void printListGamesFail() {
        System.out.println("No games found.");
        show_logged_in_options();
    }

    public void printListGamesSuccess(ArrayList<GameData> games) {
        if (games.isEmpty()) {
            System.out.println("No games found.\n");
            show_logged_in_options();
            return;
        }
        System.out.println("Games: ");
        for (GameData game : games) {
            System.out.println("ID: " + game.gameID() + " Name: " + game.gameName() + "White: " + game.whiteUsername() + " Black: " + game.blackUsername());
        }
        System.out.println();
        show_logged_in_options();
    }

    public void printJoinGameFail(int id) {
        System.out.println("Failed to join game " + id + ". Please try again.");
        show_logged_in_options();
    }

    public void printJoinGameSuccess(String playerColor, int gameID) {
        if (playerColor == null) {
            System.out.println("Joined game " + gameID + " as an observer.");
        } else{
            System.out.println("Joined game " + gameID + " as the " + playerColor + " player.");
        }
        show_logged_in_options();
    }

    public void gameScreen(boolean isObserver) {
        while (true) {
            // Get the game state
            // Print the game state
            // Get the move
            // Send the move
            printGameHelp();
            Scanner scanner = new Scanner(System.in);
            int choice;
            try {
                choice = scanner.nextInt();
                if (isObserver) {
                    if (choice == 4) {
                        printGameHelp();
                    } else if (choice == 5) {
                        currentGameID = -1;
                        break;
                    } else if (choice == 1) {
                        // Move a piece
                        System.out.println("Enter the row of the piece you want to move: ");
                        int row = scanner.nextInt();
                        System.out.println("Enter the column of the piece you want to move: ");
                        String col = scanner.next();
                        System.out.println("Enter the row of the destination: ");
                        int destRow = scanner.nextInt();
                        System.out.println("Enter the column of the destination: ");
                        String destCol = scanner.next();
                        // if destRow and destCol are on an end of the board, and the piece is a pawn, we need to get the promotion piece

                        try {
                            sf.move(token, currentGameID, row, col, destRow, destCol, this.playerColor);
                        } catch (Exception e) {
                            printInvalidMove(row, col, destRow, destCol);
                        }
                    } else if (choice == 2) {
                        // Resign
                        sf.resign(token, currentGameID);
                        Thread.sleep(2000);
                        break;
                    } else if (choice == 3) {
                        // Offer a draw
                        //sf.offerDraw(token
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                } else {
                    if (choice == 1) {
                        // Exit
                        currentGameID = -1;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid choice. Please try again.");
                printGameHelp();
                scanner.nextLine();
            }

        }
    }

    private void printInvalidMove(int row, String col, int destRow, String destCol) {
        System.out.println("Invalid move: " + row + col + " to " + destRow + destCol);
    }

    public void printGameHelp() {
//        System.out.println("1. Move a piece");
//        System.out.println("2. Resign");
//        System.out.println("3. Offer a draw");
        System.out.println("4. Help");
        System.out.println("5. Quit");
        System.out.println("Please enter the number of your choice: ");
    }

    public void printLogoutFail() {
        System.out.println("Failed to logout. Please try again.");
        show_logged_in_options();
    }

    public void drawChessboard(ChessGame game) {
        ChessBoard board = game.getBoard();
        board.resetBoard();
        System.out.print(ERASE_SCREEN + SET_BG_COLOR_LIGHT_GREY + EMPTY + SET_TEXT_COLOR_WHITE);

        ArrayList<String> letters = new ArrayList<>(List.of("h", "g", "f", "e", "d", "c", "b", "a"));
        int start = 0;
        int stop = 7;
        int step =1;
        if (white) {
            Collections.reverse(letters);
            start = stop;
            stop = 0;
            step = -1;
        }
        for (String letter : letters) {
            System.out.print("\u2004\u2004" + letter + "\u2004\u2004\u2004");
        }
        System.out.println(EMPTY);
        for (int row = start; row * step <= stop; row = row + step) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + (row + 1)  + " ");
            for (int col = start; col * step <= stop; col = col + step) {
                ChessPiece piece = board.getPieceUsingRowCol(row, col);
                String pieceString = getString(piece);
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_CHESS_YELLOW + pieceString + RESET_BG_COLOR + SET_TEXT_COLOR_WHITE);
                } else {
                    System.out.print(SET_BG_COLOR_CHESSGREEN + pieceString + RESET_BG_COLOR+ SET_TEXT_COLOR_WHITE);
                }
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY +  " " + (row + 1) + " ");
        }
        System.out.print(EMPTY + SET_TEXT_COLOR_WHITE);
        for (String letter : letters) {
            System.out.print("\u2004\u2004" + letter + "\u2004\u2004\u2004");
        }
        System.out.println(EMPTY);
    }

    @NotNull
    private static String getString(ChessPiece piece) {
        String pieceString = EMPTY;
        String teamColor = SET_TEXT_COLOR_WHITE;
        if (piece != null) {
            ChessPiece.PieceType type = piece.getPieceType();
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                teamColor = SET_TEXT_COLOR_BLACK;
                if (type == ChessPiece.PieceType.KING) {
                    pieceString = teamColor + BLACK_KING;
                } else if (type == ChessPiece.PieceType.QUEEN) {
                    pieceString = teamColor + BLACK_QUEEN;
                } else if (type == ChessPiece.PieceType.BISHOP) {
                    pieceString = teamColor + BLACK_BISHOP;
                } else if (type == ChessPiece.PieceType.KNIGHT) {
                    pieceString = teamColor + BLACK_KNIGHT;
                } else if (type == ChessPiece.PieceType.ROOK) {
                    pieceString = teamColor + BLACK_ROOK;
                } else if (type == ChessPiece.PieceType.PAWN) {
                    pieceString = teamColor + BLACK_PAWN;
                }
            } else {
                if (type == ChessPiece.PieceType.KING) {
                    pieceString = teamColor + WHITE_KING;
                } else if (type == ChessPiece.PieceType.QUEEN) {
                    pieceString = teamColor + WHITE_QUEEN;
                } else if (type == ChessPiece.PieceType.BISHOP) {
                    pieceString = teamColor + WHITE_BISHOP;
                } else if (type == ChessPiece.PieceType.KNIGHT) {
                    pieceString = teamColor + WHITE_KNIGHT;
                } else if (type == ChessPiece.PieceType.ROOK) {
                    pieceString = teamColor + WHITE_ROOK;
                } else if (type == ChessPiece.PieceType.PAWN) {
                    pieceString = teamColor + WHITE_PAWN;
                }
            }
        }
        return pieceString;
    }

    public void printError(ErrorResponse error) {
        System.out.println(error.message());
    }

    public void printObserverJoinGameSuccess(int gameID) {
        System.out.println("Joined game " + gameID + " as an observer.");
    }
}
