package server;

import com.google.gson.Gson;
import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.Unauthorized;
import model.CreateGameRequest;
import model.JoinGameRequest;
import model.JoinGameResponse;
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
        catch(Unauthorized e){
            response.status(401);
            JoinGameResponse errorResponse = new JoinGameResponse("unauthorized");
            return gson.toJson(errorResponse);
        }
        catch (BadRequestException e){
            response.status(400);
            JoinGameResponse errorResponse = new JoinGameResponse("bad request");
            return gson.toJson(errorResponse);
        }
        catch (AlreadyTakenException e){
            response.status(403);
            JoinGameResponse errorResponse = new JoinGameResponse("already taken");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return "{}";
    }
}
