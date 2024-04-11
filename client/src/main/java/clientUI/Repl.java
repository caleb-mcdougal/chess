package clientUI;

import Exceptions.ResponseException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import model.Request.*;
import model.Response.*;
import ui.ChessBoardPrinter;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;


import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver{

    private final ServerFacade server;
    private final WebSocketCommunicator WSCommunicator;
    private boolean signedIn;
    private boolean inGame;
    private ChessGame currentGame;
    private int gameID;

    private String teamColor;

    public Repl(String serverURL) throws ResponseException {
        server = new ServerFacade(serverURL);
        WSCommunicator = new WebSocketCommunicator(serverURL, this);
        signedIn = false;
        teamColor = null;
    }

    //Websocket Below

    @Override
    public void notify(String message) {
//        System.out.println("in notify");
//        System.out.println(message);
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//        System.out.println("here");
        switch (serverMessage.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(message);
            case ERROR -> displayError(message);
            case LOAD_GAME -> loadGame( message);
        }
    }

    private void displayNotification(String serverMessage){
        Notification notification = new Gson().fromJson(serverMessage, Notification.class);
        System.out.println(BLUE + notification.getMessage());
        printPrompt();
    }

    private void displayError(String serverMessage){
        ServerMessageError serverMessageError = new Gson().fromJson(serverMessage, ServerMessageError.class);
        System.out.println(serverMessageError.getErrorMessage());
        printPrompt();
    }

    private void loadGame(String  serverMessage){
        LoadGame loadGame = new Gson().fromJson(serverMessage, LoadGame.class);
        ChessBoardPrinter boardPrinter = new ChessBoardPrinter(loadGame.getGame().getBoard());
        this.currentGame = loadGame.getGame();
        boardPrinter.printBoards(teamColor);
        printPrompt();
    }

    //HTTP Below

    public void run() {
        if (!signedIn) {
            System.out.print(this.preloginMenu());
        }
        else {
            System.out.print(this.postLoginMenu());
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
                case "move" -> move(params);
                case "leave" -> leave(params);
                case "resign" -> resign(params);
                case "redraw" -> redraw(params);
                case "highlight" -> highlight(params);
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
            if (signedIn){
                throw new ResponseException(400, "Logout to register a new user");
            }
            if (inGame){
                throw new ResponseException(400, "Logout to register a new user");
            }
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResponse response = server.register(request);
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            signedIn = true;
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String create (String... params) throws ResponseException {
        if (params.length == 1) {
            if (!signedIn){
                throw new ResponseException(400, "Login to create a game");
            }
            if (inGame){
                throw new ResponseException(400, "Leave the current game to create a new game");
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
            if (inGame){
                throw new ResponseException(400, "Leave the current game to see a list of games");
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
            if (inGame){
                throw new ResponseException(400, "Leave the current game to join a new game");
            }

            int gameID = getDBGameID(Integer.parseInt(params[0]));
            JoinGameRequest request = new JoinGameRequest(params[1], gameID);
            JoinGameResponse response = server.join(request);
            JoinPlayer joinPlayer = new JoinPlayer(server.getAuthToken(), gameID, params[1]);
            WSCommunicator.sendUserCommand(joinPlayer);

            teamColor = params[1];
            this.gameID = gameID;
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            inGame = true;
            return "";//String.format("Joined game: %s", Integer.parseInt(params[0]));
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
            if (inGame){
                throw new ResponseException(400, "Leave the current game to observe a new game");
            }
//            if (!currentGame.gamePlayable()){
//                throw new ResponseException(400, "This game is already over");
//            }
            int gameID = getDBGameID(Integer.parseInt(params[0]));
            JoinGameRequest request = new JoinGameRequest(null, gameID);
            JoinGameResponse response = server.join(request);
            JoinObserver joinObserver = new JoinObserver(server.getAuthToken(), gameID);
            WSCommunicator.sendUserCommand(joinObserver);
            teamColor = null;
            if (response.message() != null){
                throw new ResponseException(400, response.message());
            }
            inGame = true;
            return "";//String.format("Observing game: %s", Integer.parseInt(params[0]));
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String logout (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Already logged out");
            }
            if (inGame){
                throw new ResponseException(400, "Leave the current game to logout");
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

    public String move (String... params) throws ResponseException {
        if (params.length == 2) {
            if (!signedIn){
                throw new ResponseException(400, "Already logged out");
            }
            if (!inGame){
                throw new ResponseException(400, "Join a game to make a move");
            }
//            if (!currentGame.gamePlayable()){
//                throw new ResponseException(400, "This game is already over");
//            }
            ChessMove move = inputToMove(params[0], params[1]);
            MakeMove makeMove = new MakeMove(server.getAuthToken(), gameID, move);
            WSCommunicator.sendUserCommand(makeMove);
            return "";
        }
        throw new ResponseException(400, "Expected <a-h><1-8> for both start and end positions");
    }


    private ChessMove inputToMove(String start, String end) throws ResponseException {
//        System.out.println("before loop");
        if(start.length() != 2 && end.length() != 2){
            throw new ResponseException(400, "Expected ROW: <1-8> COLUMN: <a-h> of start and end positions");
        }
        int startRow = 9;
        int startCol = 9;
        int endRow = 9;
        int endCol = 9;
        String[] letterString = {"a", "b", "c", "d", "e", "f", "g", "h"};
//        System.out.println("before loop");
        for (int i = 0; i < letterString.length; i++) {
//            System.out.println("in loop ");
//            System.out.println(i);
            if(start.substring(0, 1).toLowerCase().equals(letterString[i])){
                startCol = i+1;
            }
            if(end.substring(0,1).toLowerCase().equals(letterString[i])){
                endCol = i+1;
            }
            if(Integer.parseInt(start.substring(1)) == i+1){
                startRow = i+1;
            }
            if(Integer.parseInt(end.substring(1)) == i+1){
                endRow = i+1;
            }
        }
        if(startRow == 9 || startCol == 9 || endRow == 9 || endCol == 9){
            throw new ResponseException(400, "Invalid positions");
        }

        ChessPosition startPosition = new ChessPosition(startRow,startCol);
        ChessPosition endPosition = new ChessPosition(endRow,endCol);
        HashSet<ChessMove> validMoves = currentGame.validMoves(startPosition);
        for (ChessMove move: validMoves) {
            if(move.getStartPosition().equals(startPosition) && move.getEndPosition().equals(endPosition)){
                return move;
            }
        }
        throw new ResponseException(400, "Not a valid Move");
    }

    public String leave (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Login to join a game");
            }
            if (!inGame){
                throw new ResponseException(400, "You are not in a game");
            }
            Leave leave = new Leave(server.getAuthToken(), gameID);
            WSCommunicator.sendUserCommand(leave);
            inGame = false;
            return "You left the game";
        }
        throw new ResponseException(400, "Expected no additional arguments");
    }


    public String resign (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Login to join a game");
            }
            if (!inGame){
                throw new ResponseException(400, "You are not in a game");
            }

            Resign resign = new Resign(server.getAuthToken(), gameID);
            WSCommunicator.sendUserCommand(resign);
            return "You lost";
        }
        throw new ResponseException(400, "Expected no additional arguments");
    }

    public String redraw (String... params) throws ResponseException {
        if (params.length == 0) {
            if (!signedIn){
                throw new ResponseException(400, "Login to join a game");
            }
            if (!inGame){
                throw new ResponseException(400, "You are not in a game");
            }

            ChessBoardPrinter boardPrinter = new ChessBoardPrinter(currentGame.getBoard());
            boardPrinter.printBoards(teamColor);
            return "";
        }
        throw new ResponseException(400, "Expected no additional arguments");
    }

    public String highlight (String... params) throws ResponseException {
        if (params.length == 1) {
            if (!signedIn){
                throw new ResponseException(400, "Login to join a game");
            }
            if (!inGame){
                throw new ResponseException(400, "You are not in a game");
            }

            ChessBoardPrinter boardPrinter = new ChessBoardPrinter(currentGame.getBoard());
            boardPrinter.printHighlight(teamColor, params[0], currentGame);
            return "";
        }
        throw new ResponseException(400, "Expected no additional arguments");
    }


    public String help(){
        if (signedIn){
            if (inGame){
                return inGameMenu();
            }
            else {
                return postLoginMenu();
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

    public String postLoginMenu() {
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
                - move <a-h><1-8> <a-h><1-8>
                - resign
                - highlight <a-h><1-8>
                - help
        """;
    }

    private void printPrompt() {
        if(signedIn){
            if(inGame){
                System.out.print("\n" + RESET + "[IN_GAME]");
            }
            else {
                System.out.print("\n" + RESET + "[LOGGED_IN]");
            }
        }
        else{
            System.out.print("\n" + RESET + "[LOGGED_OUT]");
        }
//        System.out.print("\n" + RESET + ">>> " + GREEN);
        System.out.print(">>> " + GREEN);
    }
}
