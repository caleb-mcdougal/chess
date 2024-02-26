package dataAccess;

import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static HashMap<String,String> AuthDB;

    // Static block to initialize the HashMap for testing
    static {
        AuthDB = new HashMap<>();
    }

    @Override
    public boolean validAuth(String authToken, UserData ud) {
        return Objects.equals(AuthDB.get(authToken), ud.username());
    }

    @Override
    public String getUsername(String authToken) {
        return AuthDB.get(authToken);
    }

    @Override
    public boolean authExists(String authToken) throws UnauthorizedException {
        //Return if the auth token doesn't exist throw an unauthorized error
        if (AuthDB.containsKey(authToken)) {
            return AuthDB.containsKey(authToken);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }


    @Override
    public String createAuth(UserData ud) {
        String authToken = UUID.randomUUID().toString();
        AuthDB.put(authToken, ud.username());
        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {
        //If the given auth token exists remove it from the DB otherwise throw unauthorized error
        if(!AuthDB.containsKey(authToken)){
            throw new UnauthorizedException("AuthToken DNE");
        }
        AuthDB.remove(authToken);
    }


    @Override
    public void clear() {
        //Delete all auth elements in DB
        AuthDB.clear();
    }

    public int getDBSize(){
        if(AuthDB != null) {
            return AuthDB.size();
        }
        else {
            return 0;
        }    }

}
