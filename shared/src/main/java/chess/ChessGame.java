package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
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
    public HashSet<ChessMove> validMoves(ChessPosition startPosition) {
        //System.out.println("in validMoves");
        if(board.getPiece(startPosition) == null){
            return null;
        }
        ChessPiece piece = board.getPiece(startPosition);
        HashSet<ChessMove> moveSet = piece.pieceMoves(board,startPosition);
        ChessBoard ogBoard = board.deepCopy();
        HashSet<ChessMove> invalidMoves = new HashSet<>();
        for (ChessMove move : moveSet){
            moveIt(move);
            if (isInCheck(piece.getTeamColor())) { // if moving to check, then invalid move
                invalidMoves.add(move);
            }
            board = ogBoard.deepCopy();
        }
        moveSet.removeAll(invalidMoves);
        return moveSet;
    }

    private void moveIt(ChessMove move){
        //System.out.println("in moveIt");
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        //Move the piece
        if(move.getPromotionPiece() == null) { // move piece no promotion
            board.addPiece(end, board.getPiece(start)); //overwrite the piece where you end with the piece moving there
            board.addPiece(start, null); //remove the piece that started there
        } else { // move piece with promotion
            ChessPiece newPiece = new ChessPiece(board.getPiece(start).getTeamColor(), move.getPromotionPiece()); // make promoted piece
            board.addPiece(end, newPiece);//overwrite the piece where you end with the piece moving there
            board.addPiece(start, null); //remove the piece that started there
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException { // This should implement promotions (I think)
        //System.out.println("in makeMove");
        //Move the piece
        ChessPosition start = move.getStartPosition();

        //Check validity
        HashSet<ChessMove> validMoves = validMoves(start);
        //Check to see if a piece is there, the right turn color, and it is one of that pieces valid moves
        if(!validMoves.contains(move) || board.getPiece(start) == null || board.getPiece(start).getTeamColor() != teamTurn){
            throw new InvalidMoveException("Invalid move");
        }

        //Move the piece
        moveIt(move);

        //Change the team turn
        changeTeamTurn();
    }

    private void changeTeamTurn(){
        if(teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        System.out.println("in isInCheck");
        //Find the king position
        ChessPosition kingPos = findKing(teamColor);

        //Check for all possible enemy moves to king positiion
        for (int i = 1; i < 9; i++) { //loop through board to find all enemy pieces
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != teamColor){ //If enemy piece
                    HashSet<ChessMove> moveSet = board.getPiece(pos).pieceMoves(board,pos); // get piece moves
                    for(ChessMove move : moveSet){
                        if(move.getEndPosition().equals(kingPos)){ // if possible move ends on king position you are in check
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //Must be in check to be in checkmate
        if(!isInCheck(teamColor)){
            return false;
        }

        //Find King
        ChessPosition kingPos = findKing(teamColor);

        //Try all king moves
        ChessBoard ogBoard = board.deepCopy();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                System.out.println(currentPosition);
                if(board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() == teamColor) {
                    ChessPiece currentPiece = board.getPiece(currentPosition);
                    System.out.println(currentPiece);
                    HashSet<ChessMove> movesSet = currentPiece.pieceMoves(board, kingPos);
                    for (ChessMove move : movesSet) {
                        try {
                            makeMove(move);
                        } catch (InvalidMoveException e) { // What do i do with this exception?
                            System.out.println("Caught an InvalidMoveException: " + e.getMessage());
                        }
                        if (!isInCheck(teamColor)) { // if not in check at new position not in checkmate
                            return false;
                        } else { // Reset the board
                            board = ogBoard.deepCopy();
                        }
                    }
                }
            }
        }
        return true;
    }

    public ChessPosition findKing(TeamColor teamColor){
        int kingRow = 0;
        int kingCol = 0;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor && board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING){
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
        }
        return new ChessPosition(kingRow,kingCol);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition newPos = new ChessPosition(i,j);
                if(board.getPiece(newPos) != null && board.getPiece(newPos).getTeamColor() == teamColor){
                    HashSet<ChessMove> moveSet = validMoves(newPos);
                    if(!moveSet.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
