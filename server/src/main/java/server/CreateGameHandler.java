package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.MemoryAuthDAO;
import dataAccess.Unauthorized;
import model.CreateGameRequest;
import model.CreateGameResponse;
import model.GameData;
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
        catch(Unauthorized e){
            response.status(401);
            CreateGameResponse errorResponse = new CreateGameResponse(0, "Error: unauthorized");
            return gson.toJson(errorResponse);
        }
        catch(BadRequestException e){
            response.status(400);
            CreateGameResponse errorResponse = new CreateGameResponse(0, "Error: bad request");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse.gameID());
    }
}
