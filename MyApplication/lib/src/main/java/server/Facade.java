package server;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import server.dataAccess.AuthDAO;
import server.dataAccess.EventDAO;
import server.dataAccess.PersonDAO;
import server.dataAccess.UserDAO;
import server.models.Auth;
import server.models.Event;
import server.models.Person;
import server.models.User;
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
        return UUID.randomUUID().toString();
    }

   /* private String getRandomGender()
    {
        Random rand = new Random();
        int val = rand.nextInt(1); //get a 0 or 1
        if (val == 0)
            return "Male";
        else if (val == 1)
            return "Female";
        else
        {
            System.out.println("Random Gender ERROR");
            return null;
        }


    }*/ //this doesn't sense to have this

    //recursive function to fill in person data
    //adds ancestors to the database
    //if generations is > 0, the user will not be added to the databse
    private Person generatePersonData(String descendant, int generations, String gender) throws SQLException
    {
        String personID = generatePersonID();//call generatePersonID();
        //descendant is the user
        RandomNames randNames = new RandomNames();
        //pull names from the given file
        String firstName = (gender.equals("Male")) ? randNames.getRandomMaleName() : randNames.getRandomFemaleName();
        String lastName = randNames.getRandomLastName();

        String father = "";
        String mother = "";
        String spouse = "";

        if (generations > 0)
        {
            Person dad = generatePersonData(descendant, generations - 1, "Male"); //call function to get father
            Person mom = generatePersonData(descendant, generations - 1, "Female"); //call function to get mother

            //set father and mother as spouses for each other
            dad.setSpouse(mom.getPersonID());
            mom.setSpouse(dad.getPersonID());

            //set strings for their child
            father = dad.getPersonID();
            mother = mom.getPersonID();

            personDAO.addPerson(dad);
            personDAO.addPerson(mom);
        }

        Person me = new Person(personID, descendant, firstName, lastName, gender, father, mother, spouse);

       // if (generations == 0)
        //    personDAO.addPerson(me); //add self to database

        return me; //return new person
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

        try {
            if (generations < 0)
                throw new IllegalArgumentException(FillResult.negativeGenMessage) ;
            //make sure the user is registered in the server
            User u = userDAO.getUser(username);

            if (u == null)
                throw new NoSuchFieldException(FillResult.unregisteredUserMessage);

            //check if there is any data already associated with the user name
            //if there is, delete it
            //authDAO.delete(username); //I probably don't need to delete this
            eventDAO.delete(username);
            personDAO.delete(username);
           // userDAO.delete(username); //don't delete here; they need to be registered

           Person user = new Person("","","","","","","","");

            //set info for the user
            user.setPersonID(userDAO.getUser(username).getPersonID()); //we want the user's personID to be the same as in the user table
            user.setFirstName(userDAO.getUser(username).getFirstName());
            user.setLastName(userDAO.getUser(username).getLastName());
            user.setGender(userDAO.getUser(username).getGender());
            user.setSpouse("");
            user.setFather("");
            user.setMother("");
            user.setDescendant(username);
           if (generations > 0) //create ancestors and add them to database
           {
               Person dad = generatePersonData(username, generations - 1, "Male"); //call function to get father
               Person mom = generatePersonData(username, generations - 1, "Female"); //call function to get mother

               //set father and mother as spouses for each other
               dad.setSpouse(mom.getPersonID());
               mom.setSpouse(dad.getPersonID());

               //add father and mother to database
               personDAO.addPerson(dad);
               personDAO.addPerson(mom);

               //set strings for father and mother
               user.setFather(dad.getPersonID());
               user.setMother(mom.getPersonID());

           }
            personDAO.addPerson(user); //add user to the persons database

        }
        catch (SQLException ex)
        {
            return new FillResult(FillResult.SQLFailureMessage);
        }
        catch (IllegalArgumentException argEx)
        {
            return new FillResult(argEx.getMessage());
        }
        catch (NoSuchFieldException reg)
        {
            return new FillResult(reg.getMessage());
        }
        return new FillResult(FillResult.successMessage); //if all goes well
    }

    //generations is optional
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
