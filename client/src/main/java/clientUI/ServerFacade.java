package clientUI;

import Exceptions.ResponseException;
import com.google.gson.Gson;
import model.Request.*;
import model.Response.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
        authToken = null;
    }

    public RegisterResponse register(RegisterRequest request) throws ResponseException{
        var path = "/user";
        RegisterResponse response = this.makeRequest("POST", path, null, request, RegisterResponse.class);
        authToken = response.authToken();
        return response;
    }

    public LoginResponse login(LoginRequest request) throws ResponseException{
        var path = "/session";
        LoginResponse response = this.makeRequest("POST", path, null, request, LoginResponse.class);
        authToken = response.authToken();
        return response;
    }

    public CreateGameResponse create(CreateGameRequest request) throws ResponseException{
        var path = "/game";
        return this.makeRequest("POST", path, authToken, request, CreateGameResponse.class);
    }

    public ListGamesResponse list() throws ResponseException{
        var path = "/game";
        return this.makeRequest("GET", path, authToken, null, ListGamesResponse.class);
    }

    public JoinGameResponse join(JoinGameRequest request) throws ResponseException{
        var path = "/game";
        return this.makeRequest("PUT", path, authToken, request, JoinGameResponse.class);
    }

    public LogoutResponse logout() throws ResponseException{
        var path = "/session";
        return this.makeRequest("DELETE", path, authToken, null, LogoutResponse.class);
    }

    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", header);                //not sure if this is right

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public String getAuthToken(){
        return authToken;
    }

}
