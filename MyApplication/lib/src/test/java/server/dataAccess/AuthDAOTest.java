package server.dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import server.models.Auth;

/**
 * Created by bondd on 10/20/2017.
 */
public class AuthDAOTest {

    private AuthDAO authDAO;
    @Before
    public void setUp() throws Exception {
        authDAO = new AuthDAO();
    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    //Be sure to delete the database.sqlite file first
    public void testAuthDAO() throws Exception
    {
        createTable();
        addAuth();
        getAuth();
    }

    public void createTable() throws Exception {
        authDAO.createTable();
    }

    String testToken = "fakeToken";
    String testUsername = "fakeUsername";
    int testLoginTime = 42;

    public void addAuth() throws Exception {
        Auth a = new Auth(testToken, testUsername, testLoginTime);
        authDAO.addAuth(a);
    }

    public void getAuth() throws Exception {
        Auth a = authDAO.getAuth(testToken);
        assertEquals(testToken, a.getToken());
        assertEquals(testUsername, a.getUsername());
        assertEquals(testLoginTime, a.getLoginTime());
    }

}