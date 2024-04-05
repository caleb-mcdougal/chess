package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessPiece;

public class MakeMove extends UserGameCommand{

    private int gameID;
    private ChessMove move;
    private CommandType commandType;

    public MakeMove(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
