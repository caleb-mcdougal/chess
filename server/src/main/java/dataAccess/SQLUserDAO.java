package dataAccess;

import dataAccess.UserDAO;
import model.UserData;

public class SQLUserDAO extends SQLDAOParent implements UserDAO {
    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData ud) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public Boolean userExists(String username) {
        return null;
    }
}