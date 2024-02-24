package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;

public class GameService {

    public GameService() {

    }

    public int createGame(GameData gd, String authToken) throws Unauthorized, BadRequestException {
        if(gd.gameName() == null || gd.gameName().isBlank()){
            throw new BadRequestException("bad request");
        }
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.authExists(authToken);
        MemoryGameDAO mgd = new MemoryGameDAO();
        return mgd.createGame(gd.gameName());
    }
    public void clear() {       //Clearing all DAO hashmaps
        MemoryUserDAO mud = new MemoryUserDAO();
        mud.clear();
        MemoryAuthDAO mad = new MemoryAuthDAO();
        mad.clear();
        MemoryGameDAO mgd = new MemoryGameDAO();
        mgd.clear();
    }
//    public ChessGame[] listGames() {
//
//    }
}
