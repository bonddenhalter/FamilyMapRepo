package server.results;

/**
 * Contains the results of the clear request
 */

public class ClearResult {

    private String message;

    public static final String successMessage = "Clear succeeded.";
    public static final String failureMessage = "An error occured when trying to clear the database.";

    public ClearResult(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
