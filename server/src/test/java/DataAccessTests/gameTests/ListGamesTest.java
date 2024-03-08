package DataAccessTests.gameTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ListGamesTest {

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
    @DisplayName("Positive listGame test")
    public void ListGamePositive(){
        SQLGameDAO sgd = new SQLGameDAO();

        String gameName = "game1";
        try {
            sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        gameName = "game2";
        try {
            sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        gameName = "game3";
        try {
            sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException or bad request thrown");
            Assertions.fail();
        }

        GameData[] gameList = new GameData[3];
        try{
            gameList = sgd.listGames();
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        if(!Objects.equals(gameList[0].gameName(), "game1")){
            System.out.println("wrong first game name");
            Assertions.fail();
        }
        if(!Objects.equals(gameList[1].gameName(), "game2")){
            System.out.println("wrong first game name");
            Assertions.fail();
        }
        if(!Objects.equals(gameList[2].gameName(), "game3")){
            System.out.println("wrong first game name");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Negative listGame test")
    public void ListGameNegative(){
        SQLGameDAO sgd = new SQLGameDAO();


        GameData[] gameList = new GameData[0];
        try{
            gameList = sgd.listGames();
        } catch (DataAccessException e) {
            System.out.println("DAE");
            Assertions.fail();
        }

        if(gameList.length > 0){
            System.out.println("Game List should be 0");
            Assertions.fail();
        }
    }
}
