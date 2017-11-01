package server.results;

/**
 * Contains the info from the response to a register request
 */

public class RegisterResult {

    private String authToken;
    private String userName;
    private String personID;

    private String message;

    public static String SQLFailureMsg = "There was a SQL error when processing the REGISTER request.";
    public static String usernameTakenMsg = "Error: the username is already taken by another user.";
    public static String requestErrorMsg = "Error: the request has a missing or invalid value.";


    public RegisterResult(String authToken, String userName, String personID) {
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
