package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    private int gameID;
//    private ChessGame.TeamColor playerColor;
    private String playerColor;

    public JoinPlayer(String authToken, int gameID, String color) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = color;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

}
