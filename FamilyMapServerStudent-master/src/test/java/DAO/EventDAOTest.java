package DAO;

import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private EventDAO eDao;
    private boolean print = true;

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nEventDAO SetUp called");
        db = new Database();
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        if (print)
            System.out.println("Test insertPass called");
        eDao.insert(bestEvent);
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException {
        if (print)
            System.out.println("Test insertFail called");
        eDao.insert(bestEvent);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        if (print)
            System.out.println("Test findPass called");
        eDao.insert(bestEvent);
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }
    @Test
    public void findSearchKeyFail() throws DataAccessException {
        if (print)
            System.out.println("Test findSearchKeyFail called");
        eDao.insert(bestEvent);
        Event compareTest = eDao.find("eventID", "fakeID");
        assertNull(compareTest);
    }

    @Test
    public void findByEventPass() throws DataAccessException {
        if (print)
            System.out.println("Test findByEventPass called");
        eDao.insert(bestEvent);
        Event compareTest = eDao.findByEvent("Biking_Around", "eventID", bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }
    @Test
    public void findByEventFail() throws DataAccessException {
        if (print)
            System.out.println("Test findByEventPass called");
        eDao.insert(bestEvent);
        Event compareTest = eDao.findByEvent("birth", "eventID", bestEvent.getEventID());
        assertNull(compareTest);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    @Test
    public void findAllPass() throws DataAccessException {
        if (print)
            System.out.println("Test findAllPass called");
        eDao.insert(bestEvent);
        int size = 1;
        Event[] actual = new Event[]{bestEvent};
        Event[] compareTest = eDao.findAll(size, "eventID", bestEvent.getEventID());
        assertNotNull(compareTest);
        for (int i = 0; i < size; i++) {
            assertEquals(actual[i], compareTest[i]);
        }
    }
    @Test
    public void findAllSizeFail() throws DataAccessException {
        if (print)
            System.out.println("Test findAllFail called");
        eDao.insert(bestEvent);
        Event[] compareTest = eDao.findAll(0, "eventID", bestEvent.getEventID());
        assertNull(compareTest);
        assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    @Test
    public void updateDeathPass() throws DataAccessException {
        if (print)
            System.out.println("Test updateDeathPass called");
        bestEvent.setEventType("death");
        eDao.insert(bestEvent);
        int year = 2020;
        eDao.updateDeath(bestEvent.getPersonID(), year);
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        bestEvent.setEventType("Biking_Around");
        assertNotNull(compareTest);
        assertNotEquals(bestEvent, compareTest);
        assertEquals(year, compareTest.getYear());
    }
    @Test
    public void updateDeathFail() throws DataAccessException {
        if (print)
            System.out.println("Test updateDeathFail called");
        eDao.insert(bestEvent);
        int year = 2020;
        eDao.updateDeath("fakePersonID", year);
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertEquals(2016, compareTest.getYear());
    }

    @Test
    public void getSubSizePass() throws DataAccessException {
        if (print)
            System.out.println("Test getSubSizePass called");
        eDao.insert(bestEvent);
        int size = eDao.getSubSize("eventID", bestEvent.getEventID());
        assertEquals(size, 1);
    }
    @Test
    public void getSubSizeFail() throws DataAccessException {
        if (print)
            System.out.println("Test getSubSizePass called");
        eDao.insert(bestEvent);
        int size = eDao.getSubSize("eventID", "fakeID");
        assertEquals(size, 0);
    }

    @Test
    public void removeEventPass() throws DataAccessException {
        if (print)
            System.out.println("Test removePass called");
        eDao.insert(bestEvent);
        eDao.removeEvent(bestEvent.getEventID());
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertNull(compareTest);
    }
    @Test
    public void removeEventFail() throws DataAccessException {
        if (print)
            System.out.println("Test removeFail called");
        eDao.insert(bestEvent);
        eDao.removeEvent(" ");
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        if (print)
            System.out.println("Test clearpass called");
        eDao.clear();
        Event compareTest = eDao.find("eventID", bestEvent.getEventID());
        assertNull(compareTest);
    }
}
