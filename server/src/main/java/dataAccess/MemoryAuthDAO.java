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
        // Reversed ensures we get the latest auth token

        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).username().equals(username)) {
                return data.get(i).authToken();
            }
        }
        return null;
    }

    @Override
    public void removeAuth(String authToken) throws DataAccessException {
        for (AuthData auth : data) {
            if (auth.authToken().equals(authToken)) {
                data.remove(auth);
                return;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
