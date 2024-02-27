package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    void clearAll();

    int addGame(String gameName) throws DataAccessException;
}
