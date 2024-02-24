package server;

import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.AlreadyTakenException;
import model.*;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        RegisterRequest serviceRequest = gson.fromJson(request.body(), RegisterRequest.class); // removed (CreateGameRequest) from before gson
        RegisterResponse serviceResponse;

        UserService us = new UserService();

        try {
            serviceResponse = us.register(serviceRequest);
        }
        catch(AlreadyTakenException e){
            response.status(403);
            return gson.toJson("Error: already taken");
        }
        catch(BadRequestException e){
            response.status(400);
            return gson.toJson("Error: bad request");
        }

        response.status(200);
        return gson.toJson(serviceResponse.authToken());     //spec says this need the username as well
    }
}
