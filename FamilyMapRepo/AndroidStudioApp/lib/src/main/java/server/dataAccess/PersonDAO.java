package server.dataAccess;
import java.util.Arrays;

import server.models.Person;


/**
 * Interfaces with the Persons database table and Person objects.
 */

public class PersonDAO {

    /**
     * Adds a person row to the persons table
     * @param p The person object to add to the table
     */
    public void addPerson (Person p)
    {

    }

    /**
     * Retrieves a row from the persons table
     * @param personID The personID of the row you want from the table
     * @return The Person object that the row represents
     */
    public Person getPerson(String personID)
    {
        return null;
    }

    /**
     * Returns an array of the ancestors of the person with the given username
     * @param username the username to match with the "descendants" field in the persons database
     * @return all of the People objects that are ancestors of the given username
     */
    public Arrays getPeople (String username)
    {
        return null;
    }
}
