package dataAccessTests.gameTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.*;

public class CreateGameTest {
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
    @DisplayName("Positive CreateGame test")
    public void createGamePositiveInsert(){
        SQLGameDAO sgd = new SQLGameDAO();

        int rowCount1 = 0;
        int rowCount2 = 0;

        try {
            rowCount1 = sgd.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        String gameName = "game";
        try {
            sgd.createGame(gameName);
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

        try {
            rowCount2 = sgd.countRows();
        } catch (DataAccessException e) {
            System.out.println("row count 1 error");
            Assertions.fail();
        }

        if(rowCount2 != rowCount1 + 1){
            System.out.println("No size increase");
            Assertions.fail();
        }
    }

    //Negative case?

//    @Test
//    @DisplayName("Negative CreateGame test")
//    public void createGameNegativeInsert(){
//        SQLGameDAO sgd = new SQLGameDAO();
//
//        int rowCount1 = 0;
//        int rowCount2 = 0;
//
//        try {
//            rowCount1 = sgd.countRows();
//        } catch (DataAccessException e) {
//            System.out.println("row count 1 error");
//            Assertions.fail();
//        }
//
//        Assertions.assertThrows(DataAccessException.class, () -> sgd.createGame(null));
//
//        try {
//            rowCount2 = sgd.countRows();
//        } catch (DataAccessException e) {
//            System.out.println("row count 1 error");
//            Assertions.fail();
//        }
//
//        if(rowCount2 != rowCount1 + 1){
//            System.out.println("No size increase");
//            Assertions.fail();
//        }
//    }
}
