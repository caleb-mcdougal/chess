package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

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
        String authToken = mad.createAuth(user);
        return new AuthData(authToken, user.username());
    }
    public AuthData login(UserData user) throws Unauthorized { // removed: NoExistingUserException

        MemoryUserDAO mud = new MemoryUserDAO();
        if(!mud.userExists(user)){
            throw new Unauthorized("Username unrecognized");
        }

        UserData ud = mud.getUser(user);
        if(!Objects.equals(ud.password(), user.password())){        //Check password
            throw new Unauthorized("Incorrect Password");
        }

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(user);

        return new AuthData(authToken, user.username());
    }

    public void logout(String authToken) throws Unauthorized{ // auth must link to user for join game
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.deleteAuth(authToken);
    }
}