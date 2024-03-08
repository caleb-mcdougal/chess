package dataAccessTests.userTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearTest {
    @Test
    @DisplayName("Positive clear test")
    public void clearTest(){
        SQLUserDAO sud = new SQLUserDAO();
        try {
            sud.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

    }
}
