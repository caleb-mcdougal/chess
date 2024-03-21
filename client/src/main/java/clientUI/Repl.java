package clientUI;

import Exceptions.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import model.GameData;
import model.Request.*;
import model.Response.CreateGameResponse;
import model.Response.ListGamesResponse;
import model.Response.LoginResponse;
import model.Response.RegisterResponse;

import static ui.EscapeSequences.*;

public class Repl {

    private final ServerFacade server;
    private boolean signedIn;

    public Repl(String serverURL) {
        server = new ServerFacade(serverURL);
        signedIn = false;
    }


    public void run() {
        if (!signedIn) {
            System.out.print(this.preloginMenu());
        }
        else {
            System.out.print(this.postloginMenu());
        }

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.print(BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

        System.out.println();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list(params);
//                case "quit" -> listPets();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            signedIn = true;
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResponse response = server.login(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            return String.format("You signed in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String register (String... params) throws ResponseException {
        if (params.length == 3) {
            signedIn = true;
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse response = server.register(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String create (String... params) throws ResponseException {
        if (params.length == 1) {
            if (!signedIn){
                throw new ResponseException(400, "Login to create a game");
            }
            CreateGameRequest request = new CreateGameRequest(params[0]);
            CreateGameResponse response = server.create(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            return String.format("You created a game named: %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Login to list games");
            }
            ListGamesResponse response = server.list();
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            return listToString(response);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    private String listToString(ListGamesResponse response){
        GameData[] games = response.games();
        StringBuilder SB = new StringBuilder();
        for (int i = 0; i < games.length; i++) {
            SB.append("Name: ");
            SB.append(games[i].gameName());
            SB.append(", White: ");
            SB.append(games[i].whiteUsername());
            SB.append(", Black: ");
            SB.append(games[i].blackUsername());
            SB.append("\n");
        }
        return SB.toString();
    }


    public String help(){
        if (signedIn){
            return postloginMenu();
        }
        else{
            return preloginMenu();
        }
    }

    public String preloginMenu() {
        return """
                - register <USERNAME> <PASSWORD> <EMAIL>
                - login <USERNAME> <PASSWORD>
                - quit
                - help
        """;
    }

    public String postloginMenu() {
        return """
                - create <NAME>
                - list
                - join <ID>
                - logout
                - quit
                - help
        """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
