package dataAccess;

import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SqlDAO implements UserDAO, GameDAO, AuthDAO{

    public SqlDAO() {
        try {
        configureDatabase();
        } catch (DataAccessException ex) {
            System.out.println("Unable to configure database: " + ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username),
              INDEX(password)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 
            """,
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `chessGame` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            
            """,
            """
                CREATE TABLE IF NOT EXISTS  auth (
                `authID` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authID`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;"""
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
    @Override
    public UserData getUser(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT * FROM user WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Unable to get user: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to get user: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void clearAll() {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "DELETE FROM user";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
            sql = "DELETE FROM game";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
            sql = "DELETE FROM auth";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Unable to clear all: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to clear all: " + ex.getMessage());
        }

    }

    @Override
    public int addGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "INSERT INTO game (gameName) VALUES (?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.executeUpdate();
            }
            sql = "SELECT LAST_INSERT_ID()";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add game: %s", ex.getMessage()));
        }
        return 0;
    }

    @Override
    public boolean gameExists(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT * FROM game WHERE gameID = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Unable to check if game exists: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to check if game exists: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public void addPlayerToGame(int gameID, String username, String playerColor) throws DataAccessException {
        if (playerColor != null && !playerColor.isEmpty()) {
            try (var conn = DatabaseManager.getConnection()) {
                var sql = "SELECT " + playerColor + "Username FROM game WHERE gameID = ?";
                try (var preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, gameID);
                    try (var resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getString(1) != null) {
                            throw new DataAccessException("Error: Player color " + playerColor + " already assigned in the game.");
                        }
                    }
                }

                // No existing player assigned for the specified color, proceed with the update
                sql = "UPDATE game SET " + playerColor + "Username = ? WHERE gameID = ?";
                try (var preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setInt(2, gameID);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException ex) {
                throw new DataAccessException(String.format("Unable to add player to game: %s", ex.getMessage()));
            }
        } else {
            throw new IllegalArgumentException("Player color cannot be null or empty.");
        }
    }

    @Override
    public ArrayList<GameData> listGames() {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT * FROM game";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    var games = new ArrayList<GameData>();
                    while (resultSet.next()) {
                        games.add(new GameData(resultSet.getInt("gameID"), resultSet.getString("whiteUsername"), resultSet.getString("blackUsername"), resultSet.getString("gameName"), null));
                    }
                    return games;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Unable to list games: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to list games: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public void addAuth(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, UUID.randomUUID().toString());
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Unable to add auth: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to add auth: " + ex.getMessage());
        }
    }

    @Override
    public String getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT * FROM auth WHERE username = ? ORDER BY authID DESC LIMIT 1";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("authToken");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get auth: %s", ex.getMessage()));
        }
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "DELETE FROM auth WHERE authToken = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DataAccessException("No rows were affected by the deletion.");
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to remove auth: %s", ex.getMessage()));
        }
    }

    @Override
    public String getUserFromAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT * FROM auth WHERE authToken = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to get user from auth: %s", ex.getMessage()));
        }
        throw new DataAccessException(String.format("Unable to get user from auth"));
    }

    @Override
    public void addUser(String username, String password, String email) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                preparedStatement.setString(2, encoder.encode(password));
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Unable to add user: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to add user: " + ex.getMessage());
        }
    }

    @Override
    public int size() {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT COUNT(*) FROM user";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Unable to get size: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.println("Unable to get size: " + ex.getMessage());
        }
        return 0;
    }

    @Override
    public String passwordMatches(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var sql = "SELECT password FROM user WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        if (encoder.matches(password, resultSet.getString("password"))) {
                            return username;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to check password: %s", ex.getMessage()));
        }
        throw new DataAccessException("Error: User not found");
    }
}
