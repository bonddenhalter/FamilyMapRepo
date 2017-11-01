package server.results;

import server.models.Event;

/**
 * Contains the results of an Events request
 */

public class EventsResult {

    private Event data[]; //an array of Event objects (as in EventResult)

    private String message;

    public static final String SQLFailureMessage = "A database error occured while retrieving the events.";
    public static final String invalidAuthMessage = "Error: invalid auth token.";


    public EventsResult(Event[] data) {
        this.data = data;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
