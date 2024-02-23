package dataAccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static HashSet<String> AuthDB; // username, auth

    // Static block to initialize the HashMap for testing
    static {
        AuthDB = new HashSet<>();
//        AuthDB.put(1, "One");
//        AuthDB.put(2, "Two");
//        AuthDB.put(3, "Three");
    }

    @Override
    public boolean validAuth(String authToken) {
        if (AuthDB.contains(authToken)) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String createAuth() {
        String authToken = UUID.randomUUID().toString();
        AuthDB.add(authToken);
        return authToken;
    }

    @Override
    public void deleteAuth() {

    }


    @Override
    public void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        AuthDB.clear();
//        System.out.println(UserDB);
    }

    public int getDBSize(){
//        System.out.println(UserDB);
        return AuthDB.size();
    }

}
