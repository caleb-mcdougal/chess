package server.Handlers;

import com.google.gson.Gson;
import dataAccess.Exceptions.DataAccessException;
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
            return e;                           //Double check this
        }
        response.status(200);
        return "{}";
    }
}
