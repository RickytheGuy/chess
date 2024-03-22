package client;

import ServerFacade.ServerFacade;

import java.util.Scanner;

public class Repl {
    private ServerFacade sf;
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
        System.out.println("Please enter your choice: ");
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

    public void printRegisterFail() {
        System.out.println("User already exists. Please try again.");
        showLoginOptions();
    }
}
