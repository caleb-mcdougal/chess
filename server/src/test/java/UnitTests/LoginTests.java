package UnitTests;

import dataAccess.Unauthorized;
import dataAccess.MemoryUserDAO;
import dataAccess.NoExistingUserException;
import dataAccess.UserTakenException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class LoginTests {
    @Test
    @DisplayName("Login Correctly")
    public void SimpleLogin() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            AuthData ad = us.register(ud);
        }
        catch(UserTakenException e){
            System.out.println("Failed due to register1");
            Assertions.fail();
        }

        try{
            AuthData ad = us.login(ud);
            Assertions.assertTrue(true);
        }
        catch (Unauthorized e) {
            System.out.println("Incorrect Password");
            Assertions.fail();
        }

        mud.clear();

    }

    @Test
    @DisplayName("Login No Existing User")
    public void NoUser() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            AuthData ad = us.register(ud);
        } catch (UserTakenException e) {
            System.out.println("Failed due to register2");
            Assertions.fail();
        }

        UserData ud2 = new UserData("McDougal", "123abc", "cdm@gmail.com");
        Assertions.assertThrows(Unauthorized.class, () -> {
            us.login(ud2);
        });

        mud.clear();
    }

    @Test
    @DisplayName("Login Incorrect Password")
    public void BadPassword() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        mud.clear();

        try {
            AuthData ad = us.register(ud);
        } catch (UserTakenException e) {
            System.out.println("Failed due to register3");
            Assertions.fail();
        }

        UserData ud2 = new UserData("Caleb", "321xyz", "cdm@gmail.com");
        Assertions.assertThrows(Unauthorized.class, () -> {
            us.login(ud2);
        });

        mud.clear();
    }
}