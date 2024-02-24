package UnitTests;

import dataAccess.BadRequestException;
import dataAccess.MemoryUserDAO;
import dataAccess.AlreadyTakenException;
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
            AuthData ad = us.register(ud);
        }
        catch (AlreadyTakenException e){
            System.out.println("UserTakenException caught: " + e.getMessage());
        }
        catch (BadRequestException e){
            System.out.println("BadReeqestException caught: " + e.getMessage());
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

        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            AuthData ad = us.register(ud);
            if(!mud.userExists(ud)) {
                AuthData ad2 = us.register(ud);
            }
            else{
                throw new AlreadyTakenException("This username is already taken");
            }
        });
    }
}
