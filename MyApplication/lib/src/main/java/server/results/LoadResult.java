package server.results;

/**
 * Contains the result of the load request
 */

public class LoadResult {

    private String message;
    public static String SQLFailureMessage = "There was an error LOADING into the database.";
    public static String NullFailureMessage = "Loading failed, probably due to trying to load null arrays";


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccessMessage(int numUsers, int numPersons, int numEvents)
    {
        String msg = "Successfully added " + numUsers + " users, " + numPersons + " persons, and " + numEvents + " events to the database.";
        setMessage(msg);
    }

}
