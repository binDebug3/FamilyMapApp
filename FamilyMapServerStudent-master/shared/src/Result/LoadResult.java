package Result;

/**
 * Hold data for results from load result
 */
public class LoadResult {
    private boolean success;
    String message;

    /**
     * Default constructor
     */
    public LoadResult() {
        new LoadResult("", false);
    }
    /**
     * LoadResult constructor
     * @param message with more details
     * @param success is true if the operation succeeds, false otherwise
     */
    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
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
        LoadResult that = (LoadResult) o;
        return success == that.success && this.message.equals(that.message);
    }
}
