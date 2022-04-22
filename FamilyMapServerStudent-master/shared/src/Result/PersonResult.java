package Result;

public class PersonResult {
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public PersonResult() {
        new PersonResult("","","","","","","","");
        success = false;
    }
    /**
     * Failure constructor
     * @param message with more details
     */
    public PersonResult(String message) {
        this.message = message;
        associatedUsername = null;
        personID = null;
        firstName = null;
        lastName = null;
        gender = null;
        fatherID = null;
        motherID = null;
        spouseID = null;
        success = false;
    }
    /**
     * Successful Person Result constructor
     * @param associatedUsername username of the associated user
     * @param personID unique ID of the person
     * @param firstName first name of the person
     * @param lastName last name of the person
     * @param gender gender of the person
     * @param fatherID ID for the father of the person
     * @param motherID ID for the mother of the person
     * @param spouseID ID for the spouse of the person
     */
    public PersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        message = "";
        success = true;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
        PersonResult that = (PersonResult) o;
        return success == that.success && associatedUsername.equals(that.associatedUsername) &&
                personID.equals(that.personID) && firstName.equals(that.firstName) && lastName.equals(that.lastName) &&
                gender.equals(that.gender) && fatherID.equals(that.fatherID) && motherID.equals(that.motherID) &&
                spouseID.equals(that.spouseID) && message.equals(that.message);
    }
}
