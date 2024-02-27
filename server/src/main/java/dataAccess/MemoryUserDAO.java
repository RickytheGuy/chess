package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    ArrayList<UserData> data_list = new ArrayList<>();
    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        for (UserData user: data_list) {
            if (Objects.equals(user.username(), username)) {
                throw new DataAccessException("Username already exists");
            }
        }

    }

    @Override
    public UserData getUser() {
        return null;
    }

    @Override
    public void clear() {

    }
}
