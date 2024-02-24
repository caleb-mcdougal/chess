package UnitTests;

import chess.ChessGame;
import dataAccess.BadRequestException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.Unauthorized;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class ListGamesTests {

    @Test
    @DisplayName("List Games Positive Case")
    public void ListGameSuccess(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();
        MemoryGameDAO mgd = new MemoryGameDAO();


        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        GameData gd = new GameData(0, "user1", "user2", "game1", game);
        try{
            gs.createGame(gd, authToken);
            gs.createGame(gd, authToken);
            gs.createGame(gd, authToken);
        }
        catch (Unauthorized e){
            System.out.println("Unaothorized create game");
            Assertions.fail();
        }
        catch(BadRequestException e){
            System.out.println("bad request create game");
            Assertions.fail();
        }

        try {
            String gamesList = gs.listGames(authToken);
            System.out.println(gamesList);
        }
        catch (Unauthorized e){
            System.out.println("Unauthorized list games request");
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("List Games negative Case")
    public void ListGameFailure(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();
        MemoryGameDAO mgd = new MemoryGameDAO();


        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        GameData gd = new GameData(0, "user1", "user2", "game1", game);
        try{
            gs.createGame(gd, authToken);
            gs.createGame(gd, authToken);
            gs.createGame(gd, authToken);
        }
        catch (Unauthorized e){
            System.out.println("Unaothorized create game");
            Assertions.fail();
        }
        catch(BadRequestException e){
            System.out.println("bad request create game");
            Assertions.fail();
        }

        try {
            String gamesList = gs.listGames("abc-123");
            System.out.println(gamesList);
        }
        catch (Unauthorized e){
            System.out.println("Unauthorized list games request");
            Assertions.assertTrue(true);
        }
    }
}
