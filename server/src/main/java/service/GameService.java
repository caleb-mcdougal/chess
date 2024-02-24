package service;

import dataAccess.*;
import model.CreateGameRequest;
import model.CreateGameResponse;
import model.GameData;
import model.ListGamesResponse;

import java.util.Objects;

public class GameService {

    public GameService() {

    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws Unauthorized, BadRequestException {
        if(request.gameName() == null || request.gameName().isBlank()){
            throw new BadRequestException("bad request");
        }
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);
        MemoryGameDAO mgd = new MemoryGameDAO();
        int gameID = mgd.createGame(request.gameName());
        CreateGameResponse response = new CreateGameResponse(gameID, null);
        return response;
    }
    public void clear() {       //Clearing all DAO hashmaps
        MemoryUserDAO mud = new MemoryUserDAO();
        mud.clear();
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.clear();
        MemoryGameDAO mgd = new MemoryGameDAO();
        mgd.clear();
    }
    public ListGamesResponse listGames(String authToken) throws Unauthorized{
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);
        MemoryGameDAO mgd = new MemoryGameDAO();
        return new ListGamesResponse(mgd.listGames(), null);
    }

    public void joinGame(String authToken, String color, int gameID) throws BadRequestException, Unauthorized, AlreadyTakenException {
        //Check authToken
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);

        //check valid color input
        if(!Objects.equals(color, "WHITE") && !Objects.equals(color, "BLACK") && !Objects.equals(color, null)){
            throw new BadRequestException("bad request");
        }

        //get and check game from id
        MemoryGameDAO mgd = new MemoryGameDAO();
        GameData gd = mgd.getGame(gameID);

        //If joining as player
        if(color != null) {
            //check if color is already taken
            if (Objects.equals(color, "WHITE")) {
                if (!gd.whiteUsername().isBlank()) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }
            else{
                if (!gd.blackUsername().isBlank()) {
                    throw new AlreadyTakenException("Color already taken");
                }
            }
            mgd.updateGame(gameID, color, mad.getUsername(authToken));
        }
        //If spectator then idempotent
    }
}
