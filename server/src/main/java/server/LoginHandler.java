package server;

import com.google.gson.Gson;
import dataAccess.Unauthorized;
import model.*;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();
        LoginRequest serviceRequest = gson.fromJson(request.body(), LoginRequest.class);

        UserService us = new UserService();
        LoginResponse serviceResponse;

        try{
            serviceResponse = us.login(serviceRequest);
        }
        catch (Unauthorized e){
            response.status(401);
            return gson.toJson("Error: unauthorized");
        }



        response.status(200);
        return gson.toJson(serviceResponse.authToken());     //spec says this need the username as well
    }
}
