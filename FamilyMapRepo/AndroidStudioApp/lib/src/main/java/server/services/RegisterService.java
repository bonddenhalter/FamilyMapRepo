package server.services;
import server.requests.RegisterRequest;
import server.results.RegisterResult;

/**
 * Class for servicing register requests
 */

public class RegisterService {

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     * @param r the RegisterRequest object
     * @return a RegisterResult object containing the auth token
     */
    public RegisterResult register(RegisterRequest r)
    {
        return null;
    }

}
