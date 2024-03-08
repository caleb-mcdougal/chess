package dataAccessTests.gameTests;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class UpdateGameTest {
    @BeforeEach
    public void clearGameDB(){
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            sgd.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Positive UpdateGame test")
    public void UpdateGamePositiveTest(){
        SQLGameDAO sgd = new SQLGameDAO();

        String gameName = "game";
        int gameID = 0;
        try {
            gameID = sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try{
            sgd.updateGame(gameID, "BLACK", "Caleb");
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }
        catch (BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        }

        GameData gd = null;
        try{
            gd = sgd.getGame(gameID);
        } catch (BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        if(!Objects.equals(gd.blackUsername(), "Caleb") || !Objects.equals(gd.whiteUsername(), null)){
            System.out.println("username mismatch");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative UpdateGame test")
    public void UpdateGameNegativeTest(){
        SQLGameDAO sgd = new SQLGameDAO();

        String gameName = "game";
        int gameID = 0;
        try {
            gameID = sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try{
            sgd.updateGame(gameID, "BLUE", "Caleb");
        } catch (DataAccessException e) {
            System.out.println("Correctly caught incorrect input");
            Assertions.assertTrue(true);
        }
        catch(BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        }

    }
}
