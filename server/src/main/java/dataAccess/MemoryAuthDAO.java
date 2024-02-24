package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static HashMap<String,String> AuthDB; // auth, username

    // Static block to initialize the HashMap for testing
    static {
        AuthDB = new HashMap<>();
//        AuthDB.put(1, "One");
//        AuthDB.put(2, "Two");
//        AuthDB.put(3, "Three");
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
    public boolean authExists(String authToken) throws Unauthorized{
        if (AuthDB.containsKey(authToken)) {
            return AuthDB.containsKey(authToken);
        }
        throw new Unauthorized("Error: unauthorized");
    }


    @Override
    public String createAuth(UserData ud) {
        String authToken = UUID.randomUUID().toString();
        AuthDB.put(authToken, ud.username());
        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) throws Unauthorized{
        if(!AuthDB.containsKey(authToken)){
            throw new Unauthorized("AuthToken DNE");
        }
        AuthDB.remove(authToken);
    }


    @Override
    public void clear() {
        AuthDB.clear();
    }

    public int getDBSize(){
        return AuthDB.size();
    }

}
