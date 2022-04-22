package FakeData;


import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Model.Person;

import java.util.Random;

public class LogicalYear {
    private final Database db;
    private final int MIN_AGE = 17;
    private final int FERTILITY = 50;
    private final int MAX_AGE = 120;
    private final int EARLIEST = 0;
    private final int LATEST = 2020;
    private int minDate;
    private int maxDate;
    private int range;
    Random rand;

    private int startingYear;
    private int startingMarriage = 0;


    public LogicalYear(Database db) {
        this.db = db;
        minDate = 0;
        maxDate = LATEST;
        range = 0;
        startingYear = 0;
        rand = new Random();
    }

    public int setBirthYear(Person currPerson) throws DataAccessException {
        //Parents must be born at least 13 years before their children
        //Women must not give birth when older than 50 years old
        //Birth events must be the first event for a person chronologically
        //Parents must not die before their child is born
        String motherID = currPerson.getMotherID();
        String fatherID = currPerson.getFatherID();
        String spouseID = currPerson.getSpouseID();
        if (motherID.equals("") || fatherID.equals("")) {
            if (spouseID.equals("")) {
                int tempRange = LATEST - 2 * MAX_AGE;
                if (startingYear != 0)
                    tempRange = FERTILITY;
                return distDate(tempRange, startingYear - MIN_AGE, 1);
            } else {
                int spouseBirth = new EventDAO(db.getConnection()).
                        findByEvent("birth", "personID", spouseID).getYear();
                startingYear = distDate(FERTILITY, spouseBirth - MIN_AGE, 2);
                return startingYear;
            }
        }
        try {
            //get the birth, death, and marriage dates of the mother and father
            int[] motherEvents = getEvents(motherID);
            int[] fatherEvents = getEvents(fatherID);

            //find the earliest date the person could be born
            minDate = Math.max(motherEvents[0], fatherEvents[0]) + MIN_AGE;
            minDate = Math.max(minDate, motherEvents[2]);
            //find the latest date the person could be born
            int minDeath = Math.min(motherEvents[1], fatherEvents[1]);
            int tooOld = motherEvents[0] + FERTILITY;
            maxDate = Math.min(tooOld, minDeath);
            range = maxDate - minDate;

            int birthYear = distDate(range, minDate, 3);
            if (birthYear < 0) {
                new EventDAO(db.getConnection()).updateDeath(motherID, motherEvents[1] - range);
                new EventDAO(db.getConnection()).updateDeath(fatherID, fatherEvents[1] - range);
                birthYear = minDate;
            }
            reset();
            return birthYear;
        } catch (DataAccessException ex) {
            System.out.println("\tException: " + ex);
            return -1;
        }
    }
    public int setMarriageYear(String motherID, String fatherID) throws DataAccessException {
        //Parents must be at least 13 years old when they are married.
        int[] motherEvents = getEvents(motherID);
        int[] fatherEvents = getEvents(fatherID);
        minDate = Math.max(motherEvents[0], fatherEvents[0]) + MIN_AGE;
        maxDate = Math.min(motherEvents[1], fatherEvents[1]);
        maxDate = Math.min(maxDate, minDate + MAX_AGE);
        range = maxDate - minDate;

        int marriageYear = distDate(range, minDate, 4);
        reset();
        return marriageYear;
    }
    public int setDeathYear(String personID) throws DataAccessException {
        //Death events must be the last event for a person chronologically
        //Nobody must die at an age older than 120 years old
        int[] personEvents = getEvents(personID);
        int lastEvent = EARLIEST;
        for (int year : personEvents) {
            lastEvent = Math.max(lastEvent, year);
        }
        minDate = Math.max(lastEvent, personEvents[0] + MIN_AGE + 1);
        maxDate = personEvents[0] + MAX_AGE;
        range = maxDate - minDate;
        int deathYear = distDate(range, minDate, 5);
        reset();
        return deathYear;
    }

    private int[] getEvents(String personID) throws DataAccessException {
        int birthYear = 0;
        int marriageYear = 0;
        int deathYear = 0;

        if (!personID.equals("")) {
            Event birth = new EventDAO(db.getConnection()).findByEvent("birth","personID", personID);
            Event death = new EventDAO(db.getConnection()).findByEvent("death","personID", personID);
            Event marriage = new EventDAO(db.getConnection()).findByEvent("marriage","personID", personID);
            if (birth != null)
                birthYear = birth.getYear();
            if (death != null)
                deathYear = death.getYear();
            if (marriage != null)
                marriageYear = marriage.getYear();
        }
        if (birthYear == 0 && marriageYear == 0 && deathYear == 0) {
            return originDates();
        }
        return new int[]{birthYear, deathYear, marriageYear};
    }
    private int[] originDates() {
        int tempRange = LATEST;
        if (startingYear != 0)
            tempRange = FERTILITY;
        int birthYear = distDate(tempRange, startingYear - MIN_AGE, 6);
        int deathYear = distDate(FERTILITY, FERTILITY + birthYear, 7);
        int marriageYear;
        if (startingMarriage == 0) {
            marriageYear = distDate(FERTILITY - MIN_AGE, MIN_AGE + birthYear, 8);
            startingMarriage = marriageYear;
        }
        else
            marriageYear = startingMarriage;
        if (startingYear == 0)
            startingYear = birthYear;
        if (marriageYear < birthYear)
            birthYear = marriageYear - 20;
        if (deathYear < marriageYear)
            deathYear = marriageYear + 20;
        return new int[]{birthYear, deathYear, marriageYear};
    }
    private int distDate(int width, int extrem, int id) {
        if (width < 1) {
            return -1;
        }
        try {
            int middle = Math.max(1, width / 3);
            return middle + extrem;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return minDate;
        }
    }
    private void reset() {
        minDate = EARLIEST;
        maxDate = LATEST;
        range = 0;
        startingMarriage = 0;
    }
}
