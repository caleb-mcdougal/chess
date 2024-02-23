package UnitTests;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class AuthDAOTests {

    @Test
    @DisplayName("Create unique authTokens")
    public void createAuth(){
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String auth1 = mad.createAuth(ud);
        String auth2 = mad.createAuth(ud);
        Assertions.assertNotEquals(auth1,auth2);
    }

    @Test
    @DisplayName("Check authTokens")
    public void checkAuth(){
        MemoryAuthDAO mad = new MemoryAuthDAO();
        UserData ud = new UserData("Caleb", "123abc", "cdm@gmail.com");
        String auth1 = mad.createAuth(ud);
        Assertions.assertTrue(mad.validAuth(auth1, ud));
    }
}
