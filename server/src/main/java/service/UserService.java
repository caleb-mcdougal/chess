package service;

import dataAccess.Exceptions.AlreadyTakenException;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import model.*;
import model.Request.LoginRequest;
import model.Response.LoginResponse;
import model.Request.RegisterRequest;
import model.Response.RegisterResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

public class UserService {

    public UserService() {

    }
    public RegisterResponse register(RegisterRequest request) throws AlreadyTakenException, BadRequestException, DataAccessException {
        //Ensure username is valid input from the user
        SQLUserDAO sud = new SQLUserDAO();
        if(sud.userExists(request.username())){
            throw new AlreadyTakenException("Username unrecognized");
        }

        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadRequestException("Fill in all fields");
        }

        //Create a new user
        UserData newUser = new UserData(request.username(), request.password(), request.email());
        sud.createUser(newUser);

        //Create and return a new auth token
        SQLAuthDAO sad = new SQLAuthDAO();
        String authToken = sad.createAuth(newUser);
        return new RegisterResponse(request.username(), authToken, null);
    }

    public LoginResponse login(LoginRequest request) throws UnauthorizedException, DataAccessException, BadRequestException { // removed: NoExistingUserException
        //Check valid username
        SQLUserDAO sud = new SQLUserDAO();
        if(!sud.userExists(request.username())){
            throw new UnauthorizedException("Username unrecognized");
        }

        //Check correct password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserData ud = sud.getUser(request.username());

        if(!encoder.matches(request.password(), ud.password())){
            throw new UnauthorizedException("Incorrect Password");
        }



        //Create new auth token
        SQLAuthDAO sad = new SQLAuthDAO();
        String authToken = sad.createAuth(ud);
        //Return login request object with new auth token
        return new LoginResponse(request.username(), authToken, null);
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        //Remove given auth token from DB
        SQLAuthDAO sad = new SQLAuthDAO();
        sad.deleteAuth(authToken);
    }
}