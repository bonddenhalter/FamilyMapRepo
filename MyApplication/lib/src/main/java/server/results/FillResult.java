package server.results;

/**
 * The result of the Fill request
 */

public class FillResult {

    private String message;
    public static String SQLFailureMessage = "An error occured when trying to Fill data for the user";
    public static String negativeGenMessage = "Generations must be > 0";
    public static String unregisteredUserMessage = "The user is not registered in the database";
    public static String successMessage = "Ancestry data successfully Filled for the user";

    public FillResult(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
