package server;

import com.google.gson.Gson;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var gson = new Gson();

        GameService gs = new GameService();
        gs.clear();
        response.status(200);
        return "{}";
    }
}
