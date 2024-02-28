package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> data = new ArrayList<>();
    // white username: 0, black username: 1, game name: 2, game: 3

    @Override
    public void clearAll() {
        data = new ArrayList<>();
    }

    @Override
    public int addGame(String gameName) throws DataAccessException{
        if (gameName == null || gameName.equals("")) {
            throw new DataAccessException("Error: bad request");
        }
        for (GameData game : data) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Error: game already exists");
            }
        }
        data.add(new GameData(data.size() + 1, null, null, gameName, new ChessGame()));
        return data.size();
    }

    @Override
    public boolean gameExists(int gameID) {
        for (GameData game : data) {
            if (game.gameId() == gameID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addPlayerToGame(int gameID, String username, String playerColor) throws DataAccessException {
        for (GameData game : data) {
            if (game.gameId() == gameID) {
                if (playerColor.equals("WHITE")) {
                    if (game.whiteUsername() == null) {
                        data.add(new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game()));
                        data.remove(game);
                        return;
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                } else {
                    if (game.blackUsername() == null) {
                        data.add(new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game()));
                        data.remove(game);
                        return;
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                }
            }
        }
        throw new DataAccessException("Error: game does not exist");
    }

    @Override
    public ArrayList<GameData> listGames() {
        return data;
    }
}
