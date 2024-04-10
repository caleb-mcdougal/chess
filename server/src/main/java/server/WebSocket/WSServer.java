package server.WebSocket;
import chess.*;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
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
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

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
        //Create a manager class to keep track of session
//        this.connections.add("test", session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        var gson = new Gson();
        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        //Part of the manager class (getter)

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(session, command.getAuthString(), msg);
            case JOIN_OBSERVER -> observe(session, command.getAuthString(), msg);
            case MAKE_MOVE -> move(session, command.getAuthString(), msg);
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
        }

    }

    private void join(Session session, String authToken, String msg) {
        String username = getUsername(authToken);

        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID");
        }

        connections.add(username, session);

        try{
            String message = username + " has joined the game as " + command.getPlayerColor();
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.sendServerMessageAll(username,notification);

            assert gameData != null;
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.sendMessageToRoot(username, loadGame);
        } catch (IOException e) {
            error(username,"Error sending WS message");
        }
    }

    private void observe(Session session, String authToken, String msg) {
        String username = getUsername(authToken);

        JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID");
        }

        connections.add(username, session);

        try{
            String message = username + " has joined the game as an observer";
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.sendServerMessageAll(username,notification);

            assert gameData != null;
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.sendMessageToRoot(username, loadGame);
        } catch (IOException e) {
            error(username,"Error sending WS message");
        }
    }

    private void move(Session session, String authToken, String msg) {
        String username = getUsername(authToken);
        System.out.println("here");
        MakeMove command = new Gson().fromJson(msg, MakeMove.class);
        GameData gameData = null;
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            gameData = sgd.getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID");
            return;
        }
        System.out.println("here");
        assert gameData != null;
        try {
            gameData.game().makeMove(command.getMove());
        } catch (InvalidMoveException e) {
            error(username, "Not your turn");
            return;
        }
        System.out.println("here");
        try {
            sgd.updateGameBoard(command.getGameID(), gameData.game());
        } catch (DataAccessException | BadRequestException e) {
            System.out.println("Error accessing DB for make move");
            throw new RuntimeException(e);
        }
        System.out.println("here");

        try{
            String message = getMoveMessage(username, command.getMove(), gameData);
            System.out.println("here");
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());//This is pulling the old board from the DB need to update DB first

            connections.sendMessageToRoot(username, loadGame);
            connections.sendServerMessageAll(username, loadGame);
            connections.sendServerMessageAll(username,notification);
        } catch (IOException e) {
            error(username,"Error sending WS message");
        }
    }

    private String getMoveMessage(String username, ChessMove move, GameData gameData){
        System.out.println("in getMoveMessage");
        ChessPiece piece = gameData.game().getBoard().getPiece(move.getEndPosition());
        System.out.println("start positions");
        System.out.println(move.getStartPosition().toString());
        System.out.println("piece:");
        System.out.println(piece.toString());
        String pieceString = null;
        System.out.println("in getMoveMessage");
        switch (piece.getPieceType()){
            case ROOK -> pieceString = "ROOK";
            case KNIGHT -> pieceString = "KNIGHT";
            case BISHOP -> pieceString = "BISHOP";
            case QUEEN -> pieceString = "QUEEN";
            case KING -> pieceString = "KING";
            case PAWN -> pieceString = "PAWN";
        }
        System.out.println("in getMoveMessage");
        return username + " moved their " + pieceString + " from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString();
    }

    private String getUsername(String authToken) {
        String username = null;
        try {
            SQLAuthDAO sad = new SQLAuthDAO();
            username = sad.getUsername(authToken);

        }
        catch (DataAccessException e) {
            error(username,"Invalid AuthToken");
        }
        return username;
    }

    private void error(String username, String errorMessage){
        ServerMessageError serverMessageError = new ServerMessageError(ServerMessage.ServerMessageType.ERROR, errorMessage);
        try {
            connections.sendMessageToRoot(username, serverMessageError);
        } catch (IOException e) {
            System.out.println("Error sending WS error notification");
            throw new RuntimeException(e);
        }
    }
}

