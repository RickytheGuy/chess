package dataAccess;
import model.*;

public interface UserDAO {
    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearAll() throws DataAccessException;

    void addUser(String username, String password, String email) throws DataAccessException;

    boolean userExists(String username);

    int size();
}
