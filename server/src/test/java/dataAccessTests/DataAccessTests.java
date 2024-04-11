package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {

    @Test
    @Order(0)
    public void init() {
        SqlDAO db = new SqlDAO();
    }
    @Test
    @Order(1)
    public void addUser() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        UserData res = db.getUser("testUser");
        assert (res.username().equals("testUser"));
        assert (res.email().equals("testEmail"));
    }
    @Test
    @Order(2)
    public void addUserFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        try {
            db.addUser("", "aaaa", "testEmail");
        } catch (IllegalArgumentException e) {
            return;
        }
        assert (false);
    }

    @Test
    @Order(3)
    public void getUser() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        UserData res = db.getUser("testUser");
        assert (res.username().equals("testUser"));
        assert (res.email().equals("testEmail"));
    }
    @Test
    @Order(4)
    public void getUserFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        try {
            db.getUser("");
        } catch (IllegalArgumentException e) {
            return;
        }
        assert (false);
    }

    @Test
    @Order(5)
    public void clear() {
        // Setup
        SqlDAO db = new SqlDAO();
        try {
            db.addUser("testUser", "testPassword", "testEmail");
            db.addGame("testGame");
            db.addAuth("testUser");
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            return;
        }
        db.clearAll();

        assert(db.listGames().isEmpty());
        assert(db.size() == 0);

    }

    @Test
    @Order(6)
    public void addGame() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i = -1;
        try {
            i = db.addGame("testGame");
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
        assert (i != 0);

    }
    @Test
    @Order(7)
    public void addGameFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        try {
            db.addGame("");
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    @Order(8)
    public void gameExists() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i;
        try {
            i = db.addGame("testGame");
            assert (db.gameExists(i));
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }


    }
    @Test
    @Order(9)
    public void gameExistsFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i;
        try {
            i = db.addGame("testGame");
            assert (!db.gameExists(i + 1));
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    @Order(10)
    public void addPlayer() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i;
        try {
            i = db.addGame("testGame");
            assert (db.gameExists(i));
            db.addPlayerToGame(i, "testUser", "white");
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }
    @Test
    @Order(11)
    public void addPlayerFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i;
        try {
            i = db.addGame("testGame");
            assert (db.gameExists(i));
            try {
                db.addPlayerToGame(i, "", null);
            } catch (IllegalArgumentException e) {
                assert(e.toString().contains("Error"));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    @Order(12)
    public void listGames() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        int i = 0;
        try {
            i = db.addGame("testGame");
            assert (db.gameExists(i));
            db.addPlayerToGame(i, "testUser", "white");
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }

        assert (db.listGames().size() == 1);
        assert (db.listGames().get(0).gameID() == i);
        assert (db.listGames().get(0).gameName().equals("testGame"));
        assert (db.listGames().get(0).whiteUsername().equals("testUser"));
        assert (db.listGames().get(0).blackUsername() == null);
    }
    @Test
    @Order(13)
    public void listGamesFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        assert (db.listGames().isEmpty());
    }

    @Test
    @Order(14)
    public void addAuth() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            assert (!db.getAuth("testUser").isEmpty());
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }
    @Test
    @Order(15)
    public void addAuthFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            db.getAuth("123");
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }

    @Test
    @Order(16)
    public void getAuth() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            assert (!db.getAuth("testUser").isEmpty());
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }
    @Test
    @Order(17)
    public void getAuthFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            db.getAuth("");
        } catch (DataAccessException e) {
            assert (e.toString().contains("Error"));
            return;
        }
        assert (false);
    }

    @Test
    @Order(18)
    public void rmAuth() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            String auth = db.getAuth("testUser");
            db.removeAuth(auth);
            try {
                db.removeAuth(auth);
                assert (false);
            } catch (DataAccessException e) {
                assert (e.toString().contains("Error"));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }

    }
    @Test
    @Order(19)
    public void rmAuthFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            String auth = db.getAuth("testUser");
            db.removeAuth("");
        } catch (DataAccessException e) {
            assert (e.toString().contains("Error"));
        }
    }

    @Test
    @Order(20)
    public void getUserFAuth() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            String auth = db.getAuth("testUser");
            String user = db.getUserFromAuth(auth);
            assert (user.equals("testUser"));
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }

    }
    @Test
    @Order(21)
    public void getUserFAuthFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try {
            String auth = db.getAuth("testUser");
            String user = db.getUserFromAuth("123");

        } catch (DataAccessException e) {
            assert (e.toString().contains("Error"));
        }
    }

    @Test
    @Order(22)
    public void size() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addUser("testUser2", "testPassword", "testEmail");
        db.addUser("testUser3", "testPassword", "testEmail");
        assert(db.size() == 3);
    }
    @Test
    @Order(23)
    public void sizeFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.clearAll();
        assert (db.size() == 0);
    }

    @Test
    @Order(24)
    public void matchPass() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try
        {
            String user = db.passwordMatches("testUser", "testPassword");
            assert (user.equals("testUser"));
        } catch (DataAccessException e) {
            e.printStackTrace();
            assert (false);
        }
    }
    @Test
    @Order(25)
    public void matchPassFail() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        db.addAuth("testUser");
        try
        {
            String user = db.passwordMatches("testUser", "pass2");
            assert (!user.equals("testUser"));
        } catch (DataAccessException e) {
            assert (e.toString().contains("Error"));
        }
    }
}
