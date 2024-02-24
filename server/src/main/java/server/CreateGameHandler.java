package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
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

        System.out.println(cgr.gameName());

        MemoryAuthDAO mad = new MemoryAuthDAO();
//        if(!mad.validAuth(authToken)){
//            //This should return an error
//            //TODO
//        }
//        else {
        GameData gd = new GameData(0, "", "", cgr.gameName(), new ChessGame());
        GameService gs = new GameService();
        int gameID = gs.createGame(gd, authToken);

        response.status(200);
//        }

        return gson.toJson(gameID);
    }
}
