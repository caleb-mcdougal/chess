package webSocketMessages.serverMessages;

import chess.ChessGame;

public class Error extends ServerMessage{

    private String errorMessage;
    public Error(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

}
