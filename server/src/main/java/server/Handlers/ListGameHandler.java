package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.Response.CreateGameResponse;
import model.Response.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        String authToken = request.headers("authorization");

        GameService gs = new GameService();
        ListGamesResponse serviceResponse;

        try {
            serviceResponse = gs.listGames(authToken);
        }
        catch(UnauthorizedException e){
            response.status(401);
            ListGamesResponse errorResponse = new ListGamesResponse(null, "Error: unauthorized");
            return gson.toJson(errorResponse);
        }
        catch (DataAccessException e){
            response.status(500);
            CreateGameResponse errorResponse = new CreateGameResponse(null, e.getMessage());
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse);
    }
}
