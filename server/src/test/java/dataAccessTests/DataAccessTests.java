package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import requests.ChessResponse;
import requests.RegisterRequest;
import requests.RegisterResponse;
import services.ClearService;
import services.RegisterService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {
    @Test
    @Order(1)
    public void addUser() {
        // Setup
        SqlDAO db = new SqlDAO();
        db.addUser("testUser", "testPassword", "testEmail");
        UserData res = db.getUser("testUser");
        assert (res.username() == "testUser");
        assert (res.password() == "testPassword");
        assert (res.email() == "testEmail");
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
        assert (res.username() == "testUser");
        assert (res.password() == "testPassword");
        assert (res.email() == "testEmail");
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
    @Order(1)
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

        assert(db.listGames().size() == 0);
        assert(db.size() == 0);

    }


}
