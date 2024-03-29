package dataAccessTests.authTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Objects;

public class GetUsernameTest {

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
    @DisplayName("Positive getUsername test")
    public void getUsernamePositiveInsert(){
        SQLAuthDAO sad = new SQLAuthDAO();


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }


        String username = null;
        try{
            username = sad.getUsername(authToken);
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        if(!Objects.equals(username, "username")){
            System.out.println("usernames not the same");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative getUsername test")
    public void getUsernameNegative(){
        SQLAuthDAO sad = new SQLAuthDAO();


        UserData ud = new UserData("username", "password", "email");
        String authToken = null;
        try {
            authToken = sad.createAuth(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }


        String username = null;
        String authFake = "abc-def-123-456";
        try{
            username = sad.getUsername(authFake);
        } catch (DataAccessException e) {
            System.out.println("DAE correct");
            Assertions.assertTrue(true);
        }

        if(Objects.equals(username, "username")){
            System.out.println("usernames somehow found");
            Assertions.fail();
        }
    }
}
