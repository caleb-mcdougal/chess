package dataAccess;


import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import model.UserData;

import java.util.HashMap;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData ud) throws DataAccessException;
    UserData getUser(String username) throws BadRequestException, DataAccessException;
    Boolean userExists(String username) throws DataAccessException;
}
