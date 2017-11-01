package server.requests;

/**
 * Contains the information for a Login request
 */

public class LoginRequest {

    private String userName;
    private String password;

    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
