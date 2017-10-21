package server.dataAccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.models.Event;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Created by bondd on 10/20/2017.
 */
public class EventDAOTest {

    private EventDAO eventDAO;

    @Before
    public void setUp() throws Exception {
        eventDAO = new EventDAO();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEventDAO() throws Exception
    {
        createTable();
        addEvent(); //delete database file first!
        getEvent();
        addEvent2();
        getEvents();
    }


    @Test
    public void createTable() throws Exception {
        eventDAO.createTable();
    }

    private String eventID = "fake id";
    private String descendant = "fake descendant";
    private String person = "fake person";
    private double latitude = 42.42;
    private double longitude = 007.007;
    private String country = "fake country";
    private String city = "fake city";
    private String eventType = "fake event type";
    private String year = "fake year";

    private String eventID2 = "fake id 2";
    private String person2 = "fake person 2";
    private double latitude2 = 42.4202;
    private double longitude2 = 007.00702;
    private String country2 = "fake country 2";
    private String city2 = "fake city 2";
    private String eventType2 = "fake event type 2";
    private String year2 = "fake year 2";


    //delete database file first!
    public void addEvent() throws Exception {
        Event e = new Event(eventID, descendant, person, latitude, longitude, country, city, eventType, year);
        eventDAO.addEvent(e);
    }

    public void addEvent2() throws Exception
    {
        //using same descendant so I can test getEvents()
        Event e2 = new Event(eventID2, descendant, person2, latitude2, longitude2, country2, city2, eventType2, year2);
        eventDAO.addEvent(e2);
    }


    public void getEvent() throws Exception
    {
        Event e = eventDAO.getEvent(eventID);
        assertNotNull(e);
        assertEquals(e.getEventID(), eventID);
        assertEquals(e.getDescendant(), descendant);
        assertEquals(e.getPerson(), person);
        assertEquals(e.getLatitude(), latitude, 1); //what to use for max delta? (3rd parameter)
        assertEquals(e.getLongitude(), longitude, 1);
        assertEquals(e.getCountry(), country);
        assertEquals(e.getCity(), city);
        assertEquals(e.getEventType(), eventType);
        assertEquals(e.getYear(), year);

        //test nonexistent event
        Event e2 = eventDAO.getEvent("nonexistent ID");
        assertNull(e2);
    }


    public void getEvents() throws Exception
    {
        List<Event> events = eventDAO.getEvents(descendant);
        assertEquals(events.size(), 2);
        for (Event e : events)
        {
            assertThat(e.getEventID(), anyOf(is(eventID), is(eventID2))); //should match one of the two events we added
            if (e.getEventID().equals(eventID)) //first event
            {
                assertEquals(e.getEventID(), eventID);
                assertEquals(e.getDescendant(), descendant);
                assertEquals(e.getPerson(), person);
                assertEquals(e.getLatitude(), latitude, 1); //what to use for max delta? (3rd parameter)
                assertEquals(e.getLongitude(), longitude, 1);
                assertEquals(e.getCountry(), country);
                assertEquals(e.getCity(), city);
                assertEquals(e.getEventType(), eventType);
                assertEquals(e.getYear(), year);
            }
            else //must be second event
            {
                assertEquals(e.getEventID(), eventID2);
                assertEquals(e.getDescendant(), descendant);
                assertEquals(e.getPerson(), person2);
                assertEquals(e.getLatitude(), latitude2, 1); //what to use for max delta? (3rd parameter)
                assertEquals(e.getLongitude(), longitude2, 1);
                assertEquals(e.getCountry(), country2);
                assertEquals(e.getCity(), city2);
                assertEquals(e.getEventType(), eventType2);
                assertEquals(e.getYear(), year2);
            }
        }

        //test nonexistent descendant
        List<Event> events2 = eventDAO.getEvents("nonexistent descendant");
        assertTrue(events2.isEmpty());
    }

}