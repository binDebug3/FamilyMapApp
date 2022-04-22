package Request;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Hold data for a request to load new relatives and events
 */
public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * Default constructor
     */
    public LoadRequest() {
        new LoadRequest(null, null, null);
    }
    /**
     * LoadRequest constructor with existing values
     * @param users array of user objects to load
     * @param persons array of person objects to load
     * @param events array of event objects to load
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }
    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }
    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }
    public void setEvents(Event[] events) {
        this.events = events;
    }
}
