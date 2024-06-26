package server.WebSocket;
import chess.*;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessageError;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WSServer {

    private final ConnectionManager connections = new ConnectionManager();
    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/connect", WSServer.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }


    //Do i need this?
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("A client has connected");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        var gson = new Gson();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(session, command.getAuthString(), msg);
            case JOIN_OBSERVER -> observe(session, command.getAuthString(), msg);
            case MAKE_MOVE -> move(session, command.getAuthString(), msg);
            case LEAVE -> leave(session, command.getAuthString(), msg);
            case RESIGN -> resign(session, command.getAuthString(), msg);
        }

    }

    private void join(Session session, String authToken, String msg) {
        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);

        //Check for username attached to auth, both may not be necessary
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            sendErrorToRoot(session, "Color already taken");
        }


        //get Game data
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            sendErrorToRoot(session, "Couldn't access gameData Join");
            return;
        }

        //Check that username and color we have here match DB
        if(command.getPlayerColor().equalsIgnoreCase("white")){
            if(!Objects.equals(gameData.whiteUsername(), username)){
                sendErrorToRoot(session, "Color mismatch");
                return;
            }
        }
        else{
            if(!Objects.equals(gameData.blackUsername(), username)){
                sendErrorToRoot(session, "Color mismatch");
                return;
            }
        }

        //See if the game has already ended
        if(gameData.game().gameEnded()){
            sendErrorToRoot(session, "This game is already over");
            return;
        }

        //Add user to group
        connections.add(username, session, gameData.gameID());

        //Notify other players of join
        String message = username + " has joined the game as " + command.getPlayerColor();
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendServerMessageAll(username,notification, gameData.gameID());

        //Load the game for the root
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
        sendLoadToRoot(session, loadGame);
    }

    private void observe(Session session, String authToken, String msg) {
        //Check for valid authToken
        SQLAuthDAO sad = new SQLAuthDAO();
        try {
            sad.authExists(authToken);
        } catch (UnauthorizedException e) {
            sendErrorToRoot(session, "Invalid AuthToken");
        } catch (DataAccessException e) {
            sendErrorToRoot(session, "DB error accessing AuthToken join");
        }

        JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);

        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            sendErrorToRoot(session, "Color already taken");
        }


        //get Game data
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            sendErrorToRoot(session, "Couldn't access gameData Join");
            return;
        }

        //See if the game has already ended
        if(gameData.game().gameEnded()){
            sendErrorToRoot(session, "This game is already over");
            return;
        }

        connections.add(username, session, gameData.gameID());

        String message = username + " has joined the game as an observer";
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendServerMessageAll(username,notification, gameData.gameID());

        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
        sendLoadToRoot(session, loadGame);

    }

    private void move(Session session, String authToken, String msg) {
        //Check for username attached to auth, both may not be necessary
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            sendErrorToRoot(session, "Color already taken");
        }

        MakeMove command = new Gson().fromJson(msg, MakeMove.class);

        //get Game data
        GameData gameData = null;
        SQLGameDAO sgd = null;
        try {
            sgd = new SQLGameDAO();
            gameData = sgd.getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            sendErrorToRoot(session, "Couldn't access gameData Join");
            return;
        }

        //See if the game has already ended
        if(gameData.game().gameEnded()){
            sendErrorToRoot(session, "This game is already over");
            return;
        }

        //Team color shananigans
        if(Objects.equals(gameData.whiteUsername(), username)){ // if user is white
            if(gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK){ // if it is blacks turn send error
                sendErrorToRoot(session, "Invalid move");
                return;
            }
        } else if (Objects.equals(gameData.blackUsername(), username)) { // if user is black
            if(gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE){ // if it is whites turn send error
                sendErrorToRoot(session, "Invalid move");
                return;
            }
        }
        else{ // observer is trying to move a piece
            sendErrorToRoot(session, "Observer cannot move pieces");
            return;
        }

        //Make move should check move validity
        try {
            gameData.game().makeMove(command.getMove());
        } catch (InvalidMoveException e) {
            sendErrorToRoot(session, "Invalid move");
            return;
        }

        try {
            sgd.updateGameBoard(command.getGameID(), gameData.game());
        } catch (DataAccessException | BadRequestException e) {

            sendErrorToRoot(session, "Update board DB error in move");
            return;
        }

        String message = getMoveMessage(username, command.getMove(), gameData);
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendServerMessageAll(username, notification, command.getGameID());


        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());//This is pulling the old board from the DB need to update DB first
        sendLoadToRoot(session, loadGame);
        connections.sendServerMessageAll(username,loadGame, command.getGameID());

        String checkMessage = getCheckAndMateMessage(command.getMove(), gameData);
        if(checkMessage != null){
            Notification checkNotification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
            connections.sendServerMessageAll(username, checkNotification, gameData.gameID());
            sendNotificationToRoot(session, checkNotification);
        }

    }

    private String getCheckAndMateMessage(ChessMove move, GameData gameData){
        //Get opponent color
        if(gameData.game().getBoard().getPiece(move.getEndPosition()).getTeamColor() == ChessGame.TeamColor.WHITE){
            //check for check or mate
            if(gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                //create and return message
                return gameData.blackUsername() + " is in checkmate";
            }
            else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
                return gameData.blackUsername() + " is in check";
            }
            else{
                return null;
            }
        }
        else{
            if(gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                return gameData.whiteUsername() + " is in checkmate";
            }
            else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
                return gameData.whiteUsername() + " is in check";
            }
            else{
                return null;
            }
        }

    }

    private String getMoveMessage(String username, ChessMove move, GameData gameData){
        ChessPiece piece = gameData.game().getBoard().getPiece(move.getEndPosition());
        String pieceString = null;
        switch (piece.getPieceType()){
            case ROOK -> pieceString = "ROOK";
            case KNIGHT -> pieceString = "KNIGHT";
            case BISHOP -> pieceString = "BISHOP";
            case QUEEN -> pieceString = "QUEEN";
            case KING -> pieceString = "KING";
            case PAWN -> pieceString = "PAWN";
        }
        return username + " moved their " + pieceString + " from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
    }

    private void leave(Session session, String authToken, String msg) {
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            sendErrorToRoot(session, "Invalid AuthToken");
            return;
        }

        Leave command = new Gson().fromJson(msg, Leave.class);
        GameData gameData = null;
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            gameData = sgd.getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            sendErrorToRoot(session, "Error accessing DB in leave");
            return;
        }

        //Reset game data username to null
        String color = null;
        if(Objects.equals(gameData.whiteUsername(), username)){
            color = "WHITE";
        }
        else{
            color = "BLACK";
        }
        try {
            sgd.updateGame(gameData.gameID(), color, null);
        } catch (DataAccessException | BadRequestException e) {
            sendErrorToRoot(session, "Error accessing DB in leave");
            return;
        }

        String message = username + " has left the game";
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendServerMessageAll(username,notification, command.getGameID());

        connections.remove(username, command.getGameID());
    }

    private void resign(Session session, String authToken, String msg) {
        //Check username and authToken
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            sendErrorToRoot(session, "Invalid AuthToken");
            return;
        }

        Resign command = new Gson().fromJson(msg, Resign.class);
        //Get game data
        GameData gameData = null;
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            gameData = sgd.getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            sendErrorToRoot(session, "Error accessing DB in resign");
            return;
        }

        //See if the game has already ended
        if(gameData.game().gameEnded()){
            sendErrorToRoot(session, "This game is already over");
            return;
        }
        //Team color shananigans
        if(!Objects.equals(gameData.whiteUsername(), username) && !Objects.equals(gameData.blackUsername(), username)){ // if user is white
            sendErrorToRoot(session, "Observer is trying to resign");
            return;
        }
        gameData.game().endGame();
        try {
            sgd.updateGameBoard(command.getGameID(), gameData.game());
        } catch (DataAccessException | BadRequestException e) {
            sendErrorToRoot(session, "Error accessing DB in resign");
            return;
        }

        String message = getResignMessage(username, gameData);
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendServerMessageAll(username,notification, command.getGameID());
        sendNotificationToRoot(session,notification);
    }

    private String getResignMessage(String username, GameData gameData){
        String opponentUsername = null;
        if(Objects.equals(gameData.whiteUsername(), username)){
            opponentUsername = gameData.blackUsername();
        }
        else{
            opponentUsername = gameData.whiteUsername();
        }
        return username + " has resigned, " + opponentUsername + " won!";
    }

    private String getUsername(String authToken) throws BadRequestException {
        String username = null;
        try {
            SQLAuthDAO sad = new SQLAuthDAO();
            sad.authExists(authToken);
            username = sad.getUsername(authToken);

        }
        catch (DataAccessException e) {
            throw new BadRequestException("Invalid AuthToken");
        } catch (UnauthorizedException e) {
//            sendErrorMessage();
        }
        return username;
    }

    private void sendLoadToRoot(Session session, LoadGame loadGame){
        String msg = new Gson().toJson(loadGame, LoadGame.class);
        try {
            session.getRemote().sendString(msg);
        } catch (IOException e) {
            System.out.println("Error sending WS message to root");
        }
    }

    private void sendNotificationToRoot(Session session, Notification notification){
        String msg = new Gson().toJson(notification, Notification.class);
        try {
            session.getRemote().sendString(msg);
        } catch (IOException e) {
            System.out.println("Error sending WS message to root");
        }
    }

    private void sendErrorToRoot(Session session, String message){
        ServerMessageError serverMessageError =
                new ServerMessageError(ServerMessage.ServerMessageType.ERROR, message);
        String msg = new Gson().toJson(serverMessageError, ServerMessageError.class);
        try {
            session.getRemote().sendString(msg);
        } catch (IOException e) {
            System.out.println("Error sending WS message to root");
        }
    }

}

