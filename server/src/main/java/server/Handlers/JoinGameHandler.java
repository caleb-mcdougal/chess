package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.AlreadyTakenException;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.UnauthorizedException;
import model.Request.JoinGameRequest;
import model.Response.JoinGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        JoinGameRequest serviceRequest = gson.fromJson(request.body(), JoinGameRequest.class);
        String authToken = request.headers("authorization");

        GameService gs = new GameService();
        String gamesList;

        try {
            gs.joinGame(serviceRequest, authToken);
        }
        catch(UnauthorizedException e){
            response.status(401);
            JoinGameResponse errorResponse = new JoinGameResponse("Error: unauthorized");
            return gson.toJson(errorResponse);
        }
        catch (BadRequestException e){
            response.status(400);
            JoinGameResponse errorResponse = new JoinGameResponse("Error: bad request");
            return gson.toJson(errorResponse);
        }
        catch (AlreadyTakenException e){
            response.status(403);
            JoinGameResponse errorResponse = new JoinGameResponse("Error: already taken");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return "{}";
    }
}
