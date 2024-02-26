package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.BadRequestException;
import dataAccess.Unauthorized;
import model.CreateGameRequest;
import model.GameData;
import model.ListGamesResponse;
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
        catch(Unauthorized e){
            response.status(401);
            ListGamesResponse errorResponse = new ListGamesResponse(null, "Error: unauthorized");
            return gson.toJson(errorResponse);
        }

        response.status(200);
        return gson.toJson(serviceResponse);
    }
}
