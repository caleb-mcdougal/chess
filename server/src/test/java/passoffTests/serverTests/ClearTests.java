package passoffTests.serverTests;

import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.GameService;


public class ClearTests{

    @Test
    @DisplayName("Clear1")
    public void tryClear(){
        GameService gs = new GameService();
        gs.clear();
        int HMSize = MemoryUserDAO.getUserDBSize();
        Assertions.assertEquals(HMSize,0);
    }
}