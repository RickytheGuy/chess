package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    ArrayList<UserData> data_list = new ArrayList<>();

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

    @Override
    public int size() {
        return data_list.size();
    }

    @Override
    public String passwordMatches(String username, String password) throws DataAccessException {
        for (UserData user: data_list) {
            if (Objects.equals(user.username(), username)) {
                if (Objects.equals(user.password(), password)) {
                    return user.username();
                }
            }
        }
        throw new DataAccessException("Error: User not found");
    }


}


