package server;

import com.google.gson.Gson;
import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.Unauthorized;
import model.CreateGameRequest;
import model.JoinGameRequest;
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
            return gson.toJson("Error: unauthorized");
        }
        catch (BadRequestException e){
            response.status(400);
            return gson.toJson("Error: bad request");
        }
        catch (AlreadyTakenException e){
            response.status(403);
            return gson.toJson("Error: already taken");
        }

        response.status(200);
        return gson.toJson("");
    }
}
