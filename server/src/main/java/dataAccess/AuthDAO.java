package dataAccess;

public interface AuthDAO {

    boolean validAuth(String authToken);
    String createAuth();
    void deleteAuth();
    void clear();
}
