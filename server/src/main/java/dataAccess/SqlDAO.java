package dataAccess;

import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
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
        return null;
    }

    @Override
    public void clearAll() {

    }

    @Override
    public int addGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public boolean gameExists(int gameID) {
        return false;
    }

    @Override
    public void addPlayerToGame(int i, String username, String s) throws DataAccessException {

    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void addAuth(String username) {

    }

    @Override
    public String getAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {

    }

    @Override
    public String getUserFromAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(String username, String password, String email) {

    }

    @Override
    public int size() {
        return 0;
    }
}
