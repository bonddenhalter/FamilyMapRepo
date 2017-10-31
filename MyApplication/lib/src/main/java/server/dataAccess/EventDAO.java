package server.dataAccess;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import server.models.Event;

/**
 * Interfaces with the Events database table and Event objects.
 */

public class EventDAO {

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    final String connectionURL = "jdbc:sqlite:database.sqlite";

    /**
     * Create the Event table in the database
     * @throws SQLException
     */
    public void createTable() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String create  = "create table if not exists events (" +
                "eventID text not null primary key," +
                "descendant text not null," +
                "person text not null," +
                "latitude text not null," +
                "longitude text not null," +
                "country text not null," +
                "city text not null," +
                "eventType text not null," +
                "year text not null)";
        PreparedStatement stmt = connection.prepareStatement(create);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }


    /**
     * Adds an event row to the events table
     * @param e The event to add
     */
    public void addEvent(Event e) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);

        String add = "insert into events (eventID, descendant, person, latitude, longitude, country, city, eventType, year)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(add);
        stmt.setString(1, e.getEventID());
        stmt.setString(2, e.getDescendant());
        stmt.setString(3, e.getPerson());
        stmt.setString(4, e.getLatitude());
        stmt.setString(5, e.getLongitude());
        stmt.setString(6, e.getCountry());
        stmt.setString(7, e.getCity());
        stmt.setString(8, e.getEventType());
        stmt.setString(9, e.getYear());

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Retrieves a row from the events table
     * @param eventID the eventID of the row you want
     * @return the Event object that the row represents. Null if none found
     */
    public Event getEvent(String eventID) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from events where eventID = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, eventID); //search for the row with the matching eventID

        ResultSet results = stmt.executeQuery();
        if (!results.isBeforeFirst()) //if nothing found in database
            return null;
        String eventIDResult = results.getString(1);
        String descendant = results.getString(2);
        String person = results.getString(3);
        String latitude = results.getString(4);
        String longitude = results.getString(5);
        String country = results.getString(6);
        String city = results.getString(7);
        String eventType = results.getString(8);
        String year = results.getString(9);

        Event e = new Event(eventIDResult, descendant, person, latitude, longitude, country, city, eventType, year);
        results.close(); //DO I HAVE TO CLOSE EVERYTHING, INCLUDING THE CONNECTION?
        stmt.close();
        connection.close();
        return e;
    }

    /**
     * Returns a list of all events where the descendant matches the given username
     * @param username The username of the user for whom we're searching for all family events
     * @return list of all matching family events
     */
    public List<Event> getEvents(String username) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from events where descendant = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username); //search for the row with the matching descendant
        ResultSet results = stmt.executeQuery();

        List<Event> events = new ArrayList<>();
        while (results.next()) //next() returns false when there's no data left
        {
            String eventIDResult = results.getString(1);
            String descendant = results.getString(2);
            String person = results.getString(3);
            String latitude = results.getString(4);
            String longitude = results.getString(5);
            String country = results.getString(6);
            String city = results.getString(7);
            String eventType = results.getString(8);
            String year = results.getString(9);

            Event e = new Event(eventIDResult, descendant, person, latitude, longitude, country, city, eventType, year);
            events.add(e);
        }

        return events;
    }

    /**
     * Deletes all rows where the descendant matches the username
     * @param username the username to delete
     */
    public void delete(String username) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String clear = "delete from events where descendant = ?";
        PreparedStatement stmt = connection.prepareStatement(clear);
        stmt.setString(1, username); //search for the row with the matching username

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public void clear() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String clear = "delete from events";
        PreparedStatement stmt = connection.prepareStatement(clear);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
