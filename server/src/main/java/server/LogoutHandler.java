package server;

import com.google.gson.Gson;
import dataAccess.UnauthorizedException;
import model.LogoutResponse;
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
        catch (UnauthorizedException e){
            response.status(401);
            LogoutResponse errorResponse = new LogoutResponse("Error: unauthorized");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return "{}";
    }
}
