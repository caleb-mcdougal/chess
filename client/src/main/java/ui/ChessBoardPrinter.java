package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
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
            drawBoardWhite(out);
        }
        else{
            drawBoardBlack(out);
        }

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void printHighlight() {

    }


    private void drawBoardWhite(PrintStream out) {
        drawHeaders(out, " W ");
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[7-i], BOARD[7-i], PIECE_COLORS[7-i], counter, "WHITE");
            counter += 1;
        }
        drawHeaders(out, " W ");
    }

    private void drawBoardBlack(PrintStream out) {
        drawHeaders(out, " B ");
        int counter = 1;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[i], BOARD[i], PIECE_COLORS[i], counter, "BLACK");
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

    private static void drawRow(PrintStream out, String rowNum, String[] boardRow, String[] colorRow, int counter, String color) {
        drawEdge(out, rowNum);
        if (Objects.equals(color, "WHITE")) {
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                if (counter % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                    printPiece(out, boardRow[i], colorRow[i]);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                    printPiece(out, boardRow[i], colorRow[i]);
                }
                counter += 1;
            }
        }
        else{
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                if (counter % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                    printPiece(out, boardRow[7-i], colorRow[7-i]);
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                    printPiece(out, boardRow[7-i], colorRow[7-i]);
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
