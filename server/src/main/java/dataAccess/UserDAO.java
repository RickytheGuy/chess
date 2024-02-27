package dataAccess;
import model.*;

public interface UserDAO {
    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser();

    void clear();

}
