package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;

public interface AuthDAO {

    public String getUsername(String authToken) throws DataAccessException;
    public boolean authExists(String authToken)throws UnauthorizedException, DataAccessException;
    public String createAuth(UserData ud) throws DataAccessException;
    void deleteAuth(String authToken)throws UnauthorizedException, DataAccessException;
    void clear() throws DataAccessException;
}
