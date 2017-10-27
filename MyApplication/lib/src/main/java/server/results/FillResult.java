package server.results;

/**
 * The result of the Fill request
 */

public class FillResult {

    private String message;
    public static String SQLFailureMessage = "An error occured when trying to Fill data for the user";
    public static String negativeGenMessage = "Generations must be > 0";
    public static String unregisteredUserMessage = "The user is not registered in the database";

    public FillResult(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String setSuccessMessage(int peopleAdded, int eventsAdded)
    {
        String successMessage =  "Successfully added " + peopleAdded + " persons and " + eventsAdded + " events to the database.";
        setMessage(successMessage);
        return successMessage;
    }



}
