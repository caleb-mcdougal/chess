package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    private int gameID;
//    private ChessGame.TeamColor playerColor;
    private String playerColor;
//    private CommandType commandType;

    public JoinPlayer(String authToken, int gameID, String color) {
        super(authToken);
        this.gameID = gameID;
//        if(color.equalsIgnoreCase("WHITE")){
//            this.playerColor = ChessGame.TeamColor.WHITE;
//        } else if (color.equalsIgnoreCase("BLACK")) {
//            this.playerColor = ChessGame.TeamColor.BLACK;
//        }
//        else{
//            throw new RuntimeException("invalid color in WS join player");
//        }
        this.playerColor = color;
//        this.commandType = CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

}
