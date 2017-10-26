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
        clear();
    }

    public void createTable() throws Exception {
        authDAO.createTable();
    }

    public static String testToken = "fakeToken";
    public static String testUsername = "fakeUsername";
    private int testLoginTime = 42;

    public void addAuth() throws Exception {
        Auth a = new Auth(testToken, testUsername, testLoginTime);
        authDAO.addAuth(a);
    }

    public void getAuth() throws Exception {
        Auth a = authDAO.getAuth(testToken);
        assertNotNull(a);
        assertEquals(testToken, a.getToken());
        assertEquals(testUsername, a.getUsername());
        assertEquals(testLoginTime, a.getLoginTime());

        //test nonexistent auth
        Auth a2 = authDAO.getAuth("nonexistent auth");
        assertNull(a2);
    }

    @Test
    public void delete() throws Exception
    {
        createTable();
        addAuth();
        authDAO.delete(testUsername);
        Auth a = authDAO.getAuth(testUsername);
        assertNull(a);
    }

    @Test
    public void clear() throws Exception
    {
        authDAO.clear();
        Auth a = authDAO.getAuth(testToken);
        assertNull(a);
    }

}