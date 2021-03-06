package server.dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.models.User;

import static org.junit.Assert.*;

/**
 * Created by bondd on 10/20/2017.
 */
public class UserDAOTest {

    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = new UserDAO();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUserDAO() throws Exception
    {
        createTable();
        clear();
        addUser();
        getUser();
        clear();
    }

    @Test
    public void createTable() throws Exception {
        userDAO.createTable();
    }

    public static String username = "fake username";
    public static String password = "fake password";
    public static String email = "fake email";
    public static String firstName = "fake first name";
    public static String lastName = "fake last name";
    public static String gender = "fake gender";
    public static String personID = "fake person ID";

    public void addUser() throws Exception {
        User u = new User(username, password, email, firstName, lastName, gender, personID);
        userDAO.addUser(u);
    }

    public void getUser() throws Exception {
        User u = userDAO.getUser(username);
        assertNotNull(u);
        assertEquals(u.getUsername(), username);
        assertEquals(u.getPassword(), password);
        assertEquals(u.getEmail(), email);
        assertEquals(u.getFirstName(), firstName);
        assertEquals(u.getLastName(), lastName);
        assertEquals(u.getGender(), gender);
        assertEquals(u.getPersonID(), personID);
    }

    @Test
    public void delete() throws Exception
    {
        createTable();
        clear();
        addUser();
        userDAO.delete(username);
        User u = userDAO.getUser(username);
        assertNull(u);
    }

    @Test
    public void clear() throws Exception
    {
        userDAO.clear();
        User u = userDAO.getUser(username);
        assertNull(u);
    }
}