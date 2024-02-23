package dataAccess;

public interface AuthDAO {

    boolean validAuth(String authToken);
    String createAuth();
    void clear();
    int getDBSize();
}
