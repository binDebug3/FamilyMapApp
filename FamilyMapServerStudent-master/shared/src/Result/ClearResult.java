package Result;

/**
 * Hold data for results from clear result
 */
public class ClearResult {
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public ClearResult() {
        new ClearResult("",false);
    }
    /**
     * ClearResult constructor
     * @param message with more details
     * @param success is true if the operation succeeds, false otherwise
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearResult that = (ClearResult) o;
        return success == that.success && message.equals(that.message);
    }
}
