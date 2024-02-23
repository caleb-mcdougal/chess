package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.NoExistingUserException;
import dataAccess.UserTakenException;
import model.AuthData;
import model.UserData;

public class UserService {

    public UserService() {

    }
    public AuthData register(UserData user) throws UserTakenException{      //Test this
        MemoryUserDAO mud = new MemoryUserDAO();
        if(mud.userExists(user)){
            throw new UserTakenException("This username is already taken");
        }

        mud.createUser(user);

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth();
        return new AuthData(authToken, user.username());
    }
    public AuthData login(UserData user) {
        MemoryUserDAO mud = new MemoryUserDAO();
        UserData pullUser = mud.getUser(user);
        return null;
    }
//    public void logout(UserData user) {}
}