package DAO;

/**
 * Handles data access exceptions
 */
public class DataAccessException extends Exception {

    /**
     * DataAccessException constructor
     * @param message contains the error message
     */
    DataAccessException(String message)
    {
        super(message);
    }

    /**
     * Default constructor
     */
    DataAccessException()
    {
        super();
    }
}
