package dataAccess;

import dataAccess.AuthDAO;
import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;

public class SQLAuthDAO extends SQLDAOParent implements AuthDAO {
    @Override
    public String getUsername(String authToken) {
        return null;
    }

    @Override
    public boolean authExists(String authToken) throws UnauthorizedException {
        return false;
    }

    @Override
    public String createAuth(UserData ud) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {

    }

    @Override
    public void clear() {

    }
}
