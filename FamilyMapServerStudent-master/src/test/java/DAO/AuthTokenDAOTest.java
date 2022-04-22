package DAO;

import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db;
    private AuthToken testAuthToken;
    private AuthTokenDAO aDao;
    private boolean print = true;

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nAuthTokenDAO SetUp called");
        db = new Database();
        testAuthToken = new AuthToken("123456789012", "bilbo");
        Connection conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
        aDao.clear();
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
        aDao.insert(testAuthToken);
        AuthToken compareTest = aDao.checkToken(testAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(testAuthToken, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException {
        if (print)
            System.out.println("Test insertFail called");
        aDao.insert(testAuthToken);
        assertThrows(DataAccessException.class, ()-> aDao.insert(testAuthToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        if (print)
            System.out.println("Test findPass called");
        aDao.insert(testAuthToken);
        AuthToken compareTest = aDao.checkToken(testAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(testAuthToken, compareTest);
    }
    @Test
    public void findSearchKeyFail() throws DataAccessException {
        if (print)
            System.out.println("Test findSearchKeyFail called");
        aDao.insert(testAuthToken);
        AuthToken compareTest = aDao.checkToken("fakeAuth");
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        if (print)
            System.out.println("Test removePass called");
        aDao.insert(testAuthToken);
        aDao.removeToken(testAuthToken.getAuthToken());
        AuthToken compareTest = aDao.checkToken(testAuthToken.getAuthToken());
        assertNull(compareTest);
    }
    @Test
    public void removeFail() throws DataAccessException {
        if (print)
            System.out.println("Test removeFail called");
        aDao.insert(testAuthToken);
        aDao.removeToken(testAuthToken.getAuthToken()+"4s");
        AuthToken compareTest = aDao.checkToken(testAuthToken.getAuthToken());
        assertNotNull(compareTest);
        assertEquals(testAuthToken, compareTest);
    }
    @Test
    public void clearPass() throws DataAccessException {
        if (print)
            System.out.println("Test clearPass called");
        aDao.insert(testAuthToken);
        aDao.clear();
        AuthToken compareTest = aDao.checkToken(testAuthToken.getUsername());
        assertNull(compareTest);
    }
}
