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
                moveSet = kingMoves();
                break;
            case QUEEN:
                moveSet = queenMoves();
                break;
            case BISHOP:
                moveSet = bishopMoves();
                break;
            case KNIGHT:
                moveSet = knightMoves();
                break;
            case ROOK:
                moveSet = rookMoves();
                break;
            case PAWN:
                if (color == ChessGame.TeamColor.WHITE){
                    moveSet = whitePawnMoves();
                } else {
                    moveSet = blackPawnMoves();
                }
                break;
        }
        return moveSet;
    }


    private HashSet<ChessMove> kingMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal
        int [] rowSteps = {1,1,0,-1,-1,-1,0,1};//starting at forward checking loop around king
        int [] colSteps = {0,1,1,1,0,-1,-1,-1};

        for (int i = 0; i < rowSteps.length; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + rowSteps[i], myPosition.getColumn() + colSteps[i]);//update position
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            if (newPosition.getRow() > 8 || newPosition.getColumn() > 8 || newPosition.getRow() < 1 || newPosition.getColumn() < 1) {//Off the board
                // Off the edge, don't add
            } else if (board.getPiece(newPosition) != null) {//if a piece is on newPosition
                if (board.getPiece(newPosition).getTeamColor() != color) { // if the piece is enemy color
                    moveSet.add(newMove); //capture and add possible space
                }
            } else { // if normal legal space add it to hashset
                moveSet.add(newMove);
            }
        }
        return moveSet;
    }



    private HashSet<ChessMove> queenMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal
        int [] rowSteps = {1,1,-1,-1,1,0,-1,0};
        int [] colSteps = {1,-1,1,-1,0,1,0,-1};

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
                        moveSet.add(newMove); //capture and add possible space
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
                        moveSet.add(newMove); //capture and add possible space
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


    private HashSet<ChessMove> knightMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal
        int [] rowSteps = {2,2,1,-1,-2,-2,1,-1};//starting at forward checking loop around king
        int [] colSteps = {1,-1,2,2,1,-1,-2,-2};

        for (int i = 0; i < rowSteps.length; i++) {
            newPosition = new ChessPosition(myPosition.getRow() + rowSteps[i], myPosition.getColumn() + colSteps[i]);//update position
            ChessMove newMove = new ChessMove(myPosition, newPosition, null);
            if (newPosition.getRow() > 8 || newPosition.getColumn() > 8 || newPosition.getRow() < 1 || newPosition.getColumn() < 1) {//Off the board
                //break;
            } else if (board.getPiece(newPosition) != null) {//if a piece is on newPosition
                if (board.getPiece(newPosition).getTeamColor() != color) { // if the piece is enemy color
                    moveSet.add(newMove); //capture and add possible space
                }
            } else { // if normal legal space add it to hashset
                moveSet.add(newMove);
            }
        }
        return moveSet;
    }


    private HashSet<ChessMove> rookMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal
        int [] rowSteps = {1,0,-1,0};//forward, right, back, left
        int [] colSteps = {0,1,0,-1};

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
                        moveSet.add(newMove); //capture and add possible space
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


    private HashSet<ChessMove> whitePawnMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal

        // Open space in front (includes enemy king row to promotion and move two off start position)
        newPosition = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn());
        if (board.getPiece(newPosition) == null){
            if (myPosition.getRow() == 2) { // Move forward one or two
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove); // Add one space forward
                newPosition = new ChessPosition(myPosition.getRow()+2,myPosition.getColumn());
                if (board.getPiece(newPosition) == null) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    moveSet.add(newMove); // Add two spaces forward
                }
            } else if (newPosition.getRow() == 8) { // Move forward with promotion
                pawnPromotionAdded(moveSet,newPosition);
            } else { // Move forward without promotion
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove);
            }
        }

        //Forward right with enemy
        pawnCapture(moveSet,1,1, ChessGame.TeamColor.BLACK);

        //Forward left with enemy
        pawnCapture(moveSet,1,-1, ChessGame.TeamColor.BLACK);

        return moveSet;
    }

    private HashSet<ChessMove> blackPawnMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        ChessPosition newPosition;// ChessPosition(myPosition.getRow(),myPosition.getColumn());//create new position one up and diagonal

        // Open space in front (includes enemy king row to promotion and move two off start position)
        newPosition = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn());
        if (board.getPiece(newPosition) == null){
            if (myPosition.getRow() == 7) { // Move forward one or two
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove); // Add one space forward
                newPosition = new ChessPosition(myPosition.getRow()-2,myPosition.getColumn());
                if (board.getPiece(newPosition) == null) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    moveSet.add(newMove); // Add two spaces forward
                }
            } else if (newPosition.getRow() == 1) { // Move forward with promotion
                pawnPromotionAdded(moveSet,newPosition);
            } else { // Move forward without promotion
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove);
            }
        }

        //Forward right with enemy
        pawnCapture(moveSet,-1,-1, ChessGame.TeamColor.WHITE);

        //Forward left with enemy
        pawnCapture(moveSet,-1,1, ChessGame.TeamColor.WHITE);

        return moveSet;
    }

    private void pawnCapture(HashSet<ChessMove> moveSet, int rowMove, int colMove, ChessGame.TeamColor enemyColor) {

        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + rowMove,myPosition.getColumn() + colMove);
        if (board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() == enemyColor) {
                if (newPosition.getRow() == 1) { // King row
                    pawnPromotionAdded(moveSet,newPosition);
                } else { // Non king row
                    ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                    moveSet.add(newMove);
                }
            }
        }
    }

//    private void pawnAdvance(HashSet<ChessMove> moveSet, int rowMove, int colMove, ChessGame.TeamColor enemyColor) {
//
//    }

    private void pawnPromotionAdded(HashSet<ChessMove> move, ChessPosition position){
        ChessPiece.PieceType[] pieceTypesList = {ChessPiece.PieceType.ROOK,ChessPiece.PieceType.BISHOP,ChessPiece.PieceType.QUEEN,ChessPiece.PieceType.KNIGHT};//ChessPiece.PieceType.values();
        for (ChessPiece.PieceType pieceType : pieceTypesList) {
            ChessMove newMove = new ChessMove(myPosition, position, pieceType);
            move.add(newMove);
        }
    }

}
