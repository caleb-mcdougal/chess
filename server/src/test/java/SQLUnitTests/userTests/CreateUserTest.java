package SQLUnitTests.userTests;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreateUserTest {

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
    @DisplayName("Positive CreateUser test")
    public void createUserPositiveInsert(){
        SQLUserDAO sud = new SQLUserDAO();

        int rowCount1 = 0;
        int rowCount2 = 0;

        try {
            rowCount1 = sud.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        UserData ud = new UserData("username", "password", "email");
        try {
            sud.createUser(ud);
        } catch (DataAccessException | BadRequestException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            rowCount2 = sud.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        if(rowCount2 != rowCount1 + 1){
            System.out.println("No size increase");
            Assertions.fail();
        }
    }
}
