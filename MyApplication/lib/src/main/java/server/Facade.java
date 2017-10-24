package server;

import java.sql.SQLException;
import java.util.List;

import server.dataAccess.AuthDAO;
import server.dataAccess.EventDAO;
import server.dataAccess.PersonDAO;
import server.dataAccess.UserDAO;
import server.models.Auth;
import server.models.Event;
import server.models.Person;
import server.requests.LoadRequest;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;
import server.results.ClearResult;
import server.results.EventResult;
import server.results.EventsResult;
import server.results.FillResult;
import server.results.LoadResult;
import server.results.LoginResult;
import server.results.PeopleResult;
import server.results.PersonResult;
import server.results.RegisterResult;

/**
 * Created by bondd on 10/23/2017.
 */

public class Facade {

    AuthDAO authDAO;
    EventDAO eventDAO;
    PersonDAO personDAO;
    UserDAO userDAO;

    public Facade()
    {
        authDAO = new AuthDAO();
        eventDAO = new EventDAO();
        personDAO = new PersonDAO();
        userDAO = new UserDAO();
    }

    //I DON'T KNOW IF THIS SHOULD GO HERE BUT IT WILL CREATE THE DATABASE TABLES
    public void initDatabase()
    {
        try {
            authDAO.createTable();
            eventDAO.createTable();
            personDAO.createTable();
            userDAO.createTable();
        } catch (SQLException e)
        {
            System.out.println("AN EXCEPTION OCCURED WHILE TYRING TO CREATE THE DATABASE TABLES");
        }

    }


    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data. Assumes all the tables in the database exist
     * @return a message in a ClearResult object.
     */
    public ClearResult clear()
    {
        try {
            authDAO.clear();
            eventDAO.clear();
            personDAO.clear();
            userDAO.clear();
        }
        catch (SQLException e) { //return an error message instead of shutting down the server.
            return new ClearResult(ClearResult.failureMessage);
            //Do I need to somehow identify the source of this exception?
        }

        return new ClearResult(ClearResult.successMessage); //return the success message in a ClearResult object
    }

    /**
     * Returns the single Event object with the specified ID.
     * @param eventID the eventID of the event you want
     * @return The event info in an EventResult object. Null if there's an error.
     */
    public EventResult event(String eventID)
    {
        Event e = null;
        try
        {
            e = eventDAO.getEvent(eventID);
        }
        catch(SQLException ex)
        {
            return null; //the handler must display the event result error message
        }
        if (e == null)
            return null;
        return new EventResult(e);
    }

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     * @param authToken the authToken of the current user
     * @return all events for all family members of the current user, contained in an EventsResult object. Null if there's an error
     */
    public EventsResult events(String authToken)
    {
        EventsResult result = null;
        try
        {
            //get the current user from the auth token
            Auth auth = authDAO.getAuth(authToken);
            if (auth == null)
                return null;
            String user = auth.getUsername();

            //get events
            List<Event> events = eventDAO.getEvents(user);
            Event[] eventArray = events.toArray(new Event[0]);
            result = new EventsResult(eventArray);
        }
        catch (SQLException ex)
        {
            return null;
        }
        return result;
    }

    private String generatePersonID()
    {

    }

    private Person generatePersonData()
    {
        //call generatePersonID();
        //descendant is the user
        //pull names from the given file
    }

    /**
     *  Populates the server's database with generated data for the specified user name.
     *  If there is any data in the database already associated with the given user name, it is deleted
     * @param username must be a user already registered with the server
     * @param generations (optional) the number of generations of ancestors to be generated,
     *  and must be a non-negative integer (default is 4)
     * @return a message in a FillResult class.
     */
    public FillResult fill(String username, int generations)
    {
        //check if there is any data already associated with the user name
            //if there is, delete it
        //generate data
        //add it to the database
    }

    public FillResult fill(String username)
    {
        return fill(username, 4); //default for generations is 4
    }

    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     * @param r the LoadRequest object containing users, persons, and events to add to the database
     * @return a LoadResult object with a response message.
     */
    public LoadResult load(LoadRequest r)
    {
        return null;
    }

    /**
     * Logs in the user and returns an auth token.
     * @param r the LoginRequest object
     * @return the LoginResult object containing the auth token. Null if there's an error.
     */
    public LoginResult login(LoginRequest r)
    {
        return null;
    }

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     * @param token the authToken of the the current user
     * @return an array of Person objects in a PeopleResult object. Null if there's an error.
     */
    public PeopleResult people(String token)
    {
        return null;
    }

    /**
     * Returns the single Person object with the specified ID.
     * @param personID the personID of the person you want
     * @param Token the authToken of the person you want
     * @return the person with the specified ID. Null if there's an error.
     */
    public PersonResult person(String personID, String Token)
    {
        return null;
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     * @param r the RegisterRequest object
     * @return a RegisterResult object containing the auth token. Null if there's an error.
     */
    public RegisterResult register(RegisterRequest r)
    {
        return null;
    }
}
