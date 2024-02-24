package UnitTests;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.Unauthorized;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class CreateGameTests {

    @Test
    @DisplayName("Create Game Positive Case")
    public void CreateGameSuccess(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();
        MemoryGameDAO mgd = new MemoryGameDAO();

        int GameSizePre = mgd.getDBSize();

        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        GameData gd = new GameData(0, "user1", "user2", "game1", game);
        try{
            gs.createGame(gd, authToken);
        }
        catch (Unauthorized e){
            System.out.println("Unaothorized create game");
            Assertions.fail();
        }

        int GameSizePost = mgd.getDBSize();

        Assertions.assertEquals(GameSizePre + 1,GameSizePost);
    }
}
