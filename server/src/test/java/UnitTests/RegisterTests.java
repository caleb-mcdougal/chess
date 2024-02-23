package UnitTests;

import dataAccess.MemoryUserDAO;
import dataAccess.NoExistingUserException;
import dataAccess.UserTakenException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class RegisterTests {
    @Test
    @DisplayName("Simple Register")
    public void RegisterSingleUser() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");

        MemoryUserDAO mud = new MemoryUserDAO();
        int passSize1 = mud.getDBSize();
        int emailSize1 = mud.getEmailDBSize();

        try {
            if(!mud.userExists(ud)) {
                AuthData ad = us.register(ud);
            }
            else{
                throw new UserTakenException("This username is already taken");
            }
        }
        catch (UserTakenException e){
            System.out.println("UserTakenException caught: " + e.getMessage());
        }

        int passSize2 = mud.getDBSize();
        int emailSize2 = mud.getEmailDBSize();


        Assertions.assertEquals(passSize1 + 1, passSize2);
        Assertions.assertEquals(emailSize1 + 1, emailSize2);
    }
    @Test
    @DisplayName("Reregister User")
    public void DoubleRegister() {
        UserService us = new UserService();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        Assertions.assertThrows(UserTakenException.class, () -> {
            AuthData ad = us.register(ud);
            if(!mud.userExists(ud)) {
                AuthData ad2 = us.register(ud);
            }
            else{
                throw new UserTakenException("This username is already taken");
            }
        });
    }
}
