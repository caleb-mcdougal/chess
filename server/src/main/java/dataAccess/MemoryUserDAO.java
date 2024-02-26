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
    }



    public void clear() {
        //Clear all user information in DB
        UserPassword.clear();
        UserEmail.clear();
    }

    public int getDBSize(){
        if(UserPassword != null) {
            return UserPassword.size();
        }
        else {
            return 0;
        }
    }
    public int getEmailDBSize(){
        if(UserEmail != null) {
            return UserEmail.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public void createUser(UserData ud) {
        //Create new user with username linked to password and email in respective DB's
        UserPassword.put(ud.username(), ud.password());
        UserEmail.put(ud.username(), ud.email());
    }

    @Override
    public UserData getUser(String username){
        //Return the user information in userdata object
        return new UserData(username, UserPassword.get(username), UserEmail.get(username));
    }

    @Override
    public Boolean userExists(String username) {
        return UserPassword.get(username) != null;
    }

}
