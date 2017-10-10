package server.services;
import server.requests.LoadRequest;
import server.results.LoadResult;

/**
 * Services the Load request
 */

public class LoadService {

    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     * @param r the LoadRequest object containing users, persons, and events to add to the database
     * @return a LoadResult object with a response message
     */
    public LoadResult load(LoadRequest r)
    {
        return null;
    }
}
