package clientUI;

import Exceptions.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import model.GameData;
import model.Request.*;
import model.Response.*;
import ui.ChessBoardPrinter;

import static ui.EscapeSequences.*;

public class Repl {

    private final ServerFacade server;
    private boolean signedIn;
    private boolean inGame;

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
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            LoginResponse response = server.login(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            signedIn = true;
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
        throw new ResponseException(400, "Expected no additional arguments");
    }

    private String listToString(ListGamesResponse response){
        GameData[] games = response.games();
        StringBuilder SB = new StringBuilder();
        for (int i = 0; i < games.length; i++) {
            SB.append(i + 1).append(". ");
            SB.append(games[i].gameName());
//            if (games[i].whiteUsername() != null){
                SB.append(" WHITE: ");
                SB.append(games[i].whiteUsername());
//            }
//            if (games[i].blackUsername() != null){
                SB.append("  BLACK: ");
                SB.append(games[i].blackUsername());
//            }
            SB.append("\n");
        }
        return SB.toString();
    }

    public String join (String... params) throws ResponseException {
        if (params.length == 2) {
            if (!signedIn){
                throw new ResponseException(400, "Login to join a game");
            }
            int gameID = getDBGameID(Integer.parseInt(params[0]));
            JoinGameRequest request = new JoinGameRequest(params[1], gameID);
            JoinGameResponse response = server.join(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            inGame = true;
            ChessBoardPrinter boardPrinter = new ChessBoardPrinter();
            boardPrinter.printBoards();
            return String.format("Joined game: %s", Integer.parseInt(params[0]));
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    private int getDBGameID(int gameRow) throws ResponseException {
        ListGamesResponse response = null;
        try {
            response = server.list();
        } catch (ResponseException e) {
            throw new ResponseException(400, "Enter a valid game row number");
        }
        if (response.message() != null){
            throw new ResponseException(400, response.message());
        }
        GameData[] games = response.games();
        return games[gameRow - 1].gameID();
    }

    public String observe (String... params) throws ResponseException {
        if (params.length == 1) {
            if (!signedIn){
                throw new ResponseException(400, "Login to observe a game");
            }
            int gameID = getDBGameID(Integer.parseInt(params[0]));
            JoinGameRequest request = new JoinGameRequest(null, gameID);
            JoinGameResponse response = server.join(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            inGame = true;
            ChessBoardPrinter boardPrinter = new ChessBoardPrinter();
            boardPrinter.printBoards();
            return String.format("Observing game: %s", Integer.parseInt(params[0]));
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String logout (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Already logged out");
            }
            signedIn = false;
            inGame = false;
            LogoutResponse response = server.logout();
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            return "Logged out";
        }
        throw new ResponseException(400, "Expected no additional arguments");
    }

    public String help(){
        if (signedIn){
            if (inGame){
                return inGameMenu();
            }
            else {
                return postloginMenu();
            }
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
                - join <ID> [WHITE|BLACK]
                - observe <ID>
                - logout
                - quit
                - help
        """;
    }

    public String inGameMenu() {
        return """
                - redraw
                - leave
                - move
                - resign
                - highlight
                - help
        """;
    }

    private void printPrompt() {
        if(signedIn){
            System.out.print("\n" + RESET + "[LOGGED_IN]");
        }
        else{
            System.out.print("\n" + RESET + "[LOGGED_OUT]");
        }
//        System.out.print("\n" + RESET + ">>> " + GREEN);
        System.out.print(">>> " + GREEN);
    }
}
