package server.results;

import server.models.Event;

/**
 * Contains the results of an Events request
 */

public class EventsResult {

    private Event data[]; //an array of Event objects (as in EventResult)

    public static final String failureMessage = "An error occured while retrieving the events.";


    public EventsResult(Event[] data) {
        this.data = data;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
