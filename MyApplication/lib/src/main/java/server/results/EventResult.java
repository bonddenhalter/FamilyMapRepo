package server.results;

import server.models.Event;

/**
 * Contains the results of an event request
 */

public class EventResult {

    private String descendant;
    private String eventID;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private String year;

    public static final String failureMessage = "There was an error retrieving the event from the database";

    public EventResult(Event e)
    {
        descendant = e.getDescendant();
        eventID = e.getEventID();
        personID = e.getPerson(); //MAKE SURE THIS MATCHES
        latitude = e.getLatitude();
        longitude = e.getLongitude();
        country = e.getCountry();
        city = e.getCity();
        eventType = e.getEventType();
        year = e.getYear();
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


}
