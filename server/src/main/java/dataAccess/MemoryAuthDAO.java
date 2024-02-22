package dataAccess;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private static HashMap<Integer, String> AuthDB;

    // Static block to initialize the HashMap for testing
    static {
        AuthDB = new HashMap<>();
        AuthDB.put(1, "One");
        AuthDB.put(2, "Two");
        AuthDB.put(3, "Three");
    }


    public static void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        AuthDB.clear();
//        System.out.println(UserDB);
    }

    public static int getDBSize(){
//        System.out.println(UserDB);
        return AuthDB.size();
    }
}
