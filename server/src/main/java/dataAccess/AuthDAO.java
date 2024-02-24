package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public boolean validAuth(String authToken, UserData ud);
    public String getUsername(String authToken);
    public boolean authExists(String authToken)throws Unauthorized;
    public String createAuth(UserData ud);
    void deleteAuth(String authToken)throws Unauthorized;
    void clear();
}
