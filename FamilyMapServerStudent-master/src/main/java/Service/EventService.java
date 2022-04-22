package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Result.EventAllResult;
import Result.EventResult;

/**
 * Converts the Event request to an Event result
 */
public class EventService {

    /**
     * Default constructor
     */
    public EventService() {}

    /**
     * Performs the Event operation
     * @return EventResult
     */
    public EventAllResult event(String authtoken) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            AuthToken checkAuthToken = new AuthTokenDAO(db.getConnection()).checkToken(authtoken);
            if (checkAuthToken != null && authtoken.equals(checkAuthToken.getAuthToken())) {

                String username = new AuthTokenDAO(db.getConnection()).checkToken(authtoken).getUsername();
                int size = new EventDAO(db.getConnection()).getSubSize("associatedUsername", username);
                Event[] holdEvents = new EventDAO(db.getConnection()).findAll(size, "associatedUsername", username);
                if (holdEvents == null)
                    throw new Exception("No events associated with username found");

                db.closeConnection(true);
                return new EventAllResult(holdEvents);
            } else {
                throw new Exception("Unable to authenticate username");
            }
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new EventAllResult("Error: Event not returned");
        }
    }

    /**
     * Performs the Event operation for a specific Event
     * @param eventID unique ID for the Event
     * @return EventResult
     */
    public EventResult event(String eventID, String authToken) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            Event holdEvent = new EventDAO(db.getConnection()).find("eventID", eventID);
            if (holdEvent == null)
                throw new Exception();

            AuthToken corrUsername = new AuthTokenDAO(db.getConnection()).checkToken(authToken);
            if (!holdEvent.getAssociatedUsername().equals(corrUsername.getUsername()))
                throw new Exception();

            db.closeConnection(true);
            return new EventResult(holdEvent.getEventID(), holdEvent.getAssociatedUsername(),
                    holdEvent.getPersonID(), holdEvent.getLatitude(), holdEvent.getLongitude(), holdEvent.getCountry(),
                    holdEvent.getCity(), holdEvent.getEventType(), holdEvent.getYear());
        } catch (Exception ex) {
            System.out.println("\tException:" + ex);
            db.closeConnection(false);
            return new EventResult("Error: Event not returned");
        }
    }
}
