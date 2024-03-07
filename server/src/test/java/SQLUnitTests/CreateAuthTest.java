package SQLUnitTests;

import dataAccess.DatabaseManager;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAuthTest {
    @Test
    @DisplayName("Positive no errors createAuth test")
    public void createAuthPositive(){
        SQLAuthDAO sad = new SQLAuthDAO();
        UserData ud = new UserData("username", "password", "email");
        try {
            sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Positive with insert check createAuth test")
    public void createAuthPositiveInsert(){
        SQLAuthDAO sad = new SQLAuthDAO();

        int rowCount1 = 0;
        int rowCount2 = 0;

        try {
            rowCount1 = sad.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        UserData ud = new UserData("username", "password", "email");
        String authToken;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            rowCount2 = sad.countRows();
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