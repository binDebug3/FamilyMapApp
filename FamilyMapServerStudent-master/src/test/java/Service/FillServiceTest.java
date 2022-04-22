package Service;

import DAO.*;
import Model.User;

import Result.FillResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private Database db;
    private User testUser;
    private final boolean print = true;

    public FillServiceTest() {}

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nFillServiceTest SetUp called");
        db = new Database();
        testUser = new User("bilbo", "securePassword", "fake@email.com", "bilbo", "baggins", "m", "12345678");

        new UserDAO(db.getConnection()).clear();
        new UserDAO(db.getConnection()).addUser(testUser);
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
        new UserDAO(db.getConnection()).clear();
        db.closeConnection(true);
    }

    @Test
    public void fillPass() throws DataAccessException {
        if (print)
            System.out.println("Test fillPass called");
        int numGen = 4;
        FillService service = new FillService();
        FillResult compareResult = service.fill(testUser.getUsername(), numGen);
        FillResult actualResult = new FillResult("Successfully added 31 persons and 92 events to the database.", true);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void fillPassNegGen() throws DataAccessException {
        if (print)
            System.out.println("Test fillPassNegGen called");
        int numGen = -300;
        FillService service = new FillService();
        FillResult compareResult = service.fill(testUser.getUsername(), numGen);
        FillResult actualResult = new FillResult("Successfully added 31 persons and 92 events to the database.", true);
        assertNotNull(compareResult);
        assertEquals(compareResult.isSuccess(), actualResult.isSuccess());
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }
    @Test
    public void fillFailUser() throws DataAccessException {
        if (print)
            System.out.println("Test fillFailUser called");
        int numGen = 4;
        testUser.setUsername("wrongUsername");
        FillService service = new FillService();
        FillResult compareResult = service.fill(testUser.getUsername(), numGen);
        FillResult actualResult = new FillResult("Error: Family data did not fill", false);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
}