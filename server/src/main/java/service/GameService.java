package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;

public class GameService {

    public GameService() {

    }
//    public AuthData register(UserData user) {
//
//    }
//    public AuthData login(UserData user) {
//
//    }
    public int createGame(GameData gd) {
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
}
