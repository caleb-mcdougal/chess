package ui;

import Exceptions.ResponseException;
import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
//import ChessPiece.java;


import static ui.EscapeSequences.*;

public class ChessBoardPrinter {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    private String[][] BOARD;// =
//            {{ " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R " },
//             { " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
//                    { " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R " }};

    private String[][] PIECE_COLORS;// =
//            {{ " B ", " B ", " B ", " B ", " B ", " B ", " B ", " B "},
//                    { " B ", " B ", " B ", " B ", " B ", " B ", " B ", " B "},
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { null, null, null, null, null, null, null, null },
//                    { " W ", " W ", " W ", " W ", " W ", " W ", " W ", " W "},
//                    { " W ", " W ", " W ", " W ", " W ", " W ", " W ", " W "}};


    private static final String[] EDGE = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };

    private static final String[] HEADER = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String EMPTY = "   ";

    //Confused about static signature required on methods if i change this main to be static
//    public void main(String[] args) {
//        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//
//        out.print(ERASE_SCREEN);
//
//        drawBoardWhite(out);
//        drawBoardBlack(out);
//
//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
//    }

    public ChessBoardPrinter(ChessBoard BOARD){
//        System.out.println(BOARD.toString());
        this.BOARD = BoardToStringListPieces(BOARD);
        this.PIECE_COLORS = BoardToStringListColors(BOARD);
    }

    private String[][] BoardToStringListPieces(ChessBoard board){
//        System.out.println("herebts");

        ChessPiece[][] pieces = board.getBoard();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if(pieces[i][j] != null) {
//                    System.out.println(pieces[i][j].toString());
//                }
//                else{
//                    System.out.println("null");
//                }
//            }
//        }
//        System.out.println("herebts");
        String[][] pieceType = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

//                System.out.println(pieces[i][j].getPieceType().toString());
//                System.out.println("herebtsloop");
                if (pieces[i][j] != null) {
                    switch (pieces[i][j].getPieceType()) {
                        case ROOK -> pieceType[i][j] = " R ";
                        case KNIGHT -> pieceType[i][j] = " N ";
                        case BISHOP -> pieceType[i][j] = " B ";
                        case QUEEN -> pieceType[i][j] = " Q ";
                        case KING -> pieceType[i][j] = " K ";
                        case PAWN -> pieceType[i][j] = " P ";
                        default -> pieceType[i][j] = null;
                    }
                }
                else{
                    pieceType[i][j] = null;
                }
//                System.out.println("herebtsloop");
            }
        }
