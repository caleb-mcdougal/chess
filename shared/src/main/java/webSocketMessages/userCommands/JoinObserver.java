package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserver extends UserGameCommand{

    private int gameID;
    private CommandType commandType;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }
}
