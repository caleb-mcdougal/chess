package UnitTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class AuthDAOTests {

    @Test
    @DisplayName("Create unique authTokens")
    public void createAuth(){
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String auth1 = mad.createAuth();
        System.out.println(auth1);
        String auth2 = mad.createAuth();
        System.out.println(auth2);
        Assertions.assertNotEquals(auth1,auth2);
    }

    @Test
    @DisplayName("Check authTokens")
    public void checkAuth(){
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String auth1 = mad.createAuth();
        System.out.println(auth1);
        Assertions.assertTrue(mad.validAuth(auth1));
    }
}
