package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class GameService {

    public GameService() {

    }
//    public AuthData register(UserData user) {
//
//    }
//    public AuthData login(UserData user) {
//
//    }
    public void clear() {       //Clearing all DAO hashmaps
//        System.out.println("in UserGame Actions clear");
        MemoryUserDAO.clear();
        MemoryAuthDAO.clear();
        MemoryGameDAO.clear();
    }
}
