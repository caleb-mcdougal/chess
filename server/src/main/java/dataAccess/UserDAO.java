package dataAccess;


import model.UserData;

import java.util.HashMap;

public interface UserDAO {
    void clear();
    void createUser(UserData ud);
    UserData getUser(String username);
    Boolean userExists(String username);
}
