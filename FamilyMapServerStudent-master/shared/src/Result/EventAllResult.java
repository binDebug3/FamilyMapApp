package Result;

import Model.Event;

import java.util.Arrays;

public class EventAllResult {
    private Event[] data;
    private String message;
    private boolean success;

    /**
     * Default Constructor
     */
    public EventAllResult() {
        new EventAllResult("");
    }
    /**
     * Failure constructor
     * @param message message with more details
    */
    public EventAllResult(String message) {
        this.message = message;
        data = null;
        success = false;
    }
    /**
     * Successful Constructor
     * @param data array of Event objects
     */
    public EventAllResult(Event[] data) {
        this.data = data;
        message = "";
        success = true;
    }

    public Event[] getEvents() {
        return data;
    }
    public void setEvents(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
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
        EventAllResult that = (EventAllResult) o;
        return success == that.success && Arrays.equals(data, that.data) && message.equals(that.message);
    }
}
