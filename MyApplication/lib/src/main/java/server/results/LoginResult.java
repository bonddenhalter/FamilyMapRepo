package server.results;

/**
 * Contains the information that is returned from a login request
 * If there is an error, authToken, userName, and personID will be null, and message will contain a message.
 * If error, authToken, and userName are not null, there was not an error
 */

public class LoginResult {

    private String authToken;
    private String userName;
    private String personID;
    private String message;
    public static String userDoesNotExistMsg = "Login Error: The username does not exist.";
    public static String incorrectPasswordMsg = "Login Error: Incorrect Password.";
    public static String SQLFailureMsg = "Login Error: a SQL error occured.";
    public static String invalidRequestMsg = "Login Error: Request property missing or has invalid value.";

    public LoginResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
