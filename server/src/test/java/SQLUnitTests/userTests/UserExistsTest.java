package SQLUnitTests.userTests;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class UserExistsTest {
    @BeforeEach
    public void clearGameDB(){
        SQLUserDAO sud = new SQLUserDAO();
        try {
            sud.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Positive userExists test")
    public void userExistsPositiveTest(){
        SQLUserDAO sud = new SQLUserDAO();

        UserData ud = new UserData("username", "password", "email");
        try {
            sud.createUser(ud);
        } catch (DataAccessException | BadRequestException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        try{
            if(sud.userExists("username")){
                System.out.println("Correctly identified existing user");
                Assertions.assertTrue(true);
            }
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative userExists test")
    public void userExistsNegativeTest(){
        SQLUserDAO sud = new SQLUserDAO();

        UserData ud = new UserData("username", "password", "email");
        try {
            sud.createUser(ud);
        } catch (DataAccessException | BadRequestException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        try{
            if(!sud.userExists("fakeUsername")){
                System.out.println("Correctly identified non existing user");
                Assertions.assertTrue(true);
            }
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }
    }
}
