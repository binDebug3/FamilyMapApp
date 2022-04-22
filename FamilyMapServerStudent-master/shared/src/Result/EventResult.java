package Result;

public class EventResult {
    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public EventResult() {
        new EventResult("","","",0,0,"","","",0);
        success = false;
    }
    /**
     * Failure constructor
     */
    public EventResult(String message) {
        this.message = message;
        eventID = null;
        associatedUsername = null;
        personID = null;
        latitude = 0;
        longitude = 0;
        country = null;
        city = null;
        eventType = null;
        year = 0;
        success = false;
    }
    /**
     * Successful EventResult constructor
     * @param eventID unique ID for the event
     * @param associatedUsername username associated with the event
     * @param personID ID of the person associated with the event
     * @param latitude latitude of the event
     * @param longitude longitude of the event
     * @param country country where the event happens
     * @param city city where the event happens
     * @param eventType type of event
     * @param year year of the event
     */
    public EventResult(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        message = "";
        success = true;
    }

    public String getEventID() {
        return eventID;
    }
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventResult that = (EventResult) o;
        return Float.compare(that.latitude, latitude) == 0 && Float.compare(that.longitude, longitude) == 0 &&
                year == that.year && success == that.success && eventID.equals(that.eventID) &&
                associatedUsername.equals(that.associatedUsername) && personID.equals(that.personID) &&
                country.equals(that.country) && city.equals(that.city) && eventType.equals(that.eventType) &&
                message.equals(that.message);
    }
}
