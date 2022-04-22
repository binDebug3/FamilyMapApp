package Model;

import java.util.Random;

/**
 * A user registered with the server
 */

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * Default constructor
     */
    public User() {
        new User("","","","","","m");
    }
    /**
     * Constructor for a user without a personID
     * @param username Unique username for user
     * @param password User’s password
     * @param email User’s email address
     * @param firstName User’s first name
     * @param lastName User’s last name
     * @param gender User’s gender
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        personID = generatePersonID();
    }
    /**
     * Constructor for a user with existing information
     * @param username Unique username for user
     * @param password User’s password
     * @param email User’s email address
     * @param firstName User’s first name
     * @param lastName User’s last name
     * @param gender User’s gender
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {   return username;    }
    public void setUsername(String username) {        this.username = username;    }

    public String getPassword() {        return password;    }
    public void setPassword(String password) {        this.password = password;    }

    public String getEmail() {        return email;    }
    public void setEmail(String email) {        this.email = email;    }

    public String getFirstName() {        return firstName;    }
    public void setFirstName(String firstName) {        this.firstName = firstName;    }

    public String getLastName() {        return lastName;    }
    public void setLastName(String lastName) {        this.lastName = lastName;    }

    public String getGender() {        return gender;    }
    public void setGender(String gender) {        this.gender = gender;    }

    public String getPersonID() {        return personID;    }
    public void setPersonID(String personID) {        this.personID = personID;    }

    public void clear() {
        username = "";
        password = "";
        email = "";
        firstName = "";
        lastName = "";
        gender = "";
        personID = "";
    }
    /**
     * Determines if two User objects are equal
     * @return true if their private data members are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.username.equals(user.username) && this.password.equals(user.password) &&
                this.email.equals(user.email) && this.firstName.equals(user.firstName) &&
                this.lastName.equals(user.lastName) && this.gender.equals(user.gender) &&
                this.personID.equals(user.personID);
    }

    /**
     * Generate a unique personID for a user
     * @return the personID as a String
     */
    private String generatePersonID() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
