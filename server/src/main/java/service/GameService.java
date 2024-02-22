package service;

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
    public void clear() {       //just clearing user for now
        System.out.println("in UserGame Actions clear");
        MemoryUserDAO.clear();
    }
}
