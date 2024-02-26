package UnitTests;

import dataAccess.BadRequestException;
import dataAccess.MemoryUserDAO;
import dataAccess.UnauthorizedException;
import dataAccess.AlreadyTakenException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class LogoutTests {
    @Test
    @DisplayName("Logout Correctly")
    public void SimpleLogout() {
        UserService us = new UserService();
        RegisterRequest rr = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        LoginRequest lrq = new LoginRequest("Caleb", "123abc");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();



        try {
            us.register(rr);
        }
        catch(AlreadyTakenException e){
            System.out.println("Failed due to register1 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("Failed due to register1 bad request");
            Assertions.fail();
        }

        LoginResponse lrp = null;
        try{
            lrp = us.login(lrq);
            System.out.println("login");
            System.out.println(lrp.authToken());
        }
//        catch (NoExistingUserException e){
//            System.out.println("No existing user");
//            Assertions.fail();
//        }
        catch (UnauthorizedException e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }


        try{
            us.logout(lrp.authToken());
        } catch (UnauthorizedException ex) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }

        mud.clear();
    }

    @Test
    @DisplayName("Logout Invalid AuthToken")
    public void FailLogout() {
        UserService us = new UserService();
        RegisterRequest rr = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        LoginRequest lrq = new LoginRequest("Caleb", "123abc");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();



        try {
            us.register(rr);
        }
        catch(AlreadyTakenException e){
            System.out.println("Failed due to register1 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("Failed due to register1 bad request");
            Assertions.fail();
        }

        try{
            us.login(lrq);
        }
        catch (UnauthorizedException e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }

        AuthData ad = new AuthData("123", "");
        try{
            us.logout(ad.authToken());
            Assertions.fail();
        } catch (UnauthorizedException ex) {
            System.out.println("Incorrect Password");
            Assertions.assertTrue(true);
        }

        mud.clear();
    }
}
