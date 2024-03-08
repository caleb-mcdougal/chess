package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.Request.CreateGameRequest;
import model.Response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        CreateGameRequest serviceRequest = gson.fromJson(request.body(), CreateGameRequest.class); // removed (CreateGameRequest) from before gson
        String authToken = request.headers("authorization");

        GameService gs = new GameService();
        CreateGameResponse serviceResponse;
        try {
            serviceResponse = gs.createGame(serviceRequest, authToken);
        }
        catch(UnauthorizedException e){
            response.status(401);
            CreateGameResponse errorResponse = new CreateGameResponse(null, "Error: unauthorized");
            return gson.toJson(errorResponse);
        }
        catch(BadRequestException e){
            response.status(400);
            CreateGameResponse errorResponse = new CreateGameResponse(null, "Error: bad request");
            return gson.toJson(errorResponse);
        }
        catch (DataAccessException e){
            System.out.println("DAE handler");
            response.status(500);
            CreateGameResponse errorResponse = new CreateGameResponse(null, e.getMessage());
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse);
    }
}
