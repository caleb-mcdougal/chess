package server;

import com.google.gson.Gson;
import model.ClearResponse;
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
        String result = "200";

        return gson.toJson(result);
    }
}
