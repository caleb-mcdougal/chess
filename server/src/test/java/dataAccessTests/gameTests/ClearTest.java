package dataAccessTests.gameTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLGameDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearTest {
    @Test
    @DisplayName("Positive clear test")
    public void clearTest(){
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            sgd.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

    }
}
