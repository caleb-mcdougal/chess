package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private static HashMap<String, String> UserPassword;
    private static HashMap<String, String> UserEmail;

    // Static block to initialize the HashMap for testing
    static {
        UserPassword = new HashMap<>();
        UserEmail = new HashMap<>();
//        UserDB.put(1, "One");
//        UserDB.put(2, "Two");
//        UserDB.put(3, "Three");
    }



    public void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        UserPassword.clear();
        UserEmail.clear();
//        System.out.println(UserDB);
    }

    public int getDBSize(){
//        System.out.println(UserDB);
        return UserPassword.size();
    }
    public int getEmailDBSize(){
//        System.out.println(UserDB);
        return UserEmail.size();
    }

    @Override
    public void createUser(UserData ud) {
        UserPassword.put(ud.username(), ud.password());
        UserEmail.put(ud.username(), ud.email());
    }

    @Override
    public UserData getUser(String username){
        return new UserData(username, UserPassword.get(username), UserEmail.get(username));
    }

    @Override
    public Boolean userExists(String username) {
        return UserPassword.get(username) != null;
    }

}
