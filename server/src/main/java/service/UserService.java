package service;

import dataAccess.*;
import dataAccess.Exceptions.AlreadyTakenException;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.UnauthorizedException;
import model.*;
import model.RequestResponse.LoginRequest;
import model.RequestResponse.LoginResponse;
import model.RequestResponse.RegisterRequest;
import model.RequestResponse.RegisterResponse;

import java.util.Objects;

public class UserService {

    public UserService() {

    }
    public RegisterResponse register(RegisterRequest request) throws AlreadyTakenException, BadRequestException {
        //Ensure username, password, and email are valid input from the user
        MemoryUserDAO mud = getMemoryUserDAO(request);

        //Create a new user
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        mud.createUser(newUser);

        //Create and return a new auth token
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(newUser);
        return new RegisterResponse(request.username(), authToken, null);
    }

    private static MemoryUserDAO getMemoryUserDAO(RegisterRequest request) throws BadRequestException, AlreadyTakenException {
        //Check user inputs given in request
        MemoryUserDAO mud = new MemoryUserDAO();
        if(request.username() == null || request.password() == null || request.email() == null || request.username().isBlank() || request.password().isBlank() || request.email().isBlank()){
            throw new BadRequestException("Must have all user data");
        }
        if(mud.userExists(request.username())){
            throw new AlreadyTakenException("This username is already taken");
        }
        return mud;
    }

    public LoginResponse login(LoginRequest request) throws UnauthorizedException { // removed: NoExistingUserException
        //Check valid username
        MemoryUserDAO mud = new MemoryUserDAO();
        if(!mud.userExists(request.username())){
            throw new UnauthorizedException("Username unrecognized");
        }

        //Check correct password
        UserData ud = mud.getUser(request.username());
        if(!Objects.equals(ud.password(), request.password())){
            throw new UnauthorizedException("Incorrect Password");
        }

        //Create new auth token
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        //Return login request object with new auth token
        return new LoginResponse(request.username(), authToken, null);
    }

    public void logout(String authToken) throws UnauthorizedException {
        //Remove given auth token from DB
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.deleteAuth(authToken);
    }
}