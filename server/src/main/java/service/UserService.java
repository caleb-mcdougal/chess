package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserTakenException;
import model.AuthData;
import model.UserData;

public class UserService {

    public UserService() {

    }
    public AuthData register(UserData user) throws UserTakenException{      //Test this
        MemoryUserDAO mud = new MemoryUserDAO();
        mud.getUser(user);
        mud.createUser(user);

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth();
        return new AuthData(authToken, user.username());
    }
//    public AuthData login(UserData user) {}
//    public void logout(UserData user) {}
}