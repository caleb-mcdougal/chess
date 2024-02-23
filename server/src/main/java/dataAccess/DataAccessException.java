package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{     //There will be other exceptions that may be thown in services etc that will be caught in the handler and it can manage what to do with that
    public DataAccessException(String message) {
        super(message);
    }
}
