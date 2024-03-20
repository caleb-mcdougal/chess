import Exceptions.ResponseException;
import com.google.gson.Gson;
import model.Request.CreateGameRequest;
import model.Request.RegisterRequest;
import model.Response.ClearResponse;
import model.Response.CreateGameResponse;
import model.Response.RegisterResponse;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
        authToken = null;
    }

//    public Pet addPet(Pet pet) throws Exceptions.ResponseException {
//        var path = "/pet";
//        return this.makeRequest("POST", path, pet, Pet.class);
//    }
//
//    public void deletePet(int id) throws Exceptions.ResponseException {
//        var path = String.format("/pet/%s", id);
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public void deleteAllPets() throws Exceptions.ResponseException {
//        var path = "/pet";
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public Pet[] listPets() throws Exceptions.ResponseException {
//        var path = "/pet";
//        record listPetResponse(Pet[] pet) {
//        }
//        var response = this.makeRequest("GET", path, null, listPetResponse.class);
//        return response.pet();
//    }

//    public CreateGameResponse addGame(CreateGameRequest request) throws ResponseException {
//        var path = "/game";
//        return this.makeRequest("POST", path, request, CreateGameResponse.class);
//    }
//
//    public ClearResponse clear() throws ResponseException {
//        var path = "/db";
//        return this.makeRequest("DELETE", path, null, );
//    }

    public RegisterResponse register(RegisterRequest request) throws ResponseException{
        var path = "/user";
        RegisterResponse response = this.makeRequest("POST", path, null, request, RegisterResponse.class);
        authToken = response.authToken();
        return response;
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

}
