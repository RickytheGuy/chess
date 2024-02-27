package dataAccess;

public interface AuthDAO {
    void clearAll();

    void addAuth(String username);

    String getAuth(String username) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;
}
