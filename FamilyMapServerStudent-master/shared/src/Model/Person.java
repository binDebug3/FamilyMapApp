package Model;

import java.util.Objects;

/**
 * A person in the user's family tree
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Default constructor
     */
    public Person() {
        new Person("","","","","m",null,null,null);
    }

    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = "";
        this.motherID = "";
        this.spouseID = "";
    }
    /**
     * Constructor for a person with existing information
     * @param personID Unique identifier for this person
     * @param associatedUsername Username of user to which this person belongs
     * @param firstName Person’s first name
     * @param lastName Person’s last name
     * @param gender Person’s gender
     * @param fatherID Person ID of person’s father
     * @param motherID Person ID of person’s mother
     * @param spouseID Person ID of person’s spouse
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {        return personID;    }
    public void setPersonID(String personID) {        this.personID = personID;    }

    public String getAssociatedUsername() {        return associatedUsername;    }
    public void setAssociatedUsername(String associatedUsername) {        this.associatedUsername = associatedUsername;    }

    public String getFirstName() {        return firstName;    }
    public void setFirstName(String firstName) {        this.firstName = firstName;    }

    public String getLastName() {        return lastName;    }
    public void setLastName(String lastName) {        this.lastName = lastName;    }

    public String getGender() {        return gender;    }
    public void setGender(String gender) {        this.gender = gender;    }

    public String getFatherID() {        return fatherID;    }
    public void setFatherID(String fatherID) {        this.fatherID = fatherID;    }

    public String getMotherID() {        return motherID;    }
    public void setMotherID(String motherID) {        this.motherID = motherID;    }

    public String getSpouseID() {        return spouseID;    }
    public void setSpouseID(String spouseID) {        this.spouseID = spouseID;    }

    /**
     * Clear all the values of the person
     */
    public void clear() {
        personID = "";
        associatedUsername = "";
        firstName = "";
        lastName = "";
        gender = "";
        fatherID = null;
        motherID = null;
        spouseID = null;
    }

    /**
     * Determines if two Person objects are equal
     * @return true if their private data members are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername, person.associatedUsername) &&
                Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) &&
                Objects.equals(gender, person.gender) && Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) && Objects.equals(spouseID, person.spouseID);
    }
}
