package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserver extends UserGameCommand{

    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
