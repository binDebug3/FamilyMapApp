package Result;

/**
 * Hold data for results from fill result
 */
public class FillResult {
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public FillResult() {
        new FillResult("",false);
    }
    /**
     * FillResult constructor
     * @param message with more details
     * @param success is true if the operation succeeds, false otherwise
     */
    public FillResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillResult that = (FillResult) o;
        return success == that.success && message.equals(that.message);
    }
}
