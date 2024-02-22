package passoffTests.serverTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import service.GameService;


public class ClearTests{

    @Test
    @DisplayName("Clear All Data")
    public void tryClear(){
        GameService gs = new GameService();
        gs.clear();
        int UserSize = MemoryUserDAO.getDBSize();
        int GameSize = MemoryGameDAO.getDBSize();
        int AuthSize = MemoryAuthDAO.getDBSize();
        Assertions.assertEquals(UserSize,0);
        Assertions.assertEquals(GameSize,0);
        Assertions.assertEquals(AuthSize,0);
    }
}