package webSocketMessages.serverMessages;

import chess.ChessGame;

public class Notification extends ServerMessage{

    private String message;
    public Notification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }


    public String getMessage() {
        return this.message;
    }

}
