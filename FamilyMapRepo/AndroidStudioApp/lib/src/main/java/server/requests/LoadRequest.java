package server.requests;
import java.util.ArrayList;
import java.util.Arrays;

import server.models.*;


/**
 * Contains the information for a load request
 */

public class LoadRequest {

    private Arrays users;
    private Arrays persons;
    private Arrays events;

    public Arrays getUsers() {
        return users;
    }

    public void setUsers(Arrays users) {
        this.users = users;
    }

    public Arrays getPersons() {
        return persons;
    }

    public void setPersons(Arrays persons) {
        this.persons = persons;
    }

    public Arrays getEvents() {
        return events;
    }

    public void setEvents(Arrays events) {
        this.events = events;
    }


}
