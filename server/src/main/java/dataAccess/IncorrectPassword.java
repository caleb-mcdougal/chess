package dataAccess;

public class IncorrectPassword extends Exception{
    public IncorrectPassword(String message) {
        super(message);
    }
}
