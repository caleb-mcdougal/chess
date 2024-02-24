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
        LoginRequest lr = gson.fromJson(request.body(), LoginRequest.class);

        UserService us = new UserService();
        UserData ud = new UserData(lr.username(), lr.password(), "");
        AuthData ad;

        try{
            ad = us.login(ud);
        }
        catch (Unauthorized e){
            System.out.println("Inside");
            response.status(401);
            return gson.toJson("Error: unauthorized");
        }

        System.out.println("Here");


        response.status(200);
        return gson.toJson(ad.authToken());     //spec says this need the username as well
    }
}
