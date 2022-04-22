package Service;

import DAO.*;
import FakeData.LocationLoader;
import FakeData.NameLoader;
import Model.Event;
import Model.Person;
import FakeData.PersonGenerator;
import Result.FillResult;

/**
 * Converts the fill request to a fill result
 */
public class FillService {
    private final int DEF_GEN = 4;

    /**
     * Default constructor
     */
    public FillService() {

    }

    /**
     * Performs the fill operation
     * @return FillResult object
     */
    public FillResult fill(String username, int numGen) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            int[] numAdded = new int[2];
            if (new UserDAO(db.getConnection()).checkUser(username) != null) {
                if (numGen < 0)
                    numGen = DEF_GEN;
                deleteOldData(db, username);
                numAdded = performFill(db, username, numGen);
            } else {
                throw new Exception("Error: Incorrect username");
            }

            db.closeConnection(true);
            return new FillResult("Successfully added " + numAdded[0] + " persons and " + numAdded[1] +
                    " events to the database.", true);
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new FillResult("Error: Family data did not fill", false);
        }
    }

    /**
     * Perform the fill operation by calling a personGenerator object and updating the database
     * @param db database object that has already been opened
     * @param username username of the user
     * @param numGen number of generations requested
     * @return an integer array containing the number of person and event objects added to the database
     * @throws DataAccessException if there is an error accessing the database
     */
    private int[] performFill(Database db, String username, int numGen) throws DataAccessException {
        //loads all the fake name and location data
        NameLoader nameLoader = new NameLoader();
        LocationLoader locLoader = new LocationLoader();
        nameLoader.loadData();
        locLoader.loadData();
        //fills the database with the user's fake data
        PersonGenerator generator = new PersonGenerator(db, username, numGen);
        generator.generatePerson("m",  numGen);
        return new int[]{generator.getNumPersonsAdded(), generator.getNumEventsAdded()};
    }

    /**
     * Deletes all existing data associated with a specified username from the database
     * @param db database object that has already been opened
     * @param username username of the user
     * @throws DataAccessException if there is an error accessing the database
     */
    private void deleteOldData(Database db, String username) throws DataAccessException {
        //delete every row in the Persons table
        Person checkPerson = new PersonDAO(db.getConnection()).checkPerson("associatedUsername", username);
        while (checkPerson != null) {
            new PersonDAO(db.getConnection()).removePerson(checkPerson.getPersonID());
            checkPerson = new PersonDAO(db.getConnection()).checkPerson("associatedUsername", username);
        }
        //delete every row in the Events table
        Event checkEvent = new EventDAO(db.getConnection()).find("associatedUsername", username);
        while (checkEvent != null) {
            new EventDAO(db.getConnection()).removeEvent(checkEvent.getEventID());
            checkEvent = new EventDAO(db.getConnection()).find("associatedUsername", username);
        }
    }
}
