package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.User;
import Result.ClearResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClearServiceTest {
    private Database db;
    private Event testEvent;
    private User testUser;
    private AuthToken testAuthToken;
    private EventDAO eDao;
    private UserDAO uDao;
    private AuthTokenDAO aDao;
    private final boolean print = true;

    public ClearServiceTest() {}

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nClearServiceTest SetUp called");
        db = new Database();
        testUser = new User("bilbo","securePassword", "fake@email.com","bilbo","baggins","m","12345678");
        testAuthToken = new AuthToken("abcdefghijkl","bilbo");
        testEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        Connection conn = db.getConnection();

        uDao = new UserDAO(conn);
        uDao.clear();
        uDao.addUser(testUser);
        aDao = new AuthTokenDAO(conn);
        aDao.clear();
        aDao.insert(testAuthToken);
        eDao = new EventDAO(conn);
        eDao.clear();
        eDao.insert(testEvent);
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
    }

    @Test
    public void clearPass() throws DataAccessException {
        if (print)
            System.out.println("Test clearPass called");
        ClearService service = new ClearService();
        ClearResult compareResult = service.clear();
        ClearResult actualResult = new ClearResult("Clear succeeded.", true);
        assertEquals(actualResult, compareResult);
    }
}
