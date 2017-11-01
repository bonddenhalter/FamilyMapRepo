package server.results;

import server.models.Person;

/**
 * Contains the information returned from a People request
 * Data will be null if there was an error.
 */

public class PeopleResult {

    private Person[] data; //an array of Person objects (as in PersonResult)
    private String message;
    public static String invalidAuthTokenMsg = "Error: Invalid auth token.";
    public static String SQLFailureMsg = "Error: SQL failure.";

    public PeopleResult(Person[] data) {
        this.data = data;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
