package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import server.dataAccess.AuthDAOTest;
import server.dataAccess.EventDAO;
import server.dataAccess.EventDAOTest;
import server.dataAccess.PersonDAO;
import server.dataAccess.PersonDAOTest;
import server.dataAccess.UserDAO;
import server.dataAccess.UserDAOTest;
import server.models.Person;
import server.models.User;
import server.results.ClearResult;
import server.results.EventResult;
import server.results.EventsResult;
import server.results.FillResult;

import static org.junit.Assert.*;
import static server.dataAccess.EventDAOTest.city;
import static server.dataAccess.EventDAOTest.city2;
import static server.dataAccess.EventDAOTest.country;
import static server.dataAccess.EventDAOTest.country2;
import static server.dataAccess.EventDAOTest.descendant;
import static server.dataAccess.EventDAOTest.eventID;
import static server.dataAccess.EventDAOTest.eventID2;
import static server.dataAccess.EventDAOTest.eventType;
import static server.dataAccess.EventDAOTest.eventType2;
import static server.dataAccess.EventDAOTest.latitude;
import static server.dataAccess.EventDAOTest.latitude2;
import static server.dataAccess.EventDAOTest.longitude;
import static server.dataAccess.EventDAOTest.longitude2;
import static server.dataAccess.EventDAOTest.person;
import static server.dataAccess.EventDAOTest.person2;
import static server.dataAccess.EventDAOTest.year;
import static server.dataAccess.EventDAOTest.year2;

/**
 * Created by bondd on 10/23/2017.
 */
public class FacadeTest {

    Facade facade;
    @Before
    public void setUp() throws Exception {
        facade = new Facade();
        facade.initDatabase(); //create the tables in the database
    }

    @After
    public void tearDown() throws Exception {
        facade.clear();
    }

    @Test
    public void clear() throws Exception {
        ClearResult result = facade.clear();
        assertNotNull(result);
        assertEquals(result.getMessage(), result.successMessage);
    }

    @Test
    public void event() throws Exception {
        EventDAOTest daoTest = new EventDAOTest();
        daoTest.setUp();
        daoTest.clear();
        daoTest.addEvent(); //add the test event to the database
        EventResult result = facade.event(eventID); //query that test event

        assertNotNull(result);
        assertEquals(result.getEventID(), eventID); //the EventResult object should hold everything from the test event
        assertEquals(result.getDescendant(), descendant);
        assertEquals(result.getPersonID(), person);
        assertEquals(result.getLatitude(), latitude, 1); //what to use for max delta? (3rd parameter)
        assertEquals(result.getLongitude(), longitude, 1);
        assertEquals(result.getCountry(), country);
        assertEquals(result.getCity(), city);
        assertEquals(result.getEventType(), eventType);
        assertEquals(result.getYear(), year);

        //test a nonexistent event
        result = facade.event("nonexistent eventID");
        assertNull(result);

    }

    @Test
    public void events() throws Exception {
        EventDAOTest eventDaoTest = new EventDAOTest();
        eventDaoTest.setUp();
        eventDaoTest.clear();
        eventDaoTest.addEvent();
        eventDaoTest.addEvent2();

        AuthDAOTest authDaoTest = new AuthDAOTest();
        authDaoTest.setUp();
        authDaoTest.clear();
        authDaoTest.addAuth();

        EventsResult result = facade.events(AuthDAOTest.testToken);
        assertNotNull(result);
        assertEquals(result.getData().length, 2);

        //check first event
        assertEquals(result.getData()[0].getEventID(), eventID);
        assertEquals(result.getData()[0].getDescendant(), descendant);
        assertEquals(result.getData()[0].getPerson(), person);
        assertEquals(result.getData()[0].getLatitude(), latitude, 1); //what to use for max delta? (3rd parameter)
        assertEquals(result.getData()[0].getLongitude(), longitude, 1);
        assertEquals(result.getData()[0].getCountry(), country);
        assertEquals(result.getData()[0].getCity(), city);
        assertEquals(result.getData()[0].getEventType(), eventType);
        assertEquals(result.getData()[0].getYear(), year);

        //check second event
        assertEquals(result.getData()[1].getEventID(), eventID2);
        assertEquals(result.getData()[1].getDescendant(), descendant);
        assertEquals(result.getData()[1].getPerson(), person2);
        assertEquals(result.getData()[1].getLatitude(), latitude2, 1); //what to use for max delta? (3rd parameter)
        assertEquals(result.getData()[1].getLongitude(), longitude2, 1);
        assertEquals(result.getData()[1].getCountry(), country2);
        assertEquals(result.getData()[1].getCity(), city2);
        assertEquals(result.getData()[1].getEventType(), eventType2);
        assertEquals(result.getData()[1].getYear(), year2);

        //try a nonexistent one
        result = facade.events("nonexistent token");
        assertNull(result);
    }

