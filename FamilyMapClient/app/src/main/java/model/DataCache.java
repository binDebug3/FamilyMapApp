package model;

import Model.*;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataCache extends ViewModel {

    private final String TAG = "DataCache";
    private static DataCache instance = new DataCache();
    private final String MALE = "m";
    private final String FEMALE = "f";

    //all persons
    private Map<String, Person> persons; //personID
    //map that relations a person's id to a list of their children's ids
    private Map<String, List<String>> children; //personID, personID
    //all events
    private Map<String, Event> events; //userID
    //all events that should appear on the map, in a person activity, or in a search result
    private Map<String, Event> displayEvents; //eventID
    //map that relates a person by their id to the ids of all the life events
    private Map<String, List<Event>> personEvents; //userID
    private Map<String, List<Person>> personsByUser; //userID
    //list of people that appear in a search result
    private List<Person> searchPeople;
    //list of events that appear in a search result
    private List<Event> searchEvents;
    //set of people by their person ids that belong to the father side of the users family
    private Set<String> paternalAncestors;
    //set of people by their person ids that belong to the mother side of the users family
    private Set<String> maternalAncestors;
    //person id of the current user
    private String currUserID;
    //most recent authtoken
    private String currAuthToken;
    //first name of the current user
    private String currFirst;
    //last name of the current user
    private String currLast;
    //id of the event last clicked on the map fragment
    private String lastEventID;
    //true if the event has been updated since it was last displayed
    private boolean lastEventUpdated;
    //true if the settings for displaying events have been changed
    private boolean eventsUpdated;

    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
        persons = new HashMap<>();
        children = new HashMap<>();
        events = new HashMap<>();
        displayEvents = new HashMap<>();
        personEvents = new HashMap<>();
        personsByUser = new HashMap<>();
        searchPeople = new ArrayList<>();
        searchEvents = new ArrayList<>();
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        currFirst = "1";
        currLast = "2";
        lastEventUpdated = false;
        eventsUpdated = false;
        lastEventID = "";
    }

    //getters and setters
    public Person getPersonByID(String personID) {
        return persons.get(personID);
    }
    public Event getEventByID(String eventID) {
        return events.get(eventID);
    }

    public List<String> getChildren(String parentID) {
        return children.get(parentID);
    }
    public List<Event> getPersonEvents(String personID) {
        List<Event> primeEvents = personEvents.get(personID);
        List<Event> sendEvents = new ArrayList<>();
        if (primeEvents != null) {
            for (Event prime : primeEvents) {
                if (displayEvents.get(prime.getEventID()) != null) {
                    sendEvents.add(prime);
                }
            }
        }
        return sendEvents;
    }
    public Event[] getDisplayEvents() {
        Set<String> keys = displayEvents.keySet();
        Event[] displayEventArray = new Event[keys.size()];
        int index = 0;
        for (String key : keys) {
            displayEventArray[index] = displayEvents.get(key);
            index++;
        }
        return displayEventArray;
    }

    public Map<String, Person> getAllPersons() {
        return persons;
    }
    public Map<String, Event> getAllEvents() {
        return events;
    }

    //insert a new person to the persons list
    public void insertPersonsByUser(String userID, Person[] people) {
        List<Person> listPeople = new ArrayList<>();
        for (Person person : people) {
            listPeople.add(person);
            String personID = person.getPersonID();
            persons.put(personID, person);
            updateChildren(person);
        }
        children.remove("");
        personsByUser.put(userID, listPeople);
        buildDivide();
    }
    //insert a new event to the events list
    public void insertEventsByUser(String userID, Event[] userEvents) {
        List<Event> listEvents = new ArrayList<>();
        for (Event event : userEvents) {
            listEvents.add(event);
            events.put(event.getEventID(), event);
            insertToPersonEvents(event);
        }
    }
    //helper method to add an event to the list of all events corresponding to a single person
    private void insertToPersonEvents(Event event) {
        if (personEvents.containsKey(event.getPersonID())) {
            personEvents.get(event.getPersonID()).add(event);
        } else {
            List<Event> initEvents = new ArrayList<>();
            initEvents.add(event);
            personEvents.put(event.getPersonID(), initEvents);
        }
    }
    //helper method to add a person to the list of children corresponding to each person
    private void updateChildren(Person person) {
        if (children.containsKey(person.getFatherID())) {
            children.get(person.getFatherID()).add(person.getPersonID());
        } else {
            List<String> childID = new ArrayList<>();
            childID.add(person.getPersonID());
            children.put(person.getFatherID(), childID);
        }
        if (children.containsKey(person.getMotherID())) {
            children.get(person.getMotherID()).add(person.getPersonID());
        } else {
            List<String> childID = new ArrayList<>();
            childID.add(person.getPersonID());
            children.put(person.getMotherID(), childID);
        }
    }
    //update the list of events that can be displayed based on current settings
    public void updateDisplayEvents() {
        Set<String> eventKeys = events.keySet();
        displayEvents.clear();
        for (String eventKey : eventKeys) {
            Event event = getEventByID(eventKey);
            if (needToInclude(event)) {
                displayEvents.put(eventKey, event);
            }
        }
    }
    //helper method to update the father side and mother side lists when a person is added
    private void buildDivide() {
        buildAncestors(paternalAncestors, getPersonByID(currUserID).getFatherID());
        buildAncestors(maternalAncestors, getPersonByID(currUserID).getMotherID());
        paternalAncestors.remove(currUserID);
        maternalAncestors.remove(currUserID);
    }
    //recursive helper method to build lists for the father side and mother side
    private void buildAncestors(Set<String> ancestors, String personID) {
        ancestors.add(personID);
        String fatherID = getPersonByID(personID).getFatherID();
        String motherID = getPersonByID(personID).getMotherID();
        if (fatherID != null && !fatherID.equals("")) {
            buildAncestors(ancestors, fatherID);
        }
        if (motherID != null && !motherID.equals("")) {
            buildAncestors(ancestors,motherID);
        }
    }

    //getters and setters
    public String getCurrUserID() {
        return currUserID;
    }
    public void setCurrUserID(String currUserID) {
        this.currUserID = currUserID;
    }

    public String getCurrAuthToken() {
        return currAuthToken;
    }
    public void setCurrAuthToken(String currAuthToken) {
        this.currAuthToken = currAuthToken;
    }

    public String getCurrFirst() {
        return currFirst;
    }
    public void setCurrFirst(String currFirst) {
        this.currFirst = currFirst;
    }

    public String getCurrLast() {
        return currLast;
    }
    public void setCurrLast(String currLast) {
        this.currLast = currLast;
    }

    public String getLastEventID() {
        return lastEventID;
    }
    public void setLastEventID(String lastEventID) {
        this.lastEventID = lastEventID;
        lastEventUpdated = true;
    }

    //helper method to determine if an event should be included in the displayElements list
    private boolean needToInclude(Event event) {
        boolean include = true;
        String personID = event.getPersonID();
        if (include &&
                !Settings.getInstance().isMaleEvents() &&
                getPersonByID(personID).getGender().equals(MALE)) {
            include = false; }
        if (include &&
                !Settings.getInstance().isFemaleEvents() &&
                getPersonByID(personID).getGender().equals(FEMALE)) {
            include = false; }
        if (include &&
                !Settings.getInstance().isFatherSide() &&
                paternalAncestors.contains(personID)) {
            include = false; }
        if (include &&
                !Settings.getInstance().isMotherSide() &&
                maternalAncestors.contains(personID)) {
            include = false; }
        return include;
    }
    public boolean isInDisplayEvents(String eventID) {
        return displayEvents.get(eventID) != null;
    }

    public boolean isEventsUpdated() {
        return eventsUpdated;
    }
    public void setEventsUpdated(boolean eventsUpdated) {
        this.eventsUpdated = eventsUpdated;
    }
    public boolean isLastEventUpdated() {
        return lastEventUpdated;
    }

    //clear all the data cache when a user logs out
    public void clear() {
        persons = new HashMap<>();
        children = new HashMap<>();
        events = new HashMap<>();
        displayEvents = new HashMap<>();
        personEvents = new HashMap<>();
        personsByUser = new HashMap<>();
        searchPeople = new ArrayList<>();
        searchEvents = new ArrayList<>();
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        currFirst = "1";
        currLast = "2";
        eventsUpdated = false;
    }
}
