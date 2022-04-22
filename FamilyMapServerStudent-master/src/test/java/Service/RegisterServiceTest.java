package Service;

import DAO.*;
import Model.AuthToken;
import Model.User;
import Request.RegisterRequest;

import Result.RegisterResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private Database db;
    private UserDAO uDao;
    private AuthTokenDAO aDao;
    private User user;
    private AuthToken authToken;
    private RegisterRequest request;
    private final boolean print = true;

    RegisterServiceTest() {}

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nRegisterServiceTest SetUp called");
        db = new Database();
        user = new User("bilbo","securePassword", "fake@email.com","bilbo","baggins","m","12345678");
        authToken = new AuthToken("abcdefghijkl","bilbo");
        request = new RegisterRequest("bilbo","securePassword", "fake@email.com","bilbo","baggins","m");

        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        uDao.clear();
        //uDao.addUser(user);
        aDao = new AuthTokenDAO(conn);
        aDao.clear();
        aDao.insert(authToken);
        db.closeConnection(true);
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Close called");
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        aDao = new AuthTokenDAO(conn);
        uDao.clear();
        aDao.clear();
        db.closeConnection(true);
    }

    @Test
    public void registerPass() throws DataAccessException {
        if (print)
            System.out.println("Test registerPass called");
        RegisterRequest request = new RegisterRequest("bilbo","securePassword", "fake@email.com",
                "bilbo","baggins","m");
        RegisterService service = new RegisterService();
        RegisterResult compareResult = service.register(request);
        RegisterResult actualResult = new RegisterResult("abcdefghijkl","bilbo", "12345678");
        assertNotNull(compareResult);
        assertNotNull(compareResult.getAuthToken());
        assertNotNull(compareResult.getPersonID());
        assertEquals(compareResult.getUserName(), actualResult.getUserName());
        assertEquals(compareResult.isSuccess(), actualResult.isSuccess());
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }

    @Test
    public void registerFailMissing() throws DataAccessException {
        if (print)
            System.out.println("Test registerFailMissing called");
        RegisterRequest request = new RegisterRequest("bilbo","", "fake@email.com",
                "bilbo","baggins","m");
        RegisterService service = new RegisterService();
        RegisterResult compareResult = service.register(request);
        RegisterResult actualResult = new RegisterResult("Error: Registration failed");
        assertNotNull(compareResult);
        assertNotNull(compareResult.getAuthToken());
        assertNotNull(compareResult.getPersonID());
        assertNotNull(compareResult.getUserName());
        assertNotNull(compareResult.isSuccess());
        assertNotNull(compareResult.getMessage());
    }
}