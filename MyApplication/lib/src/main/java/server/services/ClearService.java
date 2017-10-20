package server.services;
import server.results.ClearResult;

/**
 * Services the Clear request
 */

public class ClearService {

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data.
     * @return a message in a ClearResult object.
     */
    public ClearResult clear()
    {
        return null;
    }
}
