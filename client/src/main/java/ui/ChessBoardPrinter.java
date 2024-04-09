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

    private static String[][] BOARD =
            {{ " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R " },
             { " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { " P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
                    { " R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R " }};

    private static String[][] PIECE_COLORS =
            {{ " B ", " B ", " B ", " B ", " B ", " B ", " B ", " B "},
                    { " B ", " B ", " B ", " B ", " B ", " B ", " B ", " B "},
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { null, null, null, null, null, null, null, null },
                    { " W ", " W ", " W ", " W ", " W ", " W ", " W ", " W "},
                    { " W ", " W ", " W ", " W ", " W ", " W ", " W ", " W "}};


    private static final String[] EDGE = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };

    private static final String[] HEADER = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static final String EMPTY = "   ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoardWhite(out);
        drawBoardBlack(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

//    public ChessBoardPrinter(ChessBoard BOARD){
//        String[][] board = BoardToStringList(BOARD);
//        this.BOARD = board;
//    }

    private String[][] BoardToStringList(ChessBoard board){
        ChessPiece[][] pieces = board.getBoard();
        String[][] strings = new String[8][8];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces.length; j++) {
                switch (pieces[i][j].getPieceType()){
                    case ROOK -> strings[i][j] = " R ";
//                    case ROOK -> strings[i][j] = " R ";
//                    case ROOK -> strings[i][j] = " R ";
//                    case ROOK -> strings[i][j] = " R ";
//                    case ROOK -> strings[i][j] = " R ";
//                    case ROOK -> strings[i][j] = " R ";
                }
            }
        }
        return strings;
    }

    public void printBoards() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

//        out.print(ERASE_SCREEN);

        drawBoardWhite(out);
        drawBoardBlack(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void printHighlight() {

    }


    private static void drawBoardWhite(PrintStream out) {
        drawHeaders(out, " W ");
        int counter = 0;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[7-i], BOARD[7-i], PIECE_COLORS[7-i], counter);
            counter += 1;
        }
        drawHeaders(out, " W ");
    }

    private static void drawBoardBlack(PrintStream out) {
        drawHeaders(out, " B ");
        int counter = 1;
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            drawRow(out, EDGE[i], BOARD[i], PIECE_COLORS[i], counter);
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

    private static void drawRow(PrintStream out, String rowNum, String[] boardRow, String[] colorRow, int counter) {
        drawEdge(out, rowNum);
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            if (counter % 2 == 0){
                out.print(SET_BG_COLOR_WHITE);
                printPiece(out, boardRow[i], colorRow[i]);
            }
            else{
                out.print(SET_BG_COLOR_BLACK);
                printPiece(out, boardRow[i], colorRow[i]);
            }
            counter += 1;
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
