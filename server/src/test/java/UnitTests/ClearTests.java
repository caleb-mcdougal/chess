package UnitTests;

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
        MemoryUserDAO mud = new MemoryUserDAO();
        int UserSize = mud.getDBSize();
        MemoryGameDAO mgd = new MemoryGameDAO();
//        int GameSize = mgd.getDBSize();
        MemoryAuthDAO mad = new MemoryAuthDAO();
        int AuthSize = mad.getDBSize();
        Assertions.assertEquals(UserSize,0);
//        Assertions.assertEquals(GameSize,0);
        Assertions.assertEquals(AuthSize,0);
    }
}