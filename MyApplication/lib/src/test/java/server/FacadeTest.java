package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.dataAccess.AuthDAOTest;
import server.dataAccess.EventDAO;
import server.dataAccess.EventDAOTest;
import server.results.ClearResult;
import server.results.EventResult;
import server.results.EventsResult;

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

    @Test
    public void fill() throws Exception {

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