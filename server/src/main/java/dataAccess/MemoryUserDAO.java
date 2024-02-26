package dataAccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{

    private static HashMap<String, String> userPassword;
    private static HashMap<String, String> userEmail;

    // Static block to initialize the HashMap for testing
    static {
        userPassword = new HashMap<>();
        userEmail = new HashMap<>();
    }



    public void clear() {
        //Clear all user information in DB
        userPassword.clear();
        userEmail.clear();
    }

    public int getDBSize(){
        if(userPassword != null) {
            return userPassword.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public void createUser(UserData ud) {
        //Create new user with username linked to password and email in respective DB's
        userPassword.put(ud.username(), ud.password());
        userEmail.put(ud.username(), ud.email());
    }

    @Override
    public UserData getUser(String username){
        //Return the user information in userdata object
        return new UserData(username, userPassword.get(username), userEmail.get(username));
    }

    @Override
    public Boolean userExists(String username) {
        return userPassword.get(username) != null;
    }

}
