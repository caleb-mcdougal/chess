package chess;

public class ChessMovementRule {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor color;
    public ChessMovementRule(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        this.board = board;
        this.myPosition = myPosition;
        this.color = color;
    }

    //public ChessGame.TeamColor getTeamColor() {
    //    //throw new RuntimeException("Not implemented");
    //    return this.pieceColor;
    //}
}
