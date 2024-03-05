package dataAccess;
import model.*;

public interface UserDAO {

    UserData getUser(String username);

    void clearAll();

    void addUser(String username, String password, String email);

    int size();

    String passwordMatches(String username, String password) throws DataAccessException;
}
