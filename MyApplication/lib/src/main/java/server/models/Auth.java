package server.models;

/**
 * Represents a row of the Auth database table
 */

public class Auth {

    private String token;
    private String username;
    private int loginTime;

    public Auth(String token, String username, int loginTime) {
        this.token = token;
        this.username = username;
        this.loginTime = loginTime;
    }

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
