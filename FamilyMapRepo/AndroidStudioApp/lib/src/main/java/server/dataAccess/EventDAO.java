package server.dataAccess;
import java.util.Arrays;

import server.models.Event;

/**
 * Interfaces with the Events database table and Event objects.
 */

public class EventDAO {

    /**
     * Adds an event row to the events table
     * @param e The event to add
     */
    public void addEvent(Event e)
    {

    }

    /**
     * Retrieves a row from the events table
     * @param eventID the eventID of the row you want
     * @return the Event object that the row represents
     */
    public Event getEvent(String eventID)
    {
        return null;
    }

    /**
     * Returns an array of all events where the descendant matches the given username
     * @param username The username of the user for whom we're searching for all family events
     * @return array of all family events
     */
    public Arrays getEvents(String username)
    {
        return null;
    }
}
