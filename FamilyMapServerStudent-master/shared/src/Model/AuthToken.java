package Model;

import java.util.Objects;
import java.util.Random;

/**
 * Holds the data for an authToken from the AuthToken table
 */
public class AuthToken {
    private String authtoken;
    private String username;

    /**
     * Default constructor
     */
    public AuthToken() {
        new AuthToken("");
    }
    /**
     * Constructor without an existing authToken
     * @param username Unique username for user
     */
    public AuthToken(String username) {
        this.authtoken = generateAuthToken();
        this.username = username;
    }

    /**
     * Constructor with an existing authToken
     * @param authtoken unique authToken for the user
     * @param username unique username for the user
     */
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
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

    /**
     * Determines if two AuthToken objects are equal
     * @return true if their private data members are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authtoken, authToken1.authtoken) && Objects.equals(username, authToken1.username);
    }

    /**
     * Generate a unique authToken
     * @return return new authToken as a String
     */
    private String generateAuthToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
