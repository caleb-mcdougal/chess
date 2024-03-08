package service;

import dataAccess.Exceptions.AlreadyTakenException;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.*;
import model.Request.CreateGameRequest;
import model.Response.CreateGameResponse;
import model.Request.JoinGameRequest;
import model.Response.ListGamesResponse;

import java.util.Objects;

public class GameService {

    public GameService() {

    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws UnauthorizedException, BadRequestException, DataAccessException {
        //Ensure valid request, check game name
        if(request.gameName() == null || request.gameName().isBlank()){
            throw new BadRequestException("bad request");
        }

        //Ensure valid auth token
        SQLAuthDAO sad = new SQLAuthDAO();
        sad.authExists(authToken);
        SQLGameDAO sgd = new SQLGameDAO();

        //Create and return the game
        System.out.println("cg here 1");
        int gameID = sgd.createGame(request.gameName());
        System.out.println("cg here 2");
        return new CreateGameResponse(gameID, null);
    }


    public void clear() throws DataAccessException{
        //Clear all DAO data structures
        SQLUserDAO sud = new SQLUserDAO();
        sud.clear();
        SQLAuthDAO sad = new SQLAuthDAO();
        sad.clear();
        SQLGameDAO sgd = new SQLGameDAO();
        sgd.clear();
    }
    public ListGamesResponse listGames(String authToken) throws UnauthorizedException, DataAccessException {
        //Ensure valid auth token
        SQLAuthDAO sad = new SQLAuthDAO();
        sad.authExists(authToken);

        //Call DAO method to get an array of game data structures
        SQLGameDAO sgd = new SQLGameDAO();
        return new ListGamesResponse(sgd.listGames(), null);
    }

    public void joinGame(JoinGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        //Check authToken
        SQLAuthDAO sad = new SQLAuthDAO();
        sad.authExists(authToken);

        //check valid color input
        if(!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK") && !Objects.equals(request.playerColor(), null)){
            throw new BadRequestException("bad request");
        }

        //get and check game from id
        SQLGameDAO sgd = new SQLGameDAO();
        GameData gd = sgd.getGame(request.gameID());

        //If joining as player
        if(request.playerColor() != null) {
            //check if color is already taken
            if (Objects.equals(request.playerColor(), "WHITE")) {
                if (gd.whiteUsername() != null) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }
            else{
                if (gd.blackUsername() != null) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }
            sgd.updateGame(request.gameID(), request.playerColor(), sad.getUsername(authToken));
        }
        //If spectator then idempotent
    }
}
