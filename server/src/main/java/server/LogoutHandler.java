package server;

import com.google.gson.Gson;
import dataAccess.Unauthorized;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        UserService us = new UserService();
        String authToken = request.headers("authorization");

        try{
            us.logout(authToken);
        }
        catch (Unauthorized e){
            response.status(401);
            return gson.toJson("Error: unauthorized");
        }

        response.status(200);
        return gson.toJson("");
    }
}