package Service;

import DAO.*;
import Model.AuthToken;
import Model.Person;
import Result.PersonAllResult;
import Result.PersonResult;

/**
 * Converts the person request to a person result
 */
public class PersonService {

    /**
     * Default constructor
     */
    public PersonService() {

    }

    /**
     * Performs the person operation for the entire table
     * @return PersonResult
     */
    public PersonAllResult person(String authToken) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            AuthToken checkAuthToken = new AuthTokenDAO(db.getConnection()).checkToken(authToken);
            if (checkAuthToken != null && authToken.equals(checkAuthToken.getAuthToken())) {

                String username = new AuthTokenDAO(db.getConnection()).checkToken(authToken).getUsername();
                int size = new PersonDAO(db.getConnection()).getSubSize("associatedUsername", username);
                Person[] holdPersons = new PersonDAO(db.getConnection()).checkAllData(size, "associatedUsername", username);
                if (holdPersons == null)
                    throw new Exception("No persons associated with requested username");

                db.closeConnection(true);
                return new PersonAllResult(holdPersons);
            } else {
                throw new Exception("Unable to authenticate username");
            }
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new PersonAllResult("Error: Persons not returned");
        }
    }

    /**
     * Performs the person operation for a specific person
     * @param personID unique ID for the person
     * @return PersonResult
     */
    public PersonResult person(String personID, String authToken) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            Person holdPerson = new PersonDAO(db.getConnection()).checkPerson("personID", personID);
            if (holdPerson == null)
                throw new Exception();
            AuthToken corrUsername = new AuthTokenDAO(db.getConnection()).checkToken(authToken);
            if (!holdPerson.getAssociatedUsername().equals(corrUsername.getUsername()))
                throw new Exception();

            db.closeConnection(true);
            return new PersonResult(holdPerson.getAssociatedUsername(), holdPerson.getPersonID(),
                    holdPerson.getFirstName(), holdPerson.getLastName(), holdPerson.getGender(), holdPerson.getFatherID(),
                    holdPerson.getMotherID(), holdPerson.getSpouseID());
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new PersonResult("Error: Person not returned");
        }
    }
}
