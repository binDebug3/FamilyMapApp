package Service;

import DAO.*;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Database db;
    private UserDAO uDao;
    private AuthTokenDAO aDao;
    private User user;
    private AuthToken authToken;
    private final boolean print = true;

    public LoginServiceTest() {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nLoginServiceTest SetUp called");
        db = new Database();
        user = new User("bilbo","securePassword", "fake@email.com","bilbo","baggins","m","12345678");
        authToken = new AuthToken("abcdefghijkl","bilbo");
        Connection conn = db.getConnection();

        uDao = new UserDAO(conn);
        uDao.clear();
        uDao.addUser(user);
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
        //uDao.removeUser(user.getUsername());
        //aDao.removeToken(authToken.getAuthToken());
        uDao = new UserDAO(conn);
        aDao = new AuthTokenDAO(conn);
        uDao.clear();
        aDao.clear();
        db.closeConnection(true);
    }

    @Test
    public void loginPass() throws DataAccessException {
        if (print)
            System.out.println("Test loginPass called");
        LoginRequest request = new LoginRequest("bilbo", "securePassword");
        LoginService service = new LoginService();
        LoginResult compareResult = service.login(request);
        LoginResult actualResult = new LoginResult("abcdefghijkl","bilbo", "12345678");
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }

    @Test
    public void loginFailPassword() throws DataAccessException {
        if (print)
            System.out.println("Test loginFailPassword called");
        LoginRequest request = new LoginRequest("bilbo", "wrongPassword");
        LoginService service = new LoginService();
        LoginResult compareResult = service.login(request);
        LoginResult actualResult = new LoginResult("Error: Invalid username/password");
        assertNotNull(compareResult);
        assertEquals(compareResult.getUsername(), actualResult.getUsername());
        assertEquals(compareResult.getAuthToken(), actualResult.getAuthToken());
        assertEquals(compareResult.getPersonID(), actualResult.getPersonID());
        assertEquals(compareResult.isSuccess(), actualResult.isSuccess());
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }
    @Test
    public void loginFailUsername() throws DataAccessException {
        if (print)
            System.out.println("Test loginFailPassword called");
        LoginRequest request = new LoginRequest("", "wrongPassword");
        LoginService service = new LoginService();
        LoginResult compareResult = service.login(request);
        LoginResult actualResult = new LoginResult("Error: Invalid username/password");
        assertNotNull(compareResult);
        assertEquals(compareResult.getUsername(), actualResult.getUsername());
        assertEquals(compareResult.getAuthToken(), actualResult.getAuthToken());
        assertEquals(compareResult.getPersonID(), actualResult.getPersonID());
        assertEquals(compareResult.isSuccess(), actualResult.isSuccess());
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }
}
