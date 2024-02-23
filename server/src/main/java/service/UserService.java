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
        String authToken = mad.createAuth();
        return new AuthData(authToken, user.username());
    }
    public AuthData login(UserData user) throws NoExistingUserException, IncorrectPassword{

        MemoryUserDAO mud = new MemoryUserDAO();
        if(!mud.userExists(user)){
            throw new NoExistingUserException("Username unrecognized");
        }

        UserData ud = mud.getUser(user);
        if(!Objects.equals(ud.password(), user.password())){        //Check password
            throw new IncorrectPassword("Incorrect Password");
        }

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth();

        return new AuthData(authToken, user.username());
    }

//    public void logout(UserData user) {}
}