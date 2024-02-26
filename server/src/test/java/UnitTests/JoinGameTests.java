package UnitTests;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class JoinGameTests {

    @Test
    @DisplayName("Join Game Positive Case")
    public void JoinGameSuccess(){
        GameService gs = new GameService();

        gs.clear();

        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        CreateGameRequest cgr = new CreateGameRequest("g1");
        CreateGameResponse cgrp = null;
        try{
            gs.createGame(cgr, authToken);
            cgrp = gs.createGame(cgr, authToken);
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

        JoinGameRequest jgr = new JoinGameRequest("WHITE", cgrp.gameID());

        try {
            gs.joinGame(jgr,authToken);
        }
        catch (UnauthorizedException e){
            System.out.println("Unauthorized list games request");
            Assertions.fail();
        }
        catch(AlreadyTakenException e){
            System.out.println("Already taken join game test 1 fail");
            Assertions.fail();
        }
        catch (BadRequestException e){
            System.out.println("Bad request join game test 1 fail");
            Assertions.fail();
        }

        MemoryGameDAO mgd = new MemoryGameDAO();
        GameData gd = new GameData(0, null, null, null, new ChessGame());
        try {
            gd = mgd.getGame(cgrp.gameID());
        }
        catch(BadRequestException e){
            System.out.println("Bad request check game test 1 fail");
            Assertions.fail();
        }

        Assertions.assertEquals(gd.whiteUsername(),"username");

    }

    @Test
    @DisplayName("Join Game Negative Case")
    public void JoinGameFailure(){
        GameService gs = new GameService();

        UserData ud = new UserData("username", "password", "email");
        MemoryAuthDAO mad = new MemoryAuthDAO();
        String authToken = mad.createAuth(ud);

        CreateGameRequest cgr = new CreateGameRequest("g1");
        CreateGameResponse cgrp = null;
        try{
            gs.createGame(cgr, authToken);
            cgrp = gs.createGame(cgr, authToken);
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

        JoinGameRequest jgr = new JoinGameRequest("BLUE", cgrp.gameID()); // Invalid input here

        try {
            gs.joinGame(jgr,authToken);
        }
        catch (UnauthorizedException e){
            System.out.println("Unauthorized list games request");
            Assertions.fail();
        }
        catch(AlreadyTakenException e){
            System.out.println("Already taken join game test 2 fail");
            Assertions.fail();
        }
        catch (BadRequestException e){
            System.out.println("Bad request join game test 2 fail");
            Assertions.assertTrue(true);
        }


    }
}
