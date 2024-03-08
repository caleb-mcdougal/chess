package DataAccessTests.authTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import dataAccess.SQLAuthDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class AuthExistsTest {

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
    @DisplayName("Positive authExists test")
    public void authExistsPositiveTest(){
        SQLAuthDAO sad = new SQLAuthDAO();


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            if (sad.authExists(authToken)) {
                Assertions.assertTrue(true);
            }
            else {
                System.out.println("auth not recognized");
                Assertions.fail();
            }
        } catch (UnauthorizedException e) {
            System.out.println("UE");
            Assertions.fail();
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative authExists test")
    public void authExistsNegativeTest(){
        SQLAuthDAO sad = new SQLAuthDAO();


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        String fakeAuth = "abc-123-fake";
        try {
            if (sad.authExists(fakeAuth)) {
                Assertions.fail();
            }
            else {
                System.out.println("auth not recognized");
                Assertions.assertTrue(true);
            }
        } catch (UnauthorizedException e) {
            System.out.println("UE");
            System.out.println("auth not recognized");
            Assertions.assertTrue(true);
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }
    }
}
