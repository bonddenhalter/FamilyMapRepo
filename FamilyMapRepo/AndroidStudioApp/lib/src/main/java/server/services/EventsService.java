package server.services;
import server.results.EventsResult;

/**
 * Services an Events request
 */

public class EventsService {

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     * @param authToken the authToken of the current user
     * @return all events for all family members of the current user, contained in an EventsResult object
     */
    public EventsResult events(String authToken)
    {
        return null;
    }


}
