package dataAccess;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, String> GameDB;

    // Static block to initialize the HashMap for testing
    static {
        GameDB = new HashMap<>();
        GameDB.put(1, "One");
        GameDB.put(2, "Two");
        GameDB.put(3, "Three");
    }


    public static void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        GameDB.clear();
//        System.out.println(UserDB);
    }

    public static int getDBSize(){
//        System.out.println(UserDB);
        return GameDB.size();
    }
}
