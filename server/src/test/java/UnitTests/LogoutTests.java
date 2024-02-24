package UnitTests;

import dataAccess.BadRequestException;
import dataAccess.MemoryUserDAO;
import dataAccess.Unauthorized;
import dataAccess.UserTakenException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class LogoutTests {
    @Test
    @DisplayName("Logout Correctly")
    public void SimpleLogout() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        AuthData ad = new AuthData("", "");

        try {
            AuthData adr = us.register(ud);
        }
        catch(UserTakenException e){
            System.out.println("Failed due to register1 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("Failed due to register1 bad request");
            Assertions.fail();
        }

        try{
            ad = us.login(ud);
            System.out.println("login");
            System.out.println(ad.authToken());
        }
//        catch (NoExistingUserException e){
//            System.out.println("No existing user");
//            Assertions.fail();
//        }
        catch (Unauthorized e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }


        try{
            us.logout(ad.authToken());
        } catch (Unauthorized ex) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }

        mud.clear();
    }

    @Test
    @DisplayName("Logout Invalid AuthToken")
    public void FailLogout() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        AuthData ad = new AuthData("123", "");

        try {
            us.register(ud);
        }
        catch(UserTakenException e){
            System.out.println("Failed due to register1 user taken");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("Failed due to register1 bad request");
            Assertions.fail();
        }

        try{
            us.login(ud);
        }
//        catch (NoExistingUserException e){
//            System.out.println("No existing user");
//            Assertions.fail();
//        }
        catch (Unauthorized e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }


        try{
            us.logout(ad.authToken());
            Assertions.fail();
        } catch (Unauthorized ex) {
            System.out.println("Incorrect Password");
            Assertions.assertTrue(true);
        }

        mud.clear();
    }
}
