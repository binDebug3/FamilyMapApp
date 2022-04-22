package Service;

import DAO.*;
import Result.ClearResult;

/**
 * Converts the clear request to a clear result
 */
public class ClearService {

    /**
     * Default constructor
     */
    public ClearService() {}

    /**
     * Clears the data
     * @return ClearResult object
     */
    public ClearResult clear() throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            new UserDAO(db.getConnection()).clear();
            new AuthTokenDAO(db.getConnection()).clear();
            new EventDAO(db.getConnection()).clear();
            new PersonDAO(db.getConnection()).clear();

            db.closeConnection(true);
            return new ClearResult("Clear succeeded.", true);
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new ClearResult("Error: Clear failed", false);
        }
    }
}
