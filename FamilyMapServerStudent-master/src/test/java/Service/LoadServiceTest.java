package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;

import Request.LoadRequest;
import Result.LoadResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    private Database db;
    private User testUser;
    private Person testPerson;
    private Event testEvent;
    private final boolean print = true;

    public LoadServiceTest() {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nLoadServiceTest SetUp called");
        db = new Database();
        testUser = new User("bilbo", "securePassword", "fake@email.com", "bilbo", "baggins", "m", "12345678");
        testPerson = new Person("Gale_123A", "Gale3000", "Gale", "Smith",
                "f", "john3", "mary4", "bob2");
        testEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        new UserDAO(db.getConnection()).clear();
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
        new UserDAO(db.getConnection()).clear();
        new PersonDAO(db.getConnection()).clear();
        new EventDAO(db.getConnection()).clear();
        db.closeConnection(true);
    }

    @Test
    public void loadPass() throws DataAccessException {
        if (print)
            System.out.println("Test loadPass called");
        User[] users = {testUser};
        Person[] persons = {testPerson};
        Event[] events = {testEvent};
        LoadRequest request = new LoadRequest(users, persons, events);
        LoadService service = new LoadService();
        LoadResult compareResult = service.load(request);
        LoadResult actualResult = new LoadResult("Successfully added 1 users, 1 persons, and 1 events to the database.", true);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void loadFailMissing() throws DataAccessException {
        if (print)
            System.out.println("Test loadFailMissing called");
        testUser.setEmail("");
        testPerson.setGender("");
        testEvent.setCity("");
        User[] users = {testUser};
        Person[] persons = {testPerson};
        Event[] events = {testEvent};
        LoadRequest request = new LoadRequest(users, persons, events);
        LoadService service = new LoadService();
        LoadResult compareResult = service.load(request);
        LoadResult actualResult = new LoadResult("Error: Load failed", false);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void loadFailInvalid() throws DataAccessException {
        if (print)
            System.out.println("Test loadFailInvalid called");
        testEvent.setLatitude(3000);
        User[] users = {testUser};
        Person[] persons = {testPerson};
        Event[] events = {testEvent};
        LoadRequest request = new LoadRequest(users, persons, events);
        LoadService service = new LoadService();
        LoadResult compareResult = service.load(request);
        LoadResult actualResult = new LoadResult("Error: Load failed", false);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
}
