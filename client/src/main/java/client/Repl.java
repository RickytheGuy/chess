package client;

import ServerFacade.ServerFacade;
import model.GameData;

import java.util.ArrayList;
import java.util.Scanner;

public class Repl {
    private ServerFacade sf;
    private String token;
    public Repl (int port) {
        try {
            sf = new ServerFacade(port, this);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showLoginOptions() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("4. Help");
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
                }
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                sf.logout(token);
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
                    } else {
                        throw new Exception();
                    }
                    if (success) {
                        gameScreen();
                    }
                } else if (choice == 4) {
                    // Join as Observer
                    System.out.println("Enter the ID of the game you want to observe: ");
                    int gameID = scanner.nextInt();
                    boolean success = sf.joinGame(token, gameID, null);
                    if (success) {
                        gameScreen();
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
                scanner.nextLine();
            }
        }

    }

    public void printLoginFail() {
        System.out.println("Invalid username or password. Please try again.");
        show_logged_in_options();
    }

    public void show_logged_in_options() {
        System.out.println("1. Create Game");
        System.out.println("2. List Games");
        System.out.println("3. Join Game");
        System.out.println("4. Join as Observer");
        System.out.println("5. Logout");
        System.out.println("6. Help");
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
        System.out.println("Games: ");
        for (GameData game : games) {
            System.out.println("ID: " + game.gameID() + " Name: " + game.gameName());
        }
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

    public void gameScreen() {
        while (true) {
            // Get the game state
            // Print the game state
            // Get the move
            // Send the move
        }
    }

    public void printLogoutFail() {
        System.out.println("Failed to logout. Please try again.");
        show_logged_in_options();
    }
}
