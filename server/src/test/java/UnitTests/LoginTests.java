package UnitTests;

import dataAccess.BadRequestException;
import dataAccess.UnauthorizedException;
import dataAccess.MemoryUserDAO;
import dataAccess.AlreadyTakenException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class LoginTests {
    @Test
    @DisplayName("Login Correctly")
    public void SimpleLogin() {
        UserService us = new UserService();
        RegisterRequest registerRequest = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        RegisterResponse registerResponse;
        LoginRequest request = new LoginRequest("Caleb", "123abc");
        LoginResponse response;
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            registerResponse = us.register(registerRequest);
        }
        catch(AlreadyTakenException e){
            System.out.println("Failed due to register1 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e){
            System.out.println("Failed due to register1 bad request");
            Assertions.fail();
        }

        try{
            response = us.login(request);
            Assertions.assertTrue(true);
        }
        catch (UnauthorizedException e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }

        mud.clear();

    }

    @Test
    @DisplayName("Login No Existing User")
    public void NoUser() {
        UserService us = new UserService();
        RegisterRequest rr = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        RegisterResponse rrp;
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            rrp = us.register(rr);
        }
        catch (AlreadyTakenException e) {
            System.out.println("Failed due to register2 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e){
            System.out.println("Failed due to register2 bad request");
            Assertions.fail();
        }


        LoginRequest lr = new LoginRequest("McDougal", "123abc");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            us.login(lr);
        });

        mud.clear();
    }

    @Test
    @DisplayName("Login Incorrect Password")
    public void BadPassword() {
        UserService us = new UserService();
        RegisterRequest rr = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        RegisterResponse rrp;
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            rrp = us.register(rr);
        }
        catch (AlreadyTakenException e) {
            System.out.println("Failed due to register3 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("Failed due to register3 bad request");
            Assertions.fail();
        }

        LoginRequest lr = new LoginRequest("Caleb", "321xyz");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            us.login(lr);
        });

        mud.clear();
    }
}
