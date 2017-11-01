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

    private AuthDAO authDAO;
    private EventDAO eventDAO;
    private PersonDAO personDAO;
    private UserDAO userDAO;

    private int numPeopleAdded = 0;
    private int numEventsAdded = 0;

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
    public EventResult event(String eventID, String authToken)
    {
        Event failEvent = new Event(null, null, null, null, null, null, null, null, null);
        EventResult result;
        try
        {
            Auth a = authDAO.getAuth(authToken);
            if (a == null) //if the auth doesn't exist
            {
                result = new EventResult(failEvent);
                result.setMessage(EventResult.invalidAuthTokenMsg);
                return result;
            }

            String username = a.getUsername();
            Event e = eventDAO.getEvent(eventID);
            if (e == null) //there is no event associated with the given eventID
            {
                result = new EventResult(failEvent);
                result.setMessage(EventResult.invalidEventIDMsg);
                return result;
            }

            if (!e.getDescendant().equals(username)) //if the event doesn't belong to the user
            {
                result = new EventResult(failEvent);
                result.setMessage(EventResult.userPermissionMsg);
                return result;
            }

            //success
            result = new EventResult(e);
            return result;


        }
        catch(SQLException ex)
        {
            result = new EventResult(failEvent);
            result.setMessage(EventResult.SQLFailureMessage);
            return result; //the handler must display the event result error message
        }
    }



    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     * @param authToken the authToken of the current user
     * @return all events for all family members of the current user, contained in an EventsResult object.
     */
    public EventsResult events(String authToken)
    {
        EventsResult result = null;
        try
        {
            //get the current user from the auth token
            Auth auth = authDAO.getAuth(authToken);
            if (auth == null)  //invalid auth token
            {
                result = new EventsResult(null);
                result.setMessage(EventsResult.invalidAuthMessage);
                return result; //return result with null data and an error message
            }
            String user = auth.getUsername();

            //get events
            List<Event> events = eventDAO.getEvents(user);
            Event[] eventArray = events.toArray(new Event[0]);
            result = new EventsResult(eventArray);
            result.setMessage(null);
            return result;
        }
        catch (SQLException ex)
        {
            result = new EventsResult(null);
            result.setMessage(EventsResult.SQLFailureMessage);
            return result;
        }
    }

    private String generateRandomID()
    {
        return UUID.randomUUID().toString();
    }

    //recursive function to fill in person data
    //adds ancestors to the database
    //if generations is > 0, the user will not be added to the databse
    private Person generatePersonData(String descendant, int generations, String gender, int marriageYear) throws SQLException
    {
        String personID = generateRandomID();//call generateRandomID();
        //descendant is the user
        RandomNames randNames = RandomNames.getInstance();
        //pull names from the given file
        String firstName = (gender.equals("Male")) ? randNames.getRandomMaleName() : randNames.getRandomFemaleName();
        String lastName = randNames.getRandomLastName();

        String father = "";
        String mother = "";
        String spouse = "";

        //add current person's birth event
        Event currentPersonBirth = generateEvent(descendant, personID, marriageYear - 20 , "Birth");
        eventDAO.addEvent(currentPersonBirth); //add user's birth event
        numEventsAdded++;

        //if they are really old, add a death event
        if (Integer.parseInt(currentPersonBirth.getYear()) < 1940)
        {
            int deathYear = genRandYear(marriageYear + 60);
            if (deathYear > 2017)
                deathYear = genRandYear(2017);
            if (deathYear < marriageYear)
                deathYear = (marriageYear + 2017) / 2;
            eventDAO.addEvent(generateEvent(descendant, personID, deathYear, "Death"));
            numEventsAdded++;
        }

        if (generations > 0)
        {
            int parentsMarriageYear = Integer.parseInt(currentPersonBirth.getYear());
            Person dad = generatePersonData(descendant, generations - 1, "Male", parentsMarriageYear); //call function to get father
            Person mom = generatePersonData(descendant, generations - 1, "Female", parentsMarriageYear); //call function to get mother

            //set father and mother as spouses for each other
            dad.setSpouse(mom.getPersonID());
            mom.setSpouse(dad.getPersonID());

            //set strings for their child
            father = dad.getPersonID();
            mother = mom.getPersonID();

            personDAO.addPerson(dad);
            numPeopleAdded++;
            personDAO.addPerson(mom);
            numPeopleAdded++;

            //add marriage events for parents
            Event dadMarriage = generateEvent(descendant, dad.getPersonID(), parentsMarriageYear, "Marriage");
            //mom's should be the same as Dad's except for person ID
            Event momMarriage = new Event(generateRandomID(), dadMarriage.getDescendant(), mom.getPersonID(), dadMarriage.getLatitude(), dadMarriage.getLongitude(), dadMarriage.getCountry(), dadMarriage.getCity(), dadMarriage.getEventType(), dadMarriage.getYear());
            eventDAO.addEvent(dadMarriage);
            numEventsAdded++;
            eventDAO.addEvent(momMarriage);
            numEventsAdded++;
        }

        Person me = new Person(personID, descendant, firstName, lastName, gender, father, mother, spouse);

       // if (generations == 0)
        //    personDAO.addPerson(me); //add self to database

        return me; //return new person
    }

    //returns a random year that is between the base year and 20 years previous
    private int genRandYear(int baseYear)
    {
        Random rand = new Random();
        int randOffset = rand.nextInt(21);
        return baseYear - randOffset;
    }

    private Event generateEvent(String descendant, String personID, int baseYear, String eventType)
    {
        String eventID = generateRandomID();
        RandomNames randomNames = RandomNames.getInstance();
        RandomNames.Location l = randomNames.getRandomLocation();
        String year = Integer.toString(genRandYear(baseYear));
        return new Event(eventID, descendant, personID, l.getLatitude(), l.getLongitude(), l.getCountry(), l.getCity(), eventType, year);
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
        numPeopleAdded = 0;
        numEventsAdded = 0;

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

            //add user's birth event
            Event userBirth = generateEvent(username, user.getPersonID(), 2000, "Birth");
            eventDAO.addEvent(userBirth); //add user's birth event
            numEventsAdded++;

            if (generations > 0) //create ancestors and add them to database
           {
               int parentsMarriageYear = Integer.parseInt(userBirth.getYear());
               Person dad = generatePersonData(username, generations - 1, "Male", parentsMarriageYear); //call function to get father
               Person mom = generatePersonData(username, generations - 1, "Female", parentsMarriageYear); //call function to get mother

               //set father and mother as spouses for each other
               dad.setSpouse(mom.getPersonID());
               mom.setSpouse(dad.getPersonID());

               //add father and mother to database
               personDAO.addPerson(dad);
               numPeopleAdded++;
               personDAO.addPerson(mom);
               numPeopleAdded++;

               //set strings for father and mother
               user.setFather(dad.getPersonID());
               user.setMother(mom.getPersonID());

               //add marriage events for parents
               Event dadMarriage = generateEvent(username, dad.getPersonID(), parentsMarriageYear, "Marriage");
               //mom's should be the same as Dad's except for person ID
               Event momMarriage = new Event(generateRandomID(), dadMarriage.getDescendant(), mom.getPersonID(), dadMarriage.getLatitude(), dadMarriage.getLongitude(), dadMarriage.getCountry(), dadMarriage.getCity(), dadMarriage.getEventType(), dadMarriage.getYear());
               eventDAO.addEvent(dadMarriage);
               numEventsAdded++;
               eventDAO.addEvent(momMarriage);
               numEventsAdded++;
           }
            personDAO.addPerson(user); //add user to the persons database
            numPeopleAdded++;

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

        //if all goes well, return success message
        FillResult fillResult= new FillResult("");
        fillResult.setSuccessMessage(numPeopleAdded, numEventsAdded);
        return fillResult;
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
        LoadResult result = new LoadResult();
        try
        {
            int numUsersAdded = 0;
            int numEventsAdded = 0;
            int numPeopleAdded = 0;

            clear(); //clear the database
            for (User u : r.getUsers())
            {
                userDAO.addUser(u);
                numUsersAdded++;
            }
            for (Person p : r.getPersons())
            {
                personDAO.addPerson(p);
                numPeopleAdded++;
            }
            for (Event e : r.getEvents())
            {
                eventDAO.addEvent(e);
                numEventsAdded++;
            }

            result.setSuccessMessage(numUsersAdded, numPeopleAdded, numEventsAdded);
        }
        catch (SQLException ex)
        {
            result.setMessage(LoadResult.SQLFailureMessage);
        }
        catch (NullPointerException nex)
        {
            result.setMessage(LoadResult.NullFailureMessage);
        }
        return result;
    }

    private int getTimeInSeconds()
    {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Logs in the user and returns an auth token.
     * @param r the LoginRequest object
     * @return the LoginResult object containing the auth token. Null if there's an error.
     */
    public LoginResult login(LoginRequest r)
    {
        LoginResult result;
        try
        {
            User u = userDAO.getUser(r.getUsername());
            if (u == null) {
                result = new LoginResult(null, null, null);
                result.setMessage(LoginResult.userDoesNotExistMsg);
            }
            else
            {
                if (r.getPassword().equals(u.getPassword()))  //verify correct password
                {
                    String authToken = generateRandomID();
                    int loginTimeInSeconds = getTimeInSeconds();
                    authDAO.addAuth(new Auth(authToken, u.getUsername(), loginTimeInSeconds));
                    result = new LoginResult(authToken, u.getUsername(), u.getPersonID());
                }
                else
                {
                    result = new LoginResult(null, null, null);
                    result.setMessage(LoginResult.incorrectPasswordMsg);
                }
            }
        }
        catch (SQLException ex)
        {
            result = new LoginResult(null, null, null);
            result.setMessage(LoginResult.SQLFailureMsg);
        }
        return result;
    }

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     * @param token the authToken of the the current user
     * @return an array of Person objects in a PeopleResult object. Null if there's an error.
     */
    public PeopleResult people(String token)
    {
        PeopleResult result;
        try
        {
            Auth a = authDAO.getAuth(token);
            if (a == null) //if the auth token doesn't exist
            {
                result = new PeopleResult(null);
                result.setMessage(PeopleResult.invalidAuthTokenMsg);
            }
            else //valid result
            {
                List<Person> relativeList = personDAO.getPeople(a.getUsername());
                result = new PeopleResult(relativeList.toArray(new Person[0]));
            }

        }
        catch (SQLException ex)
        {
            result = new PeopleResult(null);
            result.setMessage(PeopleResult.SQLFailureMsg);
        }
        return result;
    }

    /**
     * Returns the single Person object with the specified ID, so long as it belongs to the user
     * @param personID the personID of the person you want
     * @param token the authToken of the user
     * @return the person with the specified ID. Null if there's an error.
     */
    public PersonResult person(String personID, String token)
    {
        PersonResult result;
        try
        {
            Auth a = authDAO.getAuth(token);
            if (a == null) //if the auth doesn't exist
            {
                result = new PersonResult(null);
                result.setMessage(PersonResult.invalidTokenMsg);
                return result;
            }

            String username = a.getUsername();
            Person p = personDAO.getPerson(personID);
            if (p == null) //if there is no person with the given personID
            {
                result = new PersonResult(null);
                result.setMessage(PersonResult.invalidPersonIDMsg);
                return result;
            }

            if (!p.getDescendant().equals(username)) //if the descendant doesn't match the user, it doesn't belong to them
            {
                result = new PersonResult(null);
                result.setMessage(PersonResult.userPermissiongMsg);
                return result;
            }

            result = new PersonResult(p);
            return result;
        }
        catch (SQLException ex)
        {
            result = new PersonResult(null);
            result.setMessage(PersonResult.SQLFailureMsg);
            return result;
        }
    }

    private boolean isRequestValid(RegisterRequest r)
    {
        boolean valid = true;
        if (r.getPassword() == null || r.getEmail() == null || r.getFirstName() == null || r.getGender() == null ||
                r.getLastName() == null || r.getGender() == null)
        {
            valid = false;
        }
        if (r.getGender() != "Male" && r.getGender() != "Female")
            valid = false;

        //NEED TO CHECK VALID EMAIL, VALID USERNAME, VALID PASSWORD, OR ANYTHING ELSE?
        return valid;
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     * @param r the RegisterRequest object
     * @return a RegisterResult object containing the auth token. Null if there's an error.
     */
    public RegisterResult register(RegisterRequest r)
    {
        RegisterResult result;
        try
        {
            //make sure the request is complete
            if (!isRequestValid(r))
            {
                result = new RegisterResult(null, null, null);
                result.setMessage(RegisterResult.requestErrorMsg);
                return result;
            }

            //make sure the username is not taken
            if (userDAO.getUser(r.getUserName()) != null)
            {
                result = new RegisterResult(null, null, null);
                result.setMessage(RegisterResult.usernameTakenMsg);
                return result;
            }

            //create new user account
            String newPersonID = generateRandomID();
            User u = new User(r.getUserName(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(), r.getGender(), newPersonID);
            userDAO.addUser(u);

            //generate 4 generations of ancestor data
            FillResult fillResult = fill(r.getUserName(), 4);
            //check for errors
            if (fillResult.getMessage().equals(FillResult.negativeGenMessage) || fillResult.getMessage().equals(FillResult.SQLFailureMessage) || fillResult.getMessage().equals(FillResult.unregisteredUserMessage))
            {
                result = new RegisterResult(null, null, null);
                result.setMessage(fillResult.getMessage());
                return result;
            }

            //log user in
            LoginResult loginResult = login(new LoginRequest(r.getUserName(), r.getPassword()));
            //check for erros
            if (loginResult.getAuthToken() == null) //if any data points are null, there was a failure
            {
                result = new RegisterResult(null, null, null);
                result.setMessage(loginResult.getMessage());
                return result;
            }

            //success
            //return auth token
            result = new RegisterResult(loginResult.getAuthToken(), loginResult.getUserName(), newPersonID);
            return result;
        }
        catch (SQLException ex)
        {
            result = new RegisterResult(null, null, null);
            result.setMessage(RegisterResult.SQLFailureMsg);
            return result;
        }
    }
}
