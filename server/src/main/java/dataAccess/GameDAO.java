package dataAccess;

import model.GameData;
import requests.ChessResponse;

import java.util.ArrayList;

public interface GameDAO {

    void clearAll();

    int addGame(String gameName) throws DataAccessException;

    boolean gameExists(int gameID);

    void addPlayerToGame(int i, String username, String s) throws DataAccessException;

    ArrayList<GameData> listGames();
}
