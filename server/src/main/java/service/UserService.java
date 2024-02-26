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
    public RegisterResponse register(RegisterRequest request) throws AlreadyTakenException, BadRequestException {      //Test this
        MemoryUserDAO mud = new MemoryUserDAO();
        if(request.username() == null || request.password() == null || request.email() == null || request.username().isBlank() || request.password().isBlank() || request.email().isBlank()){
            throw new BadRequestException("Must have all user data");
        }
        if(mud.userExists(request.username())){
            throw new AlreadyTakenException("This username is already taken");
        }

        UserData newUser = new UserData(request.username(), request.password(), request.email());
        mud.createUser(newUser);

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(newUser);
        return new RegisterResponse(request.username(), authToken, null);
    }
    public LoginResponse login(LoginRequest request) throws UnauthorizedException { // removed: NoExistingUserException

        MemoryUserDAO mud = new MemoryUserDAO();
        if(!mud.userExists(request.username())){
            throw new UnauthorizedException("Username unrecognized");
        }

        UserData ud = mud.getUser(request.username());
        if(!Objects.equals(ud.password(), request.password())){        //Check password
            throw new UnauthorizedException("Incorrect Password");
        }

        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        return new LoginResponse(request.username(), authToken, null);
    }

    public void logout(String authToken) throws UnauthorizedException { // auth must link to user for join game
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.deleteAuth(authToken);
    }
}