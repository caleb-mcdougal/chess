package SQLUnitTests.userTests;

import chess.ChessGame;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GetUserTest {

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
    @DisplayName("Positive getUser test")
    public void getUserPositiveTest(){
        SQLUserDAO sud = new SQLUserDAO();


        UserData ud = new UserData("username", "password", "email");
        try {
            sud.createUser(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        UserData udGot = null;
        try{
            udGot = sud.getUser("username");
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        } catch (BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        }


        if(!Objects.equals(ud, udGot)){
            System.out.println("UD not the same");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative getUser test")
    public void getUserNegativeTest(){
        SQLUserDAO sud = new SQLUserDAO();


        UserData ud = new UserData("username", "password", "email");
        try {
            sud.createUser(ud);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        try{
            sud.getUser("NotUsername");
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        } catch (BadRequestException e) {
            System.out.println("BRE");
            System.out.println("Correctly identified non existent username");
            Assertions.assertTrue(true);
        }


    }
}
