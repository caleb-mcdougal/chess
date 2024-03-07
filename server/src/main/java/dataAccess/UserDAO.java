package dataAccess;


import dataAccess.Exceptions.DataAccessException;
import model.UserData;

import java.util.HashMap;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData ud);
    UserData getUser(String username);
    Boolean userExists(String username);
}
