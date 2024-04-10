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
            case MAKE_MOVE -> move(command.getAuthString(), msg);
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
        }

    }

    private void join(Session session, String authToken, String msg) {
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }

        JoinPlayer command = new Gson().fromJson(msg, JoinPlayer.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID", command.getGameID());
        }

        assert gameData != null;
        connections.add(username, session, gameData.gameID());

        try{
            String message = username + " has joined the game as " + command.getPlayerColor();
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.sendServerMessageAll(username,notification, gameData.gameID());

            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.sendMessageToRoot(username, loadGame, gameData.gameID());
        } catch (IOException e) {
            error(username,"Error sending WS message", command.getGameID());
        }
    }

    private void observe(Session session, String authToken, String msg) {
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }

        JoinObserver command = new Gson().fromJson(msg, JoinObserver.class);
        GameData gameData = null;
        try {
            gameData = new SQLGameDAO().getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID", command.getGameID());
        }

        assert gameData != null;
        connections.add(username, session, gameData.gameID());

        try{
            String message = username + " has joined the game as an observer";
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.sendServerMessageAll(username,notification, gameData.gameID());

            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            connections.sendMessageToRoot(username, loadGame, gameData.gameID());
        } catch (IOException e) {
            error(username,"Error sending WS message",command.getGameID());
        }
    }

    private void move(String authToken, String msg) {
        //Not sure what do about username throwing error to error because it needs gameID to throw error
        String username = null;
        try {
            username = getUsername(authToken);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }

        MakeMove command = new Gson().fromJson(msg, MakeMove.class);
        GameData gameData = null;
        SQLGameDAO sgd = new SQLGameDAO();
        try {
            gameData = sgd.getGame(command.getGameID());
        } catch (BadRequestException | DataAccessException e) {
            error(username,"Invalid Game ID", command.getGameID());
            return;
        }
        assert gameData != null;
        try {
            gameData.game().makeMove(command.getMove());
        } catch (InvalidMoveException e) {
            error(username, "Not your turn", command.getGameID());
            return;
        }
        try {
            sgd.updateGameBoard(command.getGameID(), gameData.game());
        } catch (DataAccessException | BadRequestException e) {
            System.out.println("Error accessing DB for make move");
            throw new RuntimeException(e);
        }

        try{
            String message = getMoveMessage(username, command.getMove(), gameData);
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());//This is pulling the old board from the DB need to update DB first

            connections.sendMessageToRoot(username, loadGame, gameData.gameID());
            connections.sendServerMessageAll(username, loadGame, gameData.gameID());
            connections.sendServerMessageAll(username,notification, gameData.gameID());

            String checkMessage = getCheckAndMateMessage(command.getMove(), gameData);
            if(checkMessage != null){
                Notification checkNotification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                connections.sendServerMessageAll(username, checkNotification, gameData.gameID());
                connections.sendMessageToRoot(username, checkNotification, gameData.gameID());
            }
        } catch (IOException e) {
            error(username,"Error sending WS message", command.getGameID());
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
        System.out.println(move.getStartPosition().toString());
        System.out.println(piece.toString());
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

    private String getUsername(String authToken) throws BadRequestException {
        String username = null;
        try {
            SQLAuthDAO sad = new SQLAuthDAO();
            username = sad.getUsername(authToken);

        }
        catch (DataAccessException e) {
            throw new BadRequestException("Invalid AuthToken");
        }
        return username;
    }

    private void error(String username, String errorMessage, Integer gameID){
        ServerMessageError serverMessageError = new ServerMessageError(ServerMessage.ServerMessageType.ERROR, errorMessage);
        try {
            connections.sendMessageToRoot(username, serverMessageError, gameID);
        } catch (IOException e) {
            System.out.println("Error sending WS error notification");
            throw new RuntimeException(e);
        }
    }
}

