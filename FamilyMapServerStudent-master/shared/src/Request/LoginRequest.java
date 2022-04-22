package Request;

/**
 * Hold data for a request to login
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Default constructor
     */
    public LoginRequest() {
        new LoginRequest("","");
    }
    /**
     * LoginRequest constructor with existing values
     * @param username unique username for the user
     * @param password unique password for the user
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
