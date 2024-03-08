package dataAccessTests.authTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import dataAccess.SQLAuthDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class DeleteAuthTest {
    @BeforeEach
    public void clearAuthDB(){
        SQLAuthDAO sad = new SQLAuthDAO();
        try {
            sad.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }
    }


    @Test
    @DisplayName("Positive deleteAuth test")
    public void deleteAuthPositiveTest(){
        SQLAuthDAO sad = new SQLAuthDAO();

        int rowCount1 = 0;
        int rowCount2 = 0;


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            rowCount1 = sad.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        try {
            sad.deleteAuth(authToken);
        } catch (UnauthorizedException e) {
            System.out.println("UE");
            Assertions.fail();
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        try {
            rowCount2 = sad.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        if(rowCount2 != rowCount1 - 1){
            System.out.println("No size decrease");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative deleteAuth test")
    public void deleteAuthNegativeTest(){
        SQLAuthDAO sad = new SQLAuthDAO();

        int rowCount1 = 0;
        int rowCount2 = 0;


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            rowCount1 = sad.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        String fakeAuth = "123-abc-fake";
        try {
            sad.deleteAuth(fakeAuth);
        } catch (UnauthorizedException e) {
            System.out.println("UE");
            System.out.println("correctly caught nonexistent auth");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        try {
            rowCount2 = sad.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        if(rowCount2 == rowCount1 - 1){
            System.out.println("No size decrease");
            Assertions.fail();
        }
    }
}
