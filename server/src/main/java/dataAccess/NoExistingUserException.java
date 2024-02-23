package dataAccess;

public class NoExistingUserException extends Exception{
    public NoExistingUserException(String message) {
        super(message);
    }
}
