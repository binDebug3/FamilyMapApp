package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;

import Result.EventAllResult;
import Result.EventResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private Database db;
    private EventDAO eDao;
    private Event event;
    private AuthTokenDAO aDao;
    private AuthToken authToken;
    private final boolean print = true;

    EventServiceTest() {}

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nEventServiceTest SetUp called");
        db = new Database();
        event = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        authToken = new AuthToken("authToken","Gale");
        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);
        aDao = new AuthTokenDAO(conn);
        eDao.clear();
        aDao.clear();
        eDao.insert(event);
        aDao.insert(authToken);
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);
        eDao.clear();
        db.closeConnection(true);
    }

    @Test
    public void eventSinglePass() throws DataAccessException {
        if (print)
            System.out.println("Test eventPass called");
        EventService service = new EventService();
        EventResult compareResult = service.event(event.getEventID(), authToken.getAuthToken());
        EventResult actualResult = new EventResult("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void eventAllPass() throws DataAccessException {
        if (print)
            System.out.println("Test eventAllPass called");
        EventService service = new EventService();
        EventAllResult compareResult = service.event(authToken.getAuthToken());
        Event[] events = {event};
        EventAllResult actualResult = new EventAllResult(events);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void eventFailID() throws DataAccessException {
        if (print)
            System.out.println("Test eventFailID called");
        event.setEventID("wrongID");
        EventService service = new EventService();
        EventResult compareResult = service.event(event.getEventID(), authToken.getAuthToken());
        EventResult actualResult = new EventResult("Error: Event not returned");
        assertNotNull(compareResult);
        assertNull(compareResult.getAssociatedUsername());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getEventID());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getCity());
        assertNull(compareResult.getEventType());
        assertNull(compareResult.getCountry());
        assertEquals(compareResult.getLatitude(), 0);
        assertEquals(compareResult.getLongitude(), 0);
        assertEquals(compareResult.getYear(), 0);
        assertEquals(compareResult.isSuccess(), false);
    }
    @Test
    public void eventFailUser() throws DataAccessException {
        if (print)
            System.out.println("Test eventFailUser called");
        authToken.setAuthToken("wrongUser");
        EventService service = new EventService();
        EventResult compareResult = service.event(event.getEventID(), authToken.getAuthToken());
        EventResult actualResult = new EventResult("Error: Event not returned");
        assertNotNull(compareResult);
        assertNull(compareResult.getAssociatedUsername());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getEventID());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getCity());
        assertNull(compareResult.getEventType());
        assertNull(compareResult.getCountry());
        assertEquals(compareResult.getLatitude(), 0);
        assertEquals(compareResult.getLongitude(), 0);
        assertEquals(compareResult.getYear(), 0);
        assertEquals(compareResult.isSuccess(), false);
    }
}
