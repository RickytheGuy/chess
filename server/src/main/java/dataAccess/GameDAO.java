package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    void clearAll();

    int addGame(String gameName) throws DataAccessException;

    boolean gameExists(int gameID);

    void addPlayerToGame(int gameID, String username, String playerColor) throws DataAccessException;

    ArrayList<GameData> listGames();

    ChessGame getGame(Integer gameId);

    void removeGame(Integer gameID);

    void updateGame(Integer gameID, ChessGame game, String username, String playerColor);
}
