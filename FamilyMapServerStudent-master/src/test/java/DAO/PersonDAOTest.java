package DAO;

import Model.Event;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person bestPerson;
    private PersonDAO pDao;
    private boolean print = true;

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nPersonDAO SetUp called");
        db = new Database();
        bestPerson = new Person("Gale_123A", "Gale3000", "Gale", "Smith",
                "f", "john3",
                "mary4", "bob2");
        Connection conn = db.getConnection();
        pDao = new PersonDAO(conn);
        pDao.clear();
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
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        if (print)
            System.out.println("Test insertFail called");
        pDao.addPerson(bestPerson);
        assertThrows(DataAccessException.class, ()-> pDao.addPerson(bestPerson));
    }

    @Test
    public void checkPersonPass() throws DataAccessException {
        if (print)
            System.out.println("Test checkPersonPass called");
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }
    @Test
    public void checkPersonFail() throws DataAccessException {
        if (print)
            System.out.println("Test checkPersonFail called");
        pDao.addPerson(bestPerson);
        Person compareTest = pDao.checkPerson("personID", "fakeID");
        assertNull(compareTest);
    }

    @Test
    public void checkAllDataPass() throws DataAccessException {
        if (print)
            System.out.println("Test checkAllDataPass called");
        pDao.addPerson(bestPerson);
        int size = 1;
        Person[] actual = new Person[]{bestPerson};
        Person[] compareTest = pDao.checkAllData(size, "personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        for (int i = 0; i < size; i++) {
            assertEquals(actual[i], compareTest[i]);
        }
    }
    @Test
    public void checkAllDataFail() throws DataAccessException {
        if (print)
            System.out.println("Test checkAllDataFail called");
        pDao.addPerson(bestPerson);
        int size = 1;
        Person[] compareTest = pDao.checkAllData(size, "personID", "fakeID");
        for (int i = 0; i < compareTest.length; i++) {
            assertNull(compareTest[i]);
        }
    }

    @Test
    public void updateSpousePass() throws DataAccessException {
        if (print)
            System.out.println("Test checkAllDataFail called");
        pDao.addPerson(bestPerson);
        pDao.updateSpouse(bestPerson.getPersonID(), "spouseID");
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(compareTest.getSpouseID(), "spouseID");
    }
    @Test
    public void updateSpouseFail() throws DataAccessException {
        if (print)
            System.out.println("Test updateSpouseFail called");
        pDao.addPerson(bestPerson);
        pDao.updateSpouse("fakeId", "spouseID");
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertNotEquals(compareTest.getSpouseID(), "spouseID");
    }

    @Test
    public void getSubSizePass() throws DataAccessException {
        if (print)
            System.out.println("Test getSubSizePass called");
        pDao.addPerson(bestPerson);
        int size = pDao.getSubSize("personID", bestPerson.getPersonID());
        assertEquals(size, 1);
    }
    @Test
    public void getSubSizeFail() throws DataAccessException {
        if (print)
            System.out.println("Test getSubSizePass called");
        pDao.addPerson(bestPerson);
        int size = pDao.getSubSize("personID", "fakeID");
        assertEquals(size, 0);
    }

    @Test
    public void removePass() throws DataAccessException {
        if (print)
            System.out.println("Test removePass called");
        pDao.addPerson(bestPerson);
        pDao.removePerson(bestPerson.getPersonID());
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNull(compareTest);
    }

    @Test
    public void removeFail() throws DataAccessException {
        if (print)
            System.out.println("Test removeFail called");
        pDao.addPerson(bestPerson);
        pDao.removePerson(bestPerson.getPersonID()+" ");
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        if (print)
            System.out.println("Test clearPass called");
        pDao.addPerson(bestPerson);
        pDao.clear();
        Person compareTest = pDao.checkPerson("personID", bestPerson.getPersonID());
        assertNull(compareTest);
    }
}
