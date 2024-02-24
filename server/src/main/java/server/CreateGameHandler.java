package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.MemoryAuthDAO;
import dataAccess.Unauthorized;
import model.CreateGameRequest;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        CreateGameRequest cgr = gson.fromJson(request.body(), CreateGameRequest.class); // removed (CreateGameRequest) from before gson
        String authToken = request.headers("authorization");

        GameData gd = new GameData(0, "", "", cgr.gameName(), new ChessGame());
        GameService gs = new GameService();
        int gameID = 0;
        try {
            gameID = gs.createGame(gd, authToken);
        }
        catch(Unauthorized e){
            response.status(401);
            return gson.toJson("Error: unauthorized");
        }
        catch(BadRequestException e){
            response.status(400);
            return gson.toJson("Error: bad request");
        }

        response.status(200);
        return gson.toJson(gameID);
    }
}
