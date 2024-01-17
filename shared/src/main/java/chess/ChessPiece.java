package chess;

import java.util.Collection;
import java.util.HashSet;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        //throw new RuntimeException("Not implemented");
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        //throw new RuntimeException("Not implemented");
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //throw new RuntimeException("Not implemented");
        //insert switch that will link to subclasses of chessmovementrule subclass that will create hashtable and return it here
        ChessPiece.PieceType pieceType = board.getPiece(myPosition).getPieceType(); // This will get the piece type that is at the given position
        HashSet<ChessMove> moveSet = new HashSet<>();
        switch (pieceType) {
            case KING:
                // code to be executed if expression matches value1
                break;
            case QUEEN:
                // code to be executed if expression matches value2
            case BISHOP:
                System.out.println("In Bishop Switch");
                //Bishop myBishop = new Bishop(chessBoardInstance, chessPositionInstance, TeamColor.WHITE);
            case KNIGHT:
            case ROOK:
            case PAWN:
                break;
            // additional cases as needed
            default:
                // code to be executed if none of the cases match the expression
        }

        return moveSet;
    }
}
