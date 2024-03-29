package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    void clearAll();

    int addGame(String gameName) throws DataAccessException;

    boolean gameExists(int gameID);

    void addPlayerToGame(int gameID, String username, String playerColor) throws DataAccessException;

    ArrayList<GameData> listGames();
}
