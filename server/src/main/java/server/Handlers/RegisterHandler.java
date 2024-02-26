package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.AlreadyTakenException;
import model.RequestResponse.RegisterRequest;
import model.RequestResponse.RegisterResponse;
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
            RegisterResponse errorResponse = new RegisterResponse(null, null, "Error: already taken");
            return gson.toJson(errorResponse);
        }
        catch(BadRequestException e){
            response.status(400);
            RegisterResponse errorResponse = new RegisterResponse(null, null, "Error: bad request");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse);     //spec says this need the username as well
    }
}
