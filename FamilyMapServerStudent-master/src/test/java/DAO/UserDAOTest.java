package DAO;

import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User bestUser;
    private UserDAO uDao;
    private boolean print = true;

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nUserDAO SetUp called");
        db = new Database();
        bestUser = new User("Gale_123A", "secret", "gale@mail.com", "Gale", "Smith",
                "f", "gale3");
        Connection conn = db.getConnection();
        uDao = new UserDAO(conn);
        uDao.clear();
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
        uDao.addUser(bestUser);
        User compareTest = uDao.checkUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        if (print)
            System.out.println("Test insertFail called");
        uDao.addUser(bestUser);
        assertThrows(DataAccessException.class, ()-> uDao.addUser(bestUser));
    }

    @Test
    public void checkUserPass() throws DataAccessException {
        if (print)
            System.out.println("Test checkUserPass called");
        uDao.addUser(bestUser);
        User compareTest = uDao.checkUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }
    @Test
    public void checkUserFail() throws DataAccessException {
        if (print)
            System.out.println("Test checkUserFail called");
        uDao.addUser(bestUser);
        User compareTest = uDao.checkUser("username");
        assertNull(compareTest);
    }

    @Test
    public void removePass() throws DataAccessException {
        if (print)
            System.out.println("Test removePass called");
        uDao.addUser(bestUser);
        uDao.removeUser(bestUser.getUsername());
        User compareTest = uDao.checkUser(bestUser.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        if (print)
            System.out.println("Test removeFail called");
        uDao.addUser(bestUser);
        uDao.removeUser(bestUser.getPersonID()+" ");
        User compareTest = uDao.checkUser(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        if (print)
            System.out.println("Test clearPass called");
        uDao.addUser(bestUser);
        uDao.clear();
        User compareTest = uDao.checkUser(bestUser.getUsername());
        assertNull(compareTest);
    }
}
