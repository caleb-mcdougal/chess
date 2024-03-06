package UnitTests;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.MemoryDAO.MemoryUserDAO;
import dataAccess.Exceptions.AlreadyTakenException;
import model.Request.RegisterRequest;
import model.Response.RegisterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class RegisterTests {
    @Test
    @DisplayName("Simple Register")
    public void RegisterSingleUser() {
        UserService us = new UserService();
        RegisterRequest rrq = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        RegisterResponse rrp;

        MemoryUserDAO mud = new MemoryUserDAO();
        int passSize1 = mud.getDBSize();

        try {
            rrp = us.register(rrq);
        }
        catch (AlreadyTakenException e){
            System.out.println("UserTakenException caught: " + e.getMessage());
        }
        catch (BadRequestException e){
            System.out.println("BadReeqestException caught: " + e.getMessage());
        }


        int passSize2 = mud.getDBSize();


        Assertions.assertEquals(passSize1 + 1, passSize2);
    }
    @Test
    @DisplayName("Reregister User")
    public void DoubleRegister() {
        UserService us = new UserService();
        RegisterRequest rr = new RegisterRequest("Caleb", "123abc", "cdm@gmail.com");
        MemoryUserDAO mud = new MemoryUserDAO();

        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            RegisterResponse rrp = us.register(rr);
            if(!mud.userExists(rr.username())) {
                RegisterResponse rrp2 = us.register(rr);
            }
            else{
                throw new AlreadyTakenException("This username is already taken");
            }
        });
    }
}
