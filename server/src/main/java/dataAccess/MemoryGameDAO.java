package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> data = new ArrayList<>();
    // white username: 0, black username: 1, game name: 2, game: 3

    @Override
    public void clearAll() {
        data = new ArrayList<>();
    }

    @Override
    public int addGame(String gameName) throws DataAccessException{
        for (GameData game : data) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Error: game already exists");
            }
        }
        data.add(new GameData(data.size(), "", "", gameName, new ChessGame()));
        return data.size();
    }
}
