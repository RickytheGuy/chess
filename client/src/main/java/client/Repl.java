package client;

import ServerFacade.ServerFacade;

import java.util.Scanner;

public class Repl {
    private ServerFacade sf;
    public Repl (int port) {
        sf = new ServerFacade(port);
    }

    public void loginScreen() {
        System.out.println("Welcome to the game!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("Please enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int choice = scanner.nextInt();
            if (choice == 1) {
                System.out.println("Enter your username: ");
                String username = scanner.next();
                System.out.println("Enter your password: ");
                String password = scanner.next();
                System.out.println("Enter your email: ");
                String email = scanner.next();
                String token = sf.register(username, password, email);
            } else if (choice == 2) {
                String token = sf.login();
            } else if (choice == 3) {
                System.out.println("Goodbye!");
                sf.logout();
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
