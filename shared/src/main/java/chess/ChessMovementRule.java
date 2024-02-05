package chess;

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
        int [] rowSteps = {1,1,0,-1,-1,-1,0,1};//starting at forward checking loop around king
        int [] colSteps = {0,1,1,1,0,-1,-1,-1};
        singleStepMovement(moveSet,rowSteps,colSteps);
        return moveSet;
    }

    private HashSet<ChessMove> queenMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int [] rowSteps = {1,1,-1,-1,1,0,-1,0};
        int [] colSteps = {1,-1,1,-1,0,1,0,-1};
        pathMovement(moveSet,rowSteps,colSteps);
        return moveSet;
    }

    private HashSet<ChessMove> bishopMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int [] rowSteps = {1,1,-1,-1};//forward-right, forward-left, backward-right, backward-left
        int [] colSteps = {1,-1,1,-1};
        pathMovement(moveSet,rowSteps,colSteps);
        return moveSet;
    }

    private HashSet<ChessMove> knightMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int [] rowSteps = {2,2,1,-1,-2,-2,1,-1};//starting at forward checking loop around king
        int [] colSteps = {1,-1,2,2,1,-1,-2,-2};
        singleStepMovement(moveSet,rowSteps,colSteps);
        return moveSet;
    }

    private HashSet<ChessMove> rookMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();
        int [] rowSteps = {1,0,-1,0};//forward, right, back, left
        int [] colSteps = {0,1,0,-1};
        pathMovement(moveSet,rowSteps,colSteps);
        return moveSet;
    }

    private HashSet<ChessMove> whitePawnMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();

        //move forward
        pawnAdvance(moveSet,1,2,2,8);

        //Forward right with enemy
        pawnCapture(moveSet,1,1, ChessGame.TeamColor.BLACK,8);

        //Forward left with enemy
        pawnCapture(moveSet,1,-1, ChessGame.TeamColor.BLACK,8);

        return moveSet;
    }

    private HashSet<ChessMove> blackPawnMoves(){
        HashSet<ChessMove> moveSet = new HashSet<>();

        //move forward
        pawnAdvance(moveSet,-1,7,-2,1);

        //Forward right with enemy
        pawnCapture(moveSet,-1,-1, ChessGame.TeamColor.WHITE,1);

        //Forward left with enemy
        pawnCapture(moveSet,-1,1, ChessGame.TeamColor.WHITE,1);

        return moveSet;
    }

    private void singleStepMovement(HashSet<ChessMove> moveSet, int [] rowSteps, int [] colSteps) {
        ChessPosition newPosition;
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
    }

    private void pathMovement(HashSet<ChessMove> moveSet, int [] rowSteps, int [] colSteps){
        ChessPosition newPosition;
        for (int i = 0; i < rowSteps.length; i++) {
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
    }

    private void pawnCapture(HashSet<ChessMove> moveSet, int rowMove, int colMove, ChessGame.TeamColor enemyColor, int kingRow) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + rowMove,myPosition.getColumn() + colMove);
        //piece present
        if (newPosition.getColumn() >= 1 && newPosition.getColumn() <= 8){
            //on the board
            if(board.getPiece(newPosition) != null){
                //enemy piece
                if (board.getPiece(newPosition).getTeamColor() == enemyColor) {
                    if (newPosition.getRow() == kingRow) { // King row
                        pawnPromotionAdded(moveSet, newPosition);
                    } else { // Non king row
                        ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                        moveSet.add(newMove);
                    }
                }
            }
        }
    }

    private void pawnAdvance(HashSet<ChessMove> moveSet, int forward, int startPosition, int move2, int kingRow) {
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + forward,myPosition.getColumn());
        if (board.getPiece(newPosition) == null){
            if (myPosition.getRow() == startPosition) { // Move forward one or two
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove); // Add one space forward
                newPosition = new ChessPosition(myPosition.getRow() + move2,myPosition.getColumn());
                if (board.getPiece(newPosition) == null) {
                    newMove = new ChessMove(myPosition, newPosition, null);
                    moveSet.add(newMove); // Add two spaces forward
                }
            } else if (newPosition.getRow() == kingRow) { // Move forward with promotion
                pawnPromotionAdded(moveSet,newPosition);
            } else { // Move forward without promotion
                ChessMove newMove = new ChessMove(myPosition, newPosition, null);
                moveSet.add(newMove);
            }
        }
    }

    private void pawnPromotionAdded(HashSet<ChessMove> move, ChessPosition position){
        ChessPiece.PieceType[] pieceTypesList = {ChessPiece.PieceType.ROOK,ChessPiece.PieceType.BISHOP,ChessPiece.PieceType.QUEEN,ChessPiece.PieceType.KNIGHT};//ChessPiece.PieceType.values();
        for (ChessPiece.PieceType pieceType : pieceTypesList) {
            ChessMove newMove = new ChessMove(myPosition, position, pieceType);
            move.add(newMove);
        }
    }

}
