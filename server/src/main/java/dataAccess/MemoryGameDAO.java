package dataAccess;

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
}
