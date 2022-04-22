package Service;

import DAO.*;
import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import Model.Person;
import Result.PersonAllResult;
import Result.PersonResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PersonServiceTest {
    private Database db;
    private PersonDAO pDao;
    private AuthTokenDAO aDao;
    private Person person;
    private AuthToken authToken;
    private final boolean print = true;

    PersonServiceTest() {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nPersonServiceTest SetUp called");
        db = new Database();
        person = new Person("12345678", "bilbo", "bilbo", "baggins", "m", "fid123", "mid123", "sid123");
        authToken = new AuthToken("abcdefghijkl", "bilbo");
        //request = new RegisterRequest("bilbo", "securePassword", "fake@email.com", "bilbo", "baggins", "m");

        Connection conn = db.getConnection();
        pDao = new PersonDAO(conn);
        pDao.clear();
        pDao.addPerson(person);
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
        pDao = new PersonDAO(conn);
        aDao = new AuthTokenDAO(conn);
        pDao.clear();
        aDao.clear();
        db.closeConnection(true);
    }

    @Test
    public void personSinglePass() throws DataAccessException {
        if (print)
            System.out.println("Test personPass called");
        PersonService service = new PersonService();
        PersonResult compareResult = service.person(person.getPersonID(), authToken.getAuthToken());
        PersonResult actualResult = new PersonResult("bilbo", "12345678", "bilbo",
                "baggins", "m", "fid123", "mid123", "sid123");
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void personAllPass() throws DataAccessException {
        if (print)
            System.out.println("Test personAllPass called");
        PersonService service = new PersonService();
        PersonAllResult compareResult = service.person(authToken.getAuthToken());
        Person[] persons = {person};
        PersonAllResult actualResult = new PersonAllResult(persons);
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
    @Test
    public void personFailID() throws DataAccessException {
        if (print)
            System.out.println("Test personFailID called");
        person.setPersonID("wrongID");
        PersonService service = new PersonService();
        PersonResult compareResult = service.person(person.getPersonID(), authToken.getAuthToken());
        PersonResult actualResult = new PersonResult("Error: Person not returned");
        assertNotNull(compareResult);
        assertNull(compareResult.getAssociatedUsername());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getFatherID());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getGender());
        assertNull(compareResult.getFirstName());
        assertNull(compareResult.getLastName());
        assertNull(compareResult.getMotherID());
        assertNull(compareResult.getSpouseID());
        assertEquals(compareResult.isSuccess(), false);
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }
    @Test
    public void personFailUser() throws DataAccessException {
        if (print)
            System.out.println("Test personFailUser called");
        authToken.setAuthToken("wrongUser");
        PersonService service = new PersonService();
        PersonResult compareResult = service.person(person.getPersonID(), authToken.getAuthToken());
        PersonResult actualResult = new PersonResult("Error: Person not returned");
        assertNotNull(compareResult);
        assertNull(compareResult.getAssociatedUsername());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getFatherID());
        assertNull(compareResult.getPersonID());
        assertNull(compareResult.getGender());
        assertNull(compareResult.getFirstName());
        assertNull(compareResult.getLastName());
        assertNull(compareResult.getMotherID());
        assertNull(compareResult.getSpouseID());
        assertEquals(compareResult.isSuccess(), false);
        assertEquals(compareResult.getMessage(), actualResult.getMessage());
    }
    @Test
    public void personAllFailUser() throws DataAccessException {
        if (print)
            System.out.println("Test personAllFailUser called");
        authToken.setAuthToken("wrongUser");
        PersonService service = new PersonService();
        PersonAllResult compareResult = service.person(authToken.getAuthToken());
        PersonAllResult actualResult = new PersonAllResult("Error: Persons not returned");
        assertNotNull(compareResult);
        assertEquals(actualResult, compareResult);
    }
}

