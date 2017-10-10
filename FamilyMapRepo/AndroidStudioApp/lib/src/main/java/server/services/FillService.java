package server.services;
import server.requests.FillRequest;
import server.results.FillResult;

/**
 * Services a Fill request
 */

public class FillService {

    //USE FILLREQUEST CLASS???

    /**
     *  Populates the server's database with generated data for the specified user name.
     *  If there is any data in the database already associated with the given user name, it is deleted
     * @param username must be a user already registered with the server
     * @param generations (optional) the number of generations of ancestors to be generated,
     *  and must be a non-negative integer (default is 4)
     * @return
     */
    public FillResult fill(String username, int generations)
    {
        return null;
    }

    /**
     * {@code generations} defaults to 4, which results in 31 new persons
     * @see FillService#fill(String, int)
     */
    public FillResult fill(String username)
    {
       return fill(username, 4);
    }
}
