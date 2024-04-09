package clientUI;

import Exceptions.ResponseException;
import chess.*;


public class Client { // this should be in websocket communicator

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        //Not sure what to do with this try catch
        try {
            new Repl(serverUrl).run();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }


}
