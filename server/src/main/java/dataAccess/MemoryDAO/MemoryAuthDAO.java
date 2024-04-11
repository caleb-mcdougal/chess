package dataAccess.MemoryDAO;

import dataAccess.AuthDAO;
import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String,String> authDB;

    // Static block to initialize the HashMap for testing
    static {
        authDB = new HashMap<>();
    }


    @Override
    public String getUsername(String authToken) {
        return authDB.get(authToken);
    }

    @Override
    public boolean authExists(String authToken) throws UnauthorizedException {
        //Return if the auth token doesn't exist throw an unauthorized error
        if (authDB.containsKey(authToken)) {
            return authDB.containsKey(authToken);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }


    @Override
    public String createAuth(UserData ud) {
        String authToken = UUID.randomUUID().toString();
        authDB.put(authToken, ud.username());
        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {
        //If the given auth token exists remove it from the DB otherwise throw unauthorized error
        if(!authDB.containsKey(authToken)){
            throw new UnauthorizedException("AuthToken DNE");
        }
        authDB.remove(authToken);
    }


    @Override
    public void clear() {
        //Delete all auth elements in DB
        authDB.clear();
    }


}
