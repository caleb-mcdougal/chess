package UnitTests;

import chess.ChessGame;
import dataAccess.BadRequestException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.UnauthorizedException;
import model.CreateGameRequest;
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

        CreateGameRequest cgr = new CreateGameRequest("game1");
        try{
            gs.createGame(cgr, authToken);
        }
        catch (UnauthorizedException e){
            System.out.println("Unaothorized create game");
            Assertions.fail();
        }
        catch(BadRequestException e){
            System.out.println("bad request create game");
            Assertions.fail();
        }

        int GameSizePost = mgd.getDBSize();

        Assertions.assertEquals(GameSizePre + 1,GameSizePost);
    }

    @Test
    @DisplayName("Create Game Negative Case")
    public void CreateGameFailure(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();


        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        CreateGameRequest cgr = new CreateGameRequest("game1");
        try{
            gs.createGame(cgr, "abc-123");
        }
        catch (UnauthorizedException e){
            System.out.println("Unaothorized create game");
            Assertions.assertTrue(true);
        }
        catch(BadRequestException e){
            System.out.println("bad request create game");
            Assertions.fail();
        }


    }

    @Test
    @DisplayName("Create Game Negative Case Bad Request")
    public void CreateGameFailureBadRequest(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();

        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        CreateGameRequest cgr = new CreateGameRequest("");
        try{
            gs.createGame(cgr, authToken);
        }
        catch (UnauthorizedException e){
            System.out.println("Unaothorized create game");
            Assertions.fail();
        }
        catch(BadRequestException e){
            System.out.println("bad request create game");
            Assertions.assertTrue(true);
        }


    }
}
