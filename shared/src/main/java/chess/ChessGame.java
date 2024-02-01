package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessGame.TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        //throw new RuntimeException("Not implemented");
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        //throw new RuntimeException("Not implemented");
        if(teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //throw new RuntimeException("Not implemented");

        ChessPiece piece = board.getPiece(startPosition);
        return piece.pieceMoves(board,startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        board.addPiece(end,board.getPiece(start)); //overwrite the piece where you end with the piece moving there
        board.addPiece(start,null); //remove the piece that started there
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        boolean inCheck = false;

        //Find the king position
        int kingRow = 0;
        int kingCol = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING){
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
        }
        ChessPosition kingPos = new ChessPosition(kingRow,kingCol);

        //Check for possible enemy moves to king positiion
        for (int i = 0; i < 8; i++) { //loop through board to find all enemy pieces
            for (int j = 0; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != teamColor){ //If enemy piece
                    HashSet<ChessMove> moveSet = board.getPiece(pos).pieceMoves(board,pos); // get piece moves
                    for(ChessMove move : moveSet){
                        if(move.getEndPosition() == kingPos){ // if possible move ends on king position you are in check
                            inCheck = true;
                            break;
                        }
                    }
                }
            }
        }
        return inCheck;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        //throw new RuntimeException("Not implemented");
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        //throw new RuntimeException("Not implemented");
        return board;
    }
}