    //end should be true if the person should not have father, mother, or spouse
    private void checkPerson(Person p, String username, boolean end, boolean firstGen)
    {
//        assertNotNull(p);
//        assertNotNull(p.getFirstName());
//        assertNotNull(p.getLastName());
//        assertNotNull(p.getPersonID());
//        assertNotNull(p.getGender());
        assertEquals(p.getDescendant(), username);

        assertNotEquals(p.getFirstName(), "");
        assertNotEquals(p.getLastName(), "");
        assertNotEquals(p.getPersonID(), "");
        assertNotEquals(p.getGender(), "");
        assertNotEquals(p.getDescendant(), "");

        if (end)
        {
//            assertNull(p.getSpouse());
//            assertNull(p.getFather());
//            assertNull(p.getMother());
            if (firstGen)
                assertEquals(p.getSpouse(), "");
            else
                assertNotEquals(p.getSpouse(), "");
            assertEquals(p.getFather(), "");
            assertEquals(p.getMother(), "");
        }
        else
        {
//            assertNotNull(p.getSpouse());
//            assertNotNull(p.getFather());
//            assertNotNull(p.getMother());
            if (firstGen)
                assertEquals(p.getSpouse(), "");
            else
                assertNotEquals(p.getSpouse(), "");
            assertNotEquals(p.getFather(), "");
            assertNotEquals(p.getMother(), "");
        }
    }

    //checks the specified number of generations. On the first call, person should be the user, who is the bottom of the tree
    private void checkTree(Person person, String username, int generations, PersonDAO personDAO, boolean firstGen) throws Exception
    {
        boolean end = (generations == 0);
        checkPerson(person, username, end, firstGen); //check the current person
        if (generations > 0) //more generations to go
        {
            Person dad = personDAO.getPerson(person.getFather());
            Person mom = personDAO.getPerson(person.getMother());
            checkTree(dad, username, generations - 1, personDAO, false);
            checkTree(mom, username, generations - 1, personDAO, false);
        }

    }


    @Test
    public void fill() throws Exception
    {
        PersonDAOTest personDAOTest = new PersonDAOTest();
        personDAOTest.setUp();
        //personDAOTest.createTable();
        personDAOTest.clear();

        UserDAOTest userDAOTest = new UserDAOTest();
        userDAOTest.setUp();
        //userDAOTest.createTable();
        userDAOTest.clear();
        userDAOTest.addUser(); //register a user
        String username = UserDAOTest.username; //use the username from UserDAOTest

        //try a negative generation
        FillResult result = facade.fill(username, -1);
        assertEquals(result.getMessage(), FillResult.negativeGenMessage);

        //try unregistered user
        result = facade.fill("nonexistant username", 0);
        assertEquals(result.getMessage(), FillResult.unregisteredUserMessage);

        //give valid inputs
        //try with generations = 0
        result = facade.fill(username, 0);
        assertEquals(result.getMessage(), FillResult.successMessage);
        //user should be in database with no father, mother or spouse
        PersonDAO personDAO = new PersonDAO();
        List<Person> people = personDAO.getPeople(username);
        assertEquals(people.size(), 1);
        Person p = people.get(0);
        checkPerson(p, username, true, true);

            //try with generations blank
                //check that 4 generations exist
        personDAOTest.clear();
        result = facade.fill(username);
        assertEquals(result.getMessage(), FillResult.successMessage);
        UserDAO userDAO = new UserDAO();
        Person user = personDAO.getPerson(userDAO.getUser(username).getPersonID());
        checkTree(user, username, 4, personDAO, true);

            //try with generations > 0
                //check that generations exist
        personDAO.clear();
        result = facade.fill(username, 3);
        assertEquals(result.getMessage(), FillResult.successMessage);
        user = personDAO.getPerson(userDAO.getUser(username).getPersonID());
        checkTree(user, username, 3, personDAO, true);
    }

    @Test
    public void load() throws Exception {

    }

    @Test
    public void login() throws Exception {

    }

    @Test
    public void people() throws Exception {

    }

    @Test
    public void person() throws Exception {

    }

    @Test
    public void register() throws Exception {

    }

}