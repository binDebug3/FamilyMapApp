package Result;

import Model.Person;

import java.util.Arrays;

public class PersonAllResult {
    private Person[] data;
    private String message;
    private boolean success;

    /**
     * Default constructor
     */
    public PersonAllResult() {
        new PersonAllResult("");
    }
    /**
     * Failure constructor
     */
    public PersonAllResult(String message) {
        this.message = message;
        data = null;
        success = false;
    }
    /**
     * Successful Person constructor
     * @param person array to hold all data in the database
     */
    public PersonAllResult(Person[] person) {
        this.data = person;
        message = "";
        success = true;
    }

    public Person[] getPersons() {
        return data;
    }
    public void setPersons(Person[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonAllResult that = (PersonAllResult) o;
        return success == that.success && Arrays.equals(data, that.data) && message.equals(that.message);
    }
}
