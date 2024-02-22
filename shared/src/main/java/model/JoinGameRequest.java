package model;

import chess.ChessPiece;

public record JoinGameRequest(String playerColor, String gameID) {
}
