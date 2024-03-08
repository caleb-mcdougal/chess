package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
import model.Response.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        GameService gs = new GameService();
        try {
            gs.clear();
        }
        catch (DataAccessException e){
            response.status(500);
            CreateGameResponse errorResponse = new CreateGameResponse(null, e.getMessage());
            return gson.toJson(errorResponse);
        }
        response.status(200);
        return "{}";
    }
}
