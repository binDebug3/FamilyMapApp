package Result;

/**
 * Hold data for results from register result
 */
public class RegisterResult {
    private String authtoken;
    private String username;
    private String personID;
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public RegisterResult() {
        new RegisterResult("","","");
        success = false;
    }
    /**
     * Failure constructor
     * @param message error message
     */
    public RegisterResult(String message) {
        success = false;
        authtoken = null;
        username = null;
        personID = null;
        this.message = message;
    }
    /**
     * Successful RegisterResult constructor
     * @param authtoken unique authtoken for user
     * @param username unique username for user
     * @param personID unique ID for user
     */
    public RegisterResult(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        message = "Registration successful";
        success = true;
    }

    public String getAuthToken() {
        return authtoken;
    }
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUserName() {
        return username;
    }
    public void setUserName(String userName) {
        this.username = userName;
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
}
