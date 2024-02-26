package dataAccess;

import model.UserData;

public interface AuthDAO {

    public boolean validAuth(String authToken, UserData ud);
    public String getUsername(String authToken);
    public boolean authExists(String authToken)throws UnauthorizedException;
    public String createAuth(UserData ud);
    void deleteAuth(String authToken)throws UnauthorizedException;
    void clear();
}
