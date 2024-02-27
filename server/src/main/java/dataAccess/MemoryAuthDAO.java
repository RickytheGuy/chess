package dataAccess;
import model.*;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private ArrayList<AuthData> data = new ArrayList<>();
    @Override
    public void clearAll() {
        data = new ArrayList<>();
    }

    @Override
    public void addAuth(String username) {
        data.add(new AuthData(UUID.randomUUID().toString(), username));
    }

    @Override
    public String getAuth(String username) {
        for (AuthData auth : data) {
            if (auth.username().equals(username)) {
                return auth.authToken();
            }
        }
        return null;
    }
}
