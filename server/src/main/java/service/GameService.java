package service;

import dataAccess.*;
import model.*;

import java.util.Objects;

public class GameService {

    public GameService() {

    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws UnauthorizedException, BadRequestException {
        if(request.gameName() == null || request.gameName().isBlank()){
            throw new BadRequestException("bad request");
        }
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);
        MemoryGameDAO mgd = new MemoryGameDAO();
        int gameID = mgd.createGame(request.gameName());
        return new CreateGameResponse(gameID, null);
    }
    public void clear() {       //Clearing all DAO hashmaps
        MemoryUserDAO mud = new MemoryUserDAO();
        mud.clear();
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.clear();
        MemoryGameDAO mgd = new MemoryGameDAO();
        mgd.clear();
    }
    public ListGamesResponse listGames(String authToken) throws UnauthorizedException {
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);
        MemoryGameDAO mgd = new MemoryGameDAO();
        return new ListGamesResponse(mgd.listGames(), null);
    }

    public void joinGame(JoinGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, AlreadyTakenException {
        //Check authToken
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);

        //check valid color input
        if(!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK") && !Objects.equals(request.playerColor(), null)){
            throw new BadRequestException("bad request");
        }

        //get and check game from id
        MemoryGameDAO mgd = new MemoryGameDAO();
        GameData gd = mgd.getGame(request.gameID());

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
            mgd.updateGame(request.gameID(), request.playerColor(), mad.getUsername(authToken));
        }
        //If spectator then idempotent
    }
}