//        System.out.println("herebts end");
        return pieceType;
    }

    private String[][] BoardToStringListColors(ChessBoard board){
//        System.out.println("here colors");
        ChessPiece[][] pieces = board.getBoard();
        String[][] pieceColor = new String[8][8];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                if (pieces[i][j] != null) {
                    switch (pieces[i][j].getTeamColor()) {
                        case BLACK -> pieceColor[i][j] = " B ";
                        case WHITE -> pieceColor[i][j] = " W ";
                        default -> pieceColor[i][j] = null;
                    }
                }
                else{
                    pieceColor[i][j] = null;
                }
            }
        }
        return pieceColor;
    }

    public void printBoards(String teamColor) {
//        System.out.println("here printBoards");
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

//        System.out.println("team color:");
//        System.out.println(teamColor);
//        System.out.println(Objects.requireNonNullElse(teamColor, "null"));

        out.print(ERASE_SCREEN);
        out.println();
        if (teamColor == null || teamColor.equalsIgnoreCase("WHITE")) {
            drawBoardWhite(out,new boolean[8][8]);
        }
        else{
            drawBoardBlack(out,new boolean[8][8]);
        }

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void printHighlight(String teamColor, String startString, ChessGame game) throws ResponseException {
        boolean[][] highlightBoard = positionToHighlightBoard(startString, game);
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.println();
        if (teamColor == null || teamColor.equalsIgnoreCase("WHITE")) {
            drawBoardWhite(out,highlightBoard);
        }
        else{
            drawBoardBlack(out,highlightBoard);
        }

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private boolean[][] positionToHighlightBoard(String start, ChessGame game) throws ResponseException {
        if(start.length() != 2){
            throw new ResponseException(400, "Expected ROW: <1-8> COLUMN: <a-h> of start and end positions");
        }
        int startRow = 9;
        int startCol = 9;
        String[] letterString = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int i = 0; i < letterString.length; i++) {
            if(start.substring(0, 1).toLowerCase().equals(letterString[i])){
                startCol = i+1;
            }
            if(Integer.parseInt(start.substring(1)) == i+1){
                startRow = i+1;
            }
        }
        if(startRow == 9 || startCol == 9){
            throw new ResponseException(400, "Invalid positions");
        }
        ChessPosition startPosition = new ChessPosition(startRow,startCol);
        HashSet<ChessMove> validMoves = game.validMoves(startPosition);
        boolean[][] highlightBoard = new boolean[8][8];
        for(ChessMove move:validMoves){
            highlightBoard[move.getEndPosition().getRow()][move.getEndPosition().getColumn()] = true;
        }

        return highlightBoard;
    }


    private void drawBoardWhite(PrintStream out, boolean[][] highlightBoard) {
        drawHeaders(out, " W ");
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[7 - i], BOARD[7 - i], PIECE_COLORS[7 - i], counter, "WHITE", highlightBoard[7-i]);
            counter += 1;
        }
        drawHeaders(out, " W ");
    }

    private void drawBoardBlack(PrintStream out, boolean[][] highlightBoard) {
        drawHeaders(out, " B ");
        int counter = 1;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[i], BOARD[i], PIECE_COLORS[i], counter, "BLACK", highlightBoard[i]);
            counter += 1;
        }

        drawHeaders(out, " B ");
    }

    private static void drawHeaders(PrintStream out, String color) {
        setEDGE(out);
        out.print(EMPTY);

        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; ++i) {
            if (Objects.equals(color, " W ")) {
                printHeaderText(out, HEADER[i]);
            }
            else{
                printHeaderText(out, HEADER[7-i]);
            }
        }

        setEDGE(out);
        out.print(EMPTY);
        setBlack(out);
        out.println();
    }


    private static void printHeaderText(PrintStream out, String player) {
        setEDGE(out);
        out.print(player);
        setBlack(out);
    }

    private static void drawRow(PrintStream out, String rowNum, String[] boardRow, String[] colorRow, int counter, String color, boolean[] positionHighlight) {
        drawEdge(out, rowNum);
        if (Objects.equals(color, "WHITE")) {
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                if (counter % 2 == 0) {
                    if(positionHighlight[i]){
                        out.print(SET_BG_COLOR_GREEN);
                        printPiece(out, boardRow[i], colorRow[i]);
                    }
                    else {
                        out.print(SET_BG_COLOR_WHITE);
                        printPiece(out, boardRow[i], colorRow[i]);
                    }
                } else {
                    if(positionHighlight[i]) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                        printPiece(out, boardRow[i], colorRow[i]);
                    }
                    else{
                        out.print(SET_BG_COLOR_BLACK);
                        printPiece(out, boardRow[i], colorRow[i]);
                    }
                }
                counter += 1;
            }
        }
        else{
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                if (counter % 2 == 0) {
                    if(positionHighlight[7-i]) {
                        out.print(SET_BG_COLOR_GREEN);
                        printPiece(out, boardRow[7 - i], colorRow[7 - i]);
                    }
                    else{
                        out.print(SET_BG_COLOR_WHITE);
                        printPiece(out, boardRow[7 - i], colorRow[7 - i]);
                    }
                } else {
                    if(positionHighlight[i]) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                        printPiece(out, boardRow[7 - i], colorRow[7 - i]);
                    }
                    else{
                        out.print(SET_BG_COLOR_BLACK);
                        printPiece(out, boardRow[7 - i], colorRow[7 - i]);
                    }
                }
                counter += 1;
            }
        }
        drawEdge(out, rowNum);
        setBlack(out);
        out.println();
    }

    private static void drawEdge(PrintStream out, String rowNum) {
        setEDGE(out);
        out.print(rowNum);
    }

    private static void printPiece(PrintStream out, String piece, String color){
        if (piece == null){
            out.print("   ");
        }
        else{
            if (Objects.equals(color, " W ")) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(piece);
            }
            else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(piece);
            }
        }
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setEDGE(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);
    }



}
