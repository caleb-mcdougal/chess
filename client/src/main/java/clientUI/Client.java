package clientUI;

import chess.*;
//import webSocketMessages.serverMessages.ServerMessage;

public class Client {//implements ServerMessageObserver{
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }

//    @Override
//    public void notify(ServerMessage message) {
//        switch (message.getServerMessageType()) {
//            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
//            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
//            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
//        }
//    }

}
