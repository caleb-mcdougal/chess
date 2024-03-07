package SQLUnitTests;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.SQLAuthDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearTest {
    @Test
    @DisplayName("Positive clear test")
    public void clearTest(){
        SQLAuthDAO sad = new SQLAuthDAO();
        try {
            sad.clear();
        } catch (DataAccessException e) {
            System.out.println("DataAccessException thrown");
            Assertions.fail();
        }

    }
}
