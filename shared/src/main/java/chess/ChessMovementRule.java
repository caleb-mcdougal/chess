package chess;

import java.util.Collection;
import java.util.HashSet;
public class ChessMovementRule {
    private ChessBoard board;
    private ChessPosition myPosition;
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    public ChessMovementRule(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color, ChessPiece.PieceType type) {
        this.board = board;
        this.myPosition = myPosition;
        this.color = color;
        this.type = type;
    }

    public HashSet<ChessMove> getMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        switch (type) {
            case KING:
                break;
            case QUEEN:
                break;
            case BISHOP:
                moveSet = bishopMoves();
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }
        return moveSet;
    }



    private HashSet<ChessMove> bishopMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal
        int [] rowSteps = {1,1,-1,-1};//forward-right, forward-left, backward-right, backward-left
        int [] colSteps = {1,-1,1,-1};

        for (int i = 0; i < rowSteps.length; i++) {
            //newPosition = myPosition;
            newPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn());
            while (true) {
                newPosition = new ChessPosition(newPosition.getRow() + rowSteps[i], newPosition.getColumn() + colSteps[i]);//update position

                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (newPosition.getRow() > 8 || newPosition.getColumn() > 8 || newPosition.getRow() < 1 || newPosition.getColumn() < 1) {//Off the board
                    break;
                } else if (board.getPiece(newPosition) != null) {//if a piece is on newPosition
                    if (board.getPiece(newPosition).getTeamColor() != color) { // if the piece is enemy color
                        System.out.println(newPosition.toString());
                        moveSet.add(newMove); //capture and add possible space
                        break;
                    } else { //if piece is friendly color then break
                        break;
                    }
                } else { // if normal legal space add it to hashset
                    System.out.println(newPosition.toString());
                    moveSet.add(newMove);
                }
            }
        }
        System.out.println(moveSet);
        return moveSet;
    }


}

//Considered using subclasses but determined to just use member functions with a helper function.
/*
class Bishop extends ChessMovementRule {
    public Bishop(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color); // Call the constructor of the superclass
    }

    public Collection<ChessMove> bishopMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);//create new position one up and diagonal
        int [] rowSteps = {1,1,-1,-1};//forward-right, forward-left, backward-right, backward-left
        int [] colSteps = {1,-1,1,-1};

        for (int i = 0; i < rowSteps.length; i++) {
            while (true) {
                newPosition = new ChessPosition(newPosition.getRow() + rowSteps[i], newPosition.getColumn() + colSteps[i]);//update position
                System.out.println("newPosition: " + newPosition);
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);

                if (newPosition.getRow() > 7 || newPosition.getColumn() > 7 || newPosition.getRow() < 0 || newPosition.getColumn() < 0) {//Off the board
                    break;
                } else if (board.getPiece(newPosition) != null) {//if a piece is on newPosition
                    if (board.getPiece(newPosition).getTeamColor() != color) { // if the piece is enemy color
                        moveSet.add(newMove); //capture and add possible space
                        System.out.println("moveSet: " + moveSet);
                        break;
                    } else { //if piece is friendly color then break
                        break;
                    }
                } else { // if normal legal space add it to hashset
                    moveSet.add(newMove);
                }
            }
        }
        return moveSet;
    }
}
*/
