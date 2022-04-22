package modeltest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import Model.*;
import Request.LoginRequest;
import Result.EventAllResult;
import Result.PersonAllResult;
import model.DataCache;
import model.Settings;
import network.ServerProxy;

public class SettingsTest2 {
    private final boolean print = true;
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_PORT = "8080";
    private static final String AUTHTOKEN = "ZuEto1WE5wKT";
    private static final String USERID = "Sheila_Parker";
    private LoginRequest loginRequest;
    Map<String, Person> everyPerson;
    Event[] everyEvent;

    public SettingsTest2() {}

    @BeforeEach
    public void setUp() {
        //fill the data cache with data from the CS 240 test data
        if (print)
            System.out.println("\nSettingsTest2 SetUp called");
        DataCache.getInstance().setCurrUserID(USERID);
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT, "", AUTHTOKEN);
        DataCache.getInstance().insertPersonsByUser(USERID, personAllResult.getPersons());
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, AUTHTOKEN);
        DataCache.getInstance().insertEventsByUser(USERID, eventAllResult.getEvents());
        Settings.getInstance().setFemaleEvents(true);
        DataCache.getInstance().updateDisplayEvents();
    }
    @AfterEach
    public void tearDown() {
        if (print) {
            System.out.println("Test successful. Close called");
        }
    }

    @Test
    public void maleOff() {
        //test events when male events are turned off
        if (print) {
            System.out.println("Test maleOff called");
        }
        Settings.getInstance().setMaleEvents(false);
        DataCache.getInstance().updateDisplayEvents();
        everyPerson = DataCache.getInstance().getAllPersons();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        for (Event event : everyEvent) {
            assertEquals("f", DataCache.getInstance().
                    getPersonByID(event.getPersonID()).getGender());
        }
        assertNotEquals(0, everyPerson.size());
    }

    @Test
    public void femaleOff() {
        //test events when female events are turned off
        if (print) {
            System.out.println("Test femaleOff called");
        }
        Settings.getInstance().setFemaleEvents(false);
        DataCache.getInstance().updateDisplayEvents();
        everyPerson = DataCache.getInstance().getAllPersons();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        for (Event event : everyEvent) {
            assertEquals("m", DataCache.getInstance().
                    getPersonByID(event.getPersonID()).getGender());
        }
        assertNotEquals(0, everyPerson.size());
    }

    @Test
    public void fatherOff() {
        //test events when father side events are turned off
        if (print) {
            System.out.println("Test fatherOff called");
        }
        Settings.getInstance().setFatherSide(false);
        DataCache.getInstance().updateDisplayEvents();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        String userID = DataCache.getInstance().getCurrUserID();
        Person user = DataCache.getInstance().getPersonByID(userID);
        String fatherID = user.getFatherID();
        for (Event event : everyEvent) {
            assertNotEquals(fatherID, event.getPersonID());
        }
    }

    @Test
    public void motherOff() {
        //test events when mother side events are turned off
        if (print) {
            System.out.println("Test motherOff called");
        }
        Settings.getInstance().setMotherSide(false);
        DataCache.getInstance().updateDisplayEvents();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        String userID = DataCache.getInstance().getCurrUserID();
        Person user = DataCache.getInstance().getPersonByID(userID);
        String motherID = user.getMotherID();
        for (Event event : everyEvent) {
            assertNotEquals(motherID, event.getPersonID());
        }
    }

    @Test
    public void maleAndFemaleOff() {
        //test events when male and female events are turned off
        if (print) {
            System.out.println("Test maleAndFemaleOff called");
        }
        Settings.getInstance().setMaleEvents(false);
        Settings.getInstance().setFemaleEvents(false);
        DataCache.getInstance().updateDisplayEvents();
        everyPerson = DataCache.getInstance().getAllPersons();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        assertNotNull(everyPerson);
        assertNotNull(everyEvent);
        assertEquals(0, everyEvent.length);
        assertNotEquals(0, everyPerson.size());
    }

    @Test
    public void motherAndFatherOff() {
        //test events when mother side and father side events are turned off
        if (print) {
            System.out.println("Test motherAndFatherOff called");
        }
        Settings.getInstance().setMotherSide(false);
        Settings.getInstance().setFatherSide(false);
        DataCache.getInstance().updateDisplayEvents();
        everyPerson = DataCache.getInstance().getAllPersons();
        everyEvent = DataCache.getInstance().getDisplayEvents();
        assertNotNull(everyPerson);
        assertNotNull(everyEvent);
        assertNotEquals(0, everyPerson.size());
        assertNotEquals(0, everyEvent.length);
        assertNotNull(everyPerson.get(DataCache.getInstance().getCurrUserID()));
    }
}
