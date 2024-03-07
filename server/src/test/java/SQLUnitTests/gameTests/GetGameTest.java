package SQLUnitTests.gameTests;

import chess.ChessGame;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class GetGameTest {

    @BeforeAll
    public static void clearGameDB(){
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            sgd.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Positive getGame test")
    public void getGamePositiveInsert(){
        SQLGameDAO sgd = new SQLGameDAO();


        String gameName = "name";
        int gameID = 0;
        try {
            gameID = sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }


        GameData gd = null;
        try{
            gd = sgd.getGame(gameID);
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        } catch (BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        }

        GameData gdTest = new GameData(gameID, null, null, "name", new ChessGame());

        if(!Objects.equals(gd, gdTest)){
            System.out.println("usernames not the same");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative getGame test")
    public void getGameNegativeInsert(){
        SQLGameDAO sgd = new SQLGameDAO();


        String gameName = "name";
        int gameID = 0;
        try {
            gameID = sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }


        GameData gd = null;
        try{
            gd = sgd.getGame(gameID);
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        } catch (BadRequestException e) {
            System.out.println("BRE");
            Assertions.fail();
        }

        GameData gdTest = new GameData(gameID, "whiteUsername", null, "name", new ChessGame());

        if(Objects.equals(gd, gdTest)){
            System.out.println("usernames not the same");
            Assertions.fail();
        }
    }
}
