package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.Request.LoginRequest;
import model.Response.CreateGameResponse;
import model.Response.LoginResponse;
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
        catch (UnauthorizedException e){
            response.status(401);
            LoginResponse errorResponse = new LoginResponse(null, null, "Error: unauthorized");
            return gson.toJson(errorResponse);
        }
        catch (DataAccessException e){
            response.status(500);
            CreateGameResponse errorResponse = new CreateGameResponse(null, e.getMessage());
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse);     //spec says this need the username as well
    }
}
