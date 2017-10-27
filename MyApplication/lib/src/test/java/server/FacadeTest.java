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
import server.models.Event;
import server.models.Person;
import server.models.User;
import server.requests.LoadRequest;
import server.requests.LoginRequest;
import server.results.ClearResult;
import server.results.EventResult;
import server.results.EventsResult;
import server.results.FillResult;
import server.results.LoadResult;
import server.results.LoginResult;

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
        personDAOTest.clear();

        UserDAOTest userDAOTest = new UserDAOTest();
        userDAOTest.setUp();
        userDAOTest.clear();
        userDAOTest.addUser(); //register a user
        String username = UserDAOTest.username; //use the username from UserDAOTest

        EventDAOTest eventDAOTest = new EventDAOTest();
        eventDAOTest.setUp();
        eventDAOTest.clear();

        //try a negative generation
        FillResult result = facade.fill(username, -1);
        assertEquals(result.getMessage(), FillResult.negativeGenMessage);

        //try unregistered user
        result = facade.fill("nonexistant username", 0);
        assertEquals(result.getMessage(), FillResult.unregisteredUserMessage);

        //give valid inputs
        //try with generations = 0
        result = facade.fill(username, 0);
        System.out.println("Result message: " + result.getMessage());
        assertNotNull(result.getMessage());
        assertNotEquals(result.getMessage(), FillResult.negativeGenMessage);
        assertNotEquals(result.getMessage(), FillResult.SQLFailureMessage);
        assertNotEquals(result.getMessage(), FillResult.unregisteredUserMessage);
        //user should be in database with no father, mother or spouse
        PersonDAO personDAO = new PersonDAO();
        List<Person> people = personDAO.getPeople(username);
        assertEquals(people.size(), 1);
        Person p = people.get(0);
        checkPerson(p, username, true, true);

            //try with generations blank
                //check that 4 generations exist
        personDAOTest.clear();
        eventDAOTest.clear();
        result = facade.fill(username);

        System.out.println("Result message: " + result.getMessage());
        assertNotNull(result.getMessage());
        assertNotEquals(result.getMessage(), FillResult.negativeGenMessage);
        assertNotEquals(result.getMessage(), FillResult.SQLFailureMessage);
        assertNotEquals(result.getMessage(), FillResult.unregisteredUserMessage);        UserDAO userDAO = new UserDAO();
        Person user = personDAO.getPerson(userDAO.getUser(username).getPersonID());
        checkTree(user, username, 4, personDAO, true);

        //there should have been at least 31 birth events and 15 marriage events created
        EventDAO eventDAO = new EventDAO();
        List<Event> events =  eventDAO.getEvents(username);
        System.out.println("Events created: " + events.size());
        assertTrue(events.size() >= 46);

            //try with generations == 3
                //check that generations exist
        personDAOTest.clear();
        eventDAOTest.clear();
        result = facade.fill(username, 3);
        System.out.println("Result message: " + result.getMessage());
        assertNotNull(result.getMessage());
        assertNotEquals(result.getMessage(), FillResult.negativeGenMessage);
        assertNotEquals(result.getMessage(), FillResult.SQLFailureMessage);
        assertNotEquals(result.getMessage(), FillResult.unregisteredUserMessage);        user = personDAO.getPerson(userDAO.getUser(username).getPersonID());
        checkTree(user, username, 3, personDAO, true);

        //there should have been at least 15 birth events and 7 marriages
        events =  eventDAO.getEvents(username);
        System.out.println("Events created: " + events.size());
        assertTrue(events.size() >= 22);

    }

    @Test
    public void load() throws Exception
    {
        //set up DAOs
        PersonDAOTest personDAOTest = new PersonDAOTest();
        personDAOTest.setUp();
        personDAOTest.clear();

        UserDAOTest userDAOTest = new UserDAOTest();
        userDAOTest.setUp();
        userDAOTest.clear();

        EventDAOTest eventDAOTest = new EventDAOTest();
        eventDAOTest.setUp();
        eventDAOTest.clear();

        //load null arrays
        LoadRequest req = new LoadRequest(null, null, null);
        LoadResult res = facade.load(req);
        assertEquals(res.getMessage(), LoadResult.NullFailureMessage);

        //load empty arrays
        req = new LoadRequest(new User[0], new Person[0], new Event[0]);
        res = facade.load(req);
        assertEquals(res.getMessage(), "Successfully added " + 0 + " users, " + 0 + " persons, and " + 0 + " events to the database.");

        //load nonempty arrays
        Person p = new Person(PersonDAOTest.personID, PersonDAOTest.descendant, PersonDAOTest.firstName, PersonDAOTest.lastName, PersonDAOTest.gender, PersonDAOTest.father, PersonDAOTest.mother, PersonDAOTest.spouse);
        Event e = new Event(EventDAOTest.eventID, EventDAOTest.descendant, EventDAOTest.person, EventDAOTest.latitude, EventDAOTest.longitude, EventDAOTest.country, EventDAOTest.city, EventDAOTest.eventType, EventDAOTest.year);
        User u = new User(UserDAOTest.username, UserDAOTest.password, UserDAOTest.email, UserDAOTest.firstName, UserDAOTest.lastName, UserDAOTest.gender, UserDAOTest.personID);

        Person[] pArray = {p};
        Event[] eArray = {e};
        User[] uArray = {u};

        LoadRequest newReq = new LoadRequest(uArray, pArray, eArray);
        res = facade.load(newReq);

        assertEquals(res.getMessage(), "Successfully added " + 1 + " users, " + 1 + " persons, and " + 1 + " events to the database.");

        //Load clears the database before loading, so adding the same arrays again should not result in an exception
        res = facade.load(newReq);
        assertNotEquals(res.getMessage(), LoadResult.SQLFailureMessage);
        userDAOTest.getUser();
        personDAOTest.getPerson();
        eventDAOTest.getEvent();

        //now if we load in empty arrays and query the database we shouldn't get anything
        res = facade.load(req);
        UserDAO userDAO = new UserDAO();
        EventDAO eventDAO = new EventDAO();
        PersonDAO personDAO = new PersonDAO();
        assertNull(userDAO.getUser(UserDAOTest.username));
        assertNull(eventDAO.getEvent(EventDAOTest.eventID));
        assertNull(personDAO.getPerson(PersonDAOTest.personID));
    }

    @Test
    public void login() throws Exception
    {
        //test username that doesn't exist
        UserDAOTest userDAOTest = new UserDAOTest();
        userDAOTest.setUp();
        userDAOTest.createTable();
        userDAOTest.clear();
        userDAOTest.addUser();

        LoginRequest req = new LoginRequest("nonexistant username", "whatever password");
        LoginResult result = facade.login(req);
        assertNotNull(result);
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertNull(result.getUserName());
        assertEquals(result.getErrorMessage(), LoginResult.userDoesNotExistMsg);

        //test correct username with incorrect password
        req.setUsername(UserDAOTest.username);
        result = facade.login(req);
        assertNotNull(result);
        assertNull(result.getAuthToken());
        assertNull(result.getPersonID());
        assertNull(result.getUserName());
        assertEquals(result.getErrorMessage(), LoginResult.userDoesNotExistMsg);

        //test correct username and password
        req.setPassword(UserDAOTest.password);
        result = facade.login(req);
        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        assertEquals(result.getPersonID(), UserDAOTest.personID);
        assertEquals(result.getUserName(), UserDAOTest.username);
        String authToken1 = result.getAuthToken();
        String personID1 = result.getPersonID();
        String userName1 = result.getUserName();

        //test again; should log in multiple times with different auth tokens
        result = facade.login(req);
        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        assertEquals(result.getPersonID(), UserDAOTest.personID);
        assertEquals(result.getUserName(), UserDAOTest.username);

        assertNotEquals(authToken1, result.getAuthToken()); //should be a different auth token
        assertEquals(personID1, result.getPersonID());
        assertEquals(userName1, result.getUserName());
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