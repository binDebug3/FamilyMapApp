package FakeData;

import DAO.*;
import Model.Event;
import Model.Person;
import Request.RegisterRequest;

import java.util.Random;

/**
 * Generates a new person and their family history with a specified number of generations
 */
public class PersonGenerator {
    private final Database db;
    private final LogicalYear logicalYear;
    private final Person userPerson;

    private final String associatedUsername;
    private final int ogGenerations;
    private int numPersonsAdded;
    private int numEventsAdded;
    private String wifeID;

    /**
     * Constructor for Fill request
     * @param username username of the user of the fill request
     */
    public PersonGenerator(Database db, String username, int ogGenerations) {
        this.db = db;
        logicalYear = new LogicalYear(db);
        userPerson = null;
        associatedUsername = username;
        this.ogGenerations = ogGenerations;
        numPersonsAdded = 0;
        numEventsAdded = 0;
        wifeID = "";
    }

    /**
     * Constructor for the Register request
     * @param request register request object
     */
    public PersonGenerator(Database db, RegisterRequest request, int ogGenerations, Person userPerson) {
        this.db = db;
        logicalYear = new LogicalYear(db);
        associatedUsername = request.getUsername();
        numPersonsAdded = 0;
        numEventsAdded = 0;
        this.ogGenerations = ogGenerations;
        wifeID = "";
        this.userPerson = userPerson;
    }

    /**
     * Recursive algorithm to populate a family tree
     * @param gender gender of the current person
     * @param generations number of generations to generate for the current person
     * @return the newly constructed person
     * @throws DataAccessException if there is an error accessing the database
     */
    public Person generatePerson(String gender, int generations) throws DataAccessException {
        Person mother;
        Person father;
        String mpID = "";
        String fpID = "";

        if (generations > 0) {
            mother = generatePerson("f", generations - 1);
            mpID = mother.getPersonID();
            wifeID = mpID;
            father = generatePerson("m", generations - 1);

            fpID = father.getPersonID();
            new PersonDAO(db.getConnection()).updateSpouse(fpID, mother.getPersonID());
            father.setSpouseID(mpID);
            new PersonDAO(db.getConnection()).updateSpouse(mpID, fpID);
            mother.setSpouseID(fpID);

            insertMarriage(mpID, fpID);
        }

        Person sendPerson;
        if (userPerson != null && generations == ogGenerations) {
            sendPerson = insertUserPerson(mpID, fpID);
        } else {
            sendPerson = insertPerson(mpID, fpID, gender);
        }
        insertBirth(sendPerson);
        insertDeath(sendPerson);
        return sendPerson;
    }


    /**
     * Generate a new Event object
     * @param eventType type of event to generate
     * @param personID associated personID for the event
     * @return a new Event object
     */
    private Event generateEvent(String eventType, String personID) {
        LocationLoader locLoader = new LocationLoader();
        Location location = locLoader.getRandLocation();
        float latitude = Float.parseFloat(location.getLatitude());
        float longitude = Float.parseFloat(location.getLongitude());
        String country = location.getCountry();
        String city = location.getCity();
        return new Event(generateID(), associatedUsername, personID, latitude, longitude, country, city, eventType, -1);
    }
    /**
     * Generate a unique eventID or personID
     * @return the new ID as a String
     */
    private String generateID() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    private Person insertPerson(String mpID, String fpID, String gender) throws DataAccessException {
        //set spouseID
        String personID = generateID();
        String spouseID = "";
        if (gender.equals("m"))
            spouseID = wifeID;
        //set first and last name
        NameLoader nameLoader = new NameLoader();
        String firstName;
        String lastName = nameLoader.getRandSurname();
        if (gender.equals("m")) {
            firstName = nameLoader.getRandMale();
        } else {
            firstName = nameLoader.getRandFemale();
        }
        //Build and insert person
        Person sendPerson = new Person(personID, associatedUsername, firstName, lastName, gender, fpID, mpID, spouseID);
        new PersonDAO(db.getConnection()).addPerson(sendPerson);
        numPersonsAdded++;
        return sendPerson;
    }
    private Person insertUserPerson(String mpID, String fpID) throws DataAccessException {
        Person sendPerson = new Person(userPerson.getPersonID(), associatedUsername, userPerson.getFirstName(),
                userPerson.getLastName(), userPerson.getGender(), fpID, mpID, "");
        new PersonDAO(db.getConnection()).addPerson(sendPerson);
        numPersonsAdded++;
        return sendPerson;
    }
    private void insertBirth(Person sendPerson) throws DataAccessException {
        //create a new birth event
        String personID = sendPerson.getPersonID();
        Event birth = generateEvent("birth", personID);
        //pick a random year with constraints
        int birthYear = logicalYear.setBirthYear(sendPerson);
        birth.setYear(birthYear);
        //update the database
        new EventDAO(db.getConnection()).insert(birth);
        numEventsAdded++;
    }
    private void insertDeath(Person sendPerson) throws DataAccessException {
        //create a new death event
        String personID = sendPerson.getPersonID();
        Event death = generateEvent("death", personID);
        //pick a random year with constraints
        int deathYear = logicalYear.setDeathYear(sendPerson.getPersonID());
        death.setYear(deathYear);
        //update the database
        new EventDAO(db.getConnection()).insert(death);
        numEventsAdded++;
    }
    private void insertMarriage(String mpID, String fpID) throws DataAccessException {
        //create a new marriage event
        Event marriageM = generateEvent("marriage", mpID);
        //pick a random year with constraints
        int marriageYear = logicalYear.setMarriageYear(mpID, fpID);
        marriageM.setYear(marriageYear);
        //create a duplicate marriage event
        Event marriageF = new Event(generateID(), marriageM.getAssociatedUsername(), fpID,
                marriageM.getLatitude(), marriageM.getLongitude(), marriageM.getCountry(), marriageM.getCity(),
                marriageM.getEventType(), marriageM.getYear());
        //update the database
        numEventsAdded += 2;
        new EventDAO(db.getConnection()).insert(marriageM);
        new EventDAO(db.getConnection()).insert(marriageF);
    }

    public int getNumPersonsAdded() {
        return numPersonsAdded;
    }
    public void setNumPersonsAdded(int numPersonsAdded) {
        this.numPersonsAdded = numPersonsAdded;
    }

    public int getNumEventsAdded() {
        return numEventsAdded;
    }
    public void setNumEventsAdded(int numEventsAdded) {
        this.numEventsAdded = numEventsAdded;
    }
}
