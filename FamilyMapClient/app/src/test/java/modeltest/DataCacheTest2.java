package modeltest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.*;
import Request.LoginRequest;
import Result.EventAllResult;
import Result.PersonAllResult;
import edu.byu.cs240.dallinstewart.familymapclient.Global;
import model.DataCache;
import model.Settings;
import network.ServerProxy;

public class DataCacheTest2 {
    private final boolean print = true;
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_PORT = "8080";
    private static final String AUTHTOKEN = "ZuEto1WE5wKT";
    private static final String USERID = "Sheila_Parker";
    private LoginRequest loginRequest;
    Map<String, Person> everyPerson;
    Map<String, Event> everyEvent;

    public DataCacheTest2() {}

    @BeforeEach
    public void setUp() {
        //fill the data cache with data from the CS 240 test data
        if (print)
            System.out.println("\nDataCacheTest2 SetUp called");
        DataCache.getInstance().setCurrUserID(USERID);
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT, "", AUTHTOKEN);
        DataCache.getInstance().insertPersonsByUser(USERID, personAllResult.getPersons());
        everyPerson = DataCache.getInstance().getAllPersons();
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, AUTHTOKEN);
        DataCache.getInstance().insertEventsByUser(USERID, eventAllResult.getEvents());
        everyEvent = DataCache.getInstance().getAllEvents();
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
    public void familyRelations() {
        //Test all of the relationships of every person from the CS 240 loaded data
        //including abnormal ones
        if (print)
            System.out.println("Test familyRelations called");
        Set<String> personKeys = everyPerson.keySet();
        for (String personID : personKeys) {
            Person person = everyPerson.get(personID);
            assertNotNull(person);
            String fatherID = person.getFatherID();
            String motherID = person.getMotherID();
            String spouseID = person.getSpouseID();
            if (fatherID != null && !fatherID.equals("")) {
                List<String> fatherChildren = DataCache.getInstance().getChildren(fatherID);
                String childOfFather = null;
                for (String childID : fatherChildren) {
                    if (childID.equals(personID)) {
                        childOfFather = childID;
                    }
                }
                assertNotNull(childOfFather);
                assertEquals(personID, childOfFather);
            }
            if (motherID != null && !motherID.equals("")) {
                List<String> motherChildren = DataCache.getInstance().getChildren(motherID);
                String childOfMother = null;
                for (String childID : motherChildren) {
                    if (childID.equals(personID)) {
                        childOfMother = childID;
                    }
                }
                assertNotNull(childOfMother);
                assertEquals(personID, childOfMother);
            }
            if (spouseID != null && !spouseID.equals("")) {
                String spouseOfSpouse = DataCache.getInstance().getPersonByID(spouseID).getSpouseID();
                assertNotNull(spouseOfSpouse);
                assertEquals(personID, spouseOfSpouse);
            }
        }
    }
    @Test
    public void chronologicalEventsPass() {
        //Test all of the events for every person from the CS 240 loaded data
        //including abnormal ones
        if (print)
            System.out.println("Test chronologicalEventsPass called");
        Set<String> personKeys = everyPerson.keySet();
        for (String personID : personKeys) {
            List<Event> personEvents = DataCache.getInstance().getPersonEvents(personID);
            assertNotNull(personEvents);
            personEvents = Global.orderEvents(personEvents);
            Event prevEvent = null;
            for (Event event : personEvents) {
                if (prevEvent != null) {
                    boolean inOrder = prevEvent.getYear() <= event.getYear();
                    assertTrue(inOrder);
                }
                prevEvent = event;
            }
        }
    }
    @Test
    public void searchBlank() {
        //test the search feature when no search term is entered
        if (print)
            System.out.println("Test searchBlank called");
        String searchTerm = "";
        List<Person> searchResultPerson = Global.searchPerson(everyPerson, searchTerm);
        List<Event> searchResultEvent = Global.searchEvent(everyEvent, searchTerm);
        assertNotNull(searchResultPerson);
        assertNotNull(searchResultEvent);
        assertEquals(0, searchResultPerson.size());
        assertEquals(0, searchResultEvent.size());
    }
    @Test
    public void searchPass() {
        //test a normal search result
        if (print)
            System.out.println("Test searchPass called");
        String searchTerm = "a";
        boolean containsA;
        List<Person> searchResultPerson = Global.searchPerson(everyPerson, searchTerm);
        List<Event> searchResultEvent = Global.searchEvent(everyEvent, searchTerm);
        assertNotNull(searchResultPerson);
        assertNotNull(searchResultEvent);
        assertNotEquals(0,searchResultPerson.size());
        assertNotEquals(0,searchResultEvent.size());
        for (Person person : searchResultPerson) {
            containsA = person.getFirstName().contains(searchTerm) ||
                    person.getLastName().contains(searchTerm);
            assertTrue(containsA);
        }
        for (Event event : searchResultEvent) {
            containsA = event.getEventType().contains(searchTerm) ||
                    event.getCity().contains(searchTerm) ||
                    event.getCountry().contains(searchTerm) ||
                    DataCache.getInstance().getPersonByID(event.getPersonID()).getFirstName().contains(searchTerm) ||
                    DataCache.getInstance().getPersonByID(event.getPersonID()).getLastName().contains(searchTerm);
            assertTrue(containsA);
        }
    }
    @Test
    public void searchNumber() {
        //test the search feature when a number is entered
        if (print)
            System.out.println("Test searchNumber called");
        String searchTerm = "1";
        boolean containsA = false;
        List<Person> searchResultPerson = Global.searchPerson(everyPerson, searchTerm);
        List<Event> searchResultEvent = Global.searchEvent(everyEvent, searchTerm);
        assertNotNull(searchResultPerson);
        assertNotNull(searchResultEvent);
        assertEquals(0, searchResultPerson.size());
        assertNotEquals(0, searchResultEvent.size());
        for (Event event : searchResultEvent) {
            containsA = String.valueOf(event.getYear()).contains(searchTerm);
            assertTrue(containsA);
        }
    }
    @Test
    public void searchAbnormal() {
        //test the search feature when a search term should have zero results
        if (print)
            System.out.println("Test searchAbnormal called");
        String searchTerm = "qwertyuiop";
        List<Person> searchResultPerson = Global.searchPerson(everyPerson, searchTerm);
        List<Event> searchResultEvent = Global.searchEvent(everyEvent, searchTerm);
        assertNotNull(searchResultPerson);
        assertNotNull(searchResultEvent);
        assertEquals(0, searchResultPerson.size());
        assertEquals(0, searchResultEvent.size());
    }
}
