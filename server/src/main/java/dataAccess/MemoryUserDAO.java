package dataAccess;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private static HashMap<Integer, String> UserDB;

    // Static block to initialize the HashMap for testing
    static {
        UserDB = new HashMap<>();
        UserDB.put(1, "One");
        UserDB.put(2, "Two");
        UserDB.put(3, "Three");
    }


    public static void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        UserDB.clear();
//        System.out.println(UserDB);
    }

    public static int getDBSize(){
//        System.out.println(UserDB);
        return UserDB.size();
    }

}
