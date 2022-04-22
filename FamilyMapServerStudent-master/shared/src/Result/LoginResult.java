package Result;

/**
 * Hold data for results from login result
 */
public class LoginResult {
    private String authtoken;
    private String username;
    private String personID;
    private boolean success;
    private String message;

    /**
     * Default constructor
     */
    public LoginResult() {
        new LoginResult("","","");
        success = false;
        message = "Error: [description of the error]";
    }
    /**
     * Failure LoginResult constructor
     * @param message error message
     */
    public LoginResult(String message) {
        success = false;
        authtoken = null;
        username = null;
        personID = null;
        this.message = message;
    }
    /**
     * Successful LoginResult constructor
     * @param authtoken unique authtoken for the user
     * @param username unique username for the user
     * @param personID unique ID for the user
     */
    public LoginResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        message = "";
        success = true;
    }

    public String getAuthToken() {
        return authtoken;
    }
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResult that = (LoginResult) o;
        return success == that.success && !authtoken.equals(null) && username.equals(that.username) && personID.equals(that.personID) && message.equals(that.message);
    }
}
