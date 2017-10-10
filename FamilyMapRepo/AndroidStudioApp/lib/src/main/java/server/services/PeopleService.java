package server.services;
import server.results.PeopleResult;

/**
 * Services the People request
 */

public class PeopleService {

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     * @param token the authToken of the the current user
     * @return an array of Person objects
     */
    public PeopleResult people(String token)
    {
        return null;
    }
}
