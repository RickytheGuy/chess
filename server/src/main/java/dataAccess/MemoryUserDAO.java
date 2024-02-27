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
    public UserData getUser(String username) {
        for (UserData user: data_list) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void clearAll() {
        data_list = new ArrayList<>();
    }

    @Override
    public void addUser(String username, String password, String email) {
        data_list.add(new UserData(username, password, email));
    }
}
