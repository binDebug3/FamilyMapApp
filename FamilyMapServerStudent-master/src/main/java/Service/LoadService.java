package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

/**
 * Converts the load request to a load result
 */
public class LoadService {

    /**
     * Default constructor
     */
    public LoadService() {}

    /**
     * Performs the load operation
     *
     * @param request LoadRequest object sent to the service class
     * @return LoadResult object
     */
    public LoadResult load(LoadRequest request) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            int numUsers = 0;
            int numPersons = 0;
            int numEvents = 0;
            //check for invalid input in each of the data type arrays
            if (request.getUsers() != null) {
                numUsers = request.getUsers().length;
                if (!checkUserValues(request.getUsers()))
                    throw new Exception("User data is invalid");
            }
            if (request.getPersons() != null) {
                numPersons = request.getPersons().length;
                if (!checkPersonValues(request.getPersons()))
                    throw new Exception("Person data is invalid");
            }
            if (request.getUsers() != null) {
                numEvents = request.getEvents().length;
                if (!checkEventValues(request.getEvents()))
                    throw new Exception("Event data is invalid");
            }
            //adds each element of each array to the corresponding table
            for (int i = 0; i < numUsers; i++) {
                new UserDAO(db.getConnection()).addUser(request.getUsers()[i]);
            }
            for (int i = 0; i < numPersons; i++) {
                new PersonDAO(db.getConnection()).addPerson(request.getPersons()[i]);
            }
            for (int i = 0; i < numEvents; i++) {
                new EventDAO(db.getConnection()).insert(request.getEvents()[i]);
            }

            db.closeConnection(true);
            return new LoadResult("Successfully added " + numUsers + " users, " + numPersons
                    + " persons, and " + numEvents + " events to the database.", true);
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new LoadResult("Error: Load failed", false);
        }
    }

    private boolean checkUserValues(User[] users) {
        for (User user : users) {
            if (user.getUsername().equals("") ||
                user.getPassword().equals("") ||
                user.getEmail().equals("") ||
                user.getFirstName().equals("") ||
                user.getLastName().equals("") ||
                user.getGender().equals("") ||
                user.getPersonID().equals("")
            ) { return false; }
        }
        return true;
    }
    private boolean checkPersonValues(Person[] persons) {
        for (Person person : persons) {
            if (person.getPersonID().equals("") ||
                person.getAssociatedUsername().equals("") ||
                person.getFirstName().equals("") ||
                person.getLastName().equals("") ||
                person.getGender().equals("")
            ) { return false;   }
        }
        return true;
    }
    private boolean checkEventValues(Event[] events) {
        for (Event event : events) {
            if (event.getEventID().equals("") ||
                event.getAssociatedUsername().equals("") ||
                event.getPersonID().equals("") ||
                event.getLatitude() > 180 ||
                event.getLatitude() < -180 ||
                event.getLongitude() > 180 ||
                event.getLongitude() < -180 ||
                event.getCountry().equals("") ||
                event.getCity().equals("") ||
                event.getEventType().equals("") ||
                event.getYear() > 2020
            ) { return false;   }
        }
        return true;
    }
}
