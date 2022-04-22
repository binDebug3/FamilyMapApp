package edu.byu.cs240.dallinstewart.familymapclient;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;
import Request.RegisterRequest;
import model.DataCache;

public class Global {
    public static List<Event> orderEvents(List<Event> events) {
        //order events by year and then by event type
        List<Event> orderedEvents = new LinkedList<>();
        List<Integer> order = new LinkedList<>();
        Event prevEvent = null;
        for (Event event : events) {
            int eventYear = event.getYear();
            //if the list is empty, just add the element to the list
            if (order.size() == 0) {
                order.add(eventYear);
                orderedEvents.add(event);
                prevEvent = event;
                continue;
            }
            int index = 0;
            int prevYear = 0;
            //parse through a separate but correlated list of event years to identify
            //where to put the latest event
            for (int year : order) {
                //if the event occurred in the same year as one already in the list
                //order the events by event type
                if (eventYear == year) {
                    String prevType = prevEvent.getEventType().toLowerCase(Locale.ROOT);
                    String eventType = event.getEventType().toLowerCase(Locale.ROOT);
                    int compare = compareTo(prevType, eventType);
                    if (compare > 0) {
                        order.add(index + 1, eventYear);
                        orderedEvents.add(index + 1, event);
                    } else {
                        if (index != 0) {
                            order.add(index, eventYear);
                            orderedEvents.add(index, event);
                        }
                    }
                    break;
                }
                //update the year and event lists
                if (prevYear < eventYear && eventYear < year) {
                    order.add(index, eventYear);
                    orderedEvents.add(index, event);
                    break;
                }
                //update the year and event lists
                index++;
                prevYear = year;
                prevEvent = event;
                if (index == order.size()) {
                    order.add(index, eventYear);
                    orderedEvents.add(index, event);
                    break;
                }
            }
        }
        return orderedEvents;
    }
    //helper method to compare two strings
    private static int compareTo(String firstE, String secondE) {
        return firstE.compareTo(secondE);
    }

    //return the correct icon based on the event or gender of the person passed
    public static IconDrawable getIcon(Context context, String associatedPersonID) {
        FontAwesomeIcons icon = FontAwesomeIcons.fa_female;
        int color = R.color.female_icon;
        if (associatedPersonID.equals("0")) {
            icon = FontAwesomeIcons.fa_map_marker;
            color = R.color.black;
        } else if (associatedPersonID.equals("1")) {
            icon = FontAwesomeIcons.fa_android;
            color = R.color.purple_700;
        } else if (DataCache.getInstance().getPersonByID(associatedPersonID).getGender().equals("m")) {
            icon = FontAwesomeIcons.fa_male;
            color = R.color.male_icon;
        }
        return new IconDrawable(context, icon).colorRes(color);
    }

    //return a list of people whose information contains the search term
    public static List<Person> searchPerson(Map<String, Person> people, String searchTerm) {
        List<Person> searchResultPeople = new ArrayList<>();
        if (!searchTerm.equals("")) {
            Set<String> peopleKeys = people.keySet();
            for (Object personID : peopleKeys) {
                Person person = people.get(personID);
                if (person!= null &&
                        (person.getFirstName().toLowerCase(Locale.ROOT).contains(searchTerm) ||
                                person.getLastName().toLowerCase(Locale.ROOT).contains(searchTerm))) {
                    searchResultPeople.add(person);
                }
            }
        }
        return searchResultPeople;
    }
    //return a list of events whose information contains the search term
    public static List<Event> searchEvent(Map<String, Event> events, String searchTerm) {
        List<Event> searchResultEvents = new ArrayList<>();
        if (!searchTerm.equals("")) {
            Set<String> eventKeys = events.keySet();
            for (Object eventID : eventKeys) {
                Event event = events.get(eventID);
                if (checkEvent(event, searchTerm)) {
                    searchResultEvents.add(event);
                }
            }
        }
        return searchResultEvents;
    }
    //helper method to determine if an event's information contains the search term
    private static boolean checkEvent(Event event, String searchTerm) {
        if (event == null) {
            return false;
        }
        boolean contains = event.getCountry().toLowerCase(Locale.ROOT).contains((searchTerm)) ||
                event.getCity().toLowerCase(Locale.ROOT).contains(searchTerm) ||
                event.getEventType().toLowerCase(Locale.ROOT).contains(searchTerm) ||
                String.valueOf(event.getYear()).contains(searchTerm);
        boolean filter = DataCache.getInstance().isInDisplayEvents(event.getEventID());
        return contains && filter;
    }
    //check request information to make sure it is all filled
    public static boolean registerIsValid(RegisterRequest request) {
        return !request.getFirstName().equals("") &&
                !request.getLastName().equals("") &&
                !request.getUsername().equals("") &&
                !request.getPassword().equals("") &&
                !request.getEmail().equals("") &&
                !request.getGender().equals("");
    }
}
