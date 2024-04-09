package server.WebSocket;
import chess.ChessBoard;
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
//                case MAKE_MOVE -> move(conn, msg);
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
        }

    }

    private void join(Session session, String authToken, String msg) {
        String username = null;
        try {
            SQLAuthDAO sad = new SQLAuthDAO();
            username = sad.getUsername(authToken);

        }
        catch (DataAccessException e) {
            error(username,"Invalid AuthToken");
        }

        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
            ChessBoard board = gameData.game().getBoard();
            System.out.println(board.toString());
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
        String username = null;
        try {
            SQLAuthDAO sad = new SQLAuthDAO();
            username = sad.getUsername(authToken);

        }
        catch (DataAccessException e) {
            error(username,"Invalid AuthToken");
        }

        JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
            ChessBoard board = gameData.game().getBoard();
            System.out.println(board.toString());
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

