package model.Request;

import chess.ChessPiece;

public record JoinGameRequest(String playerColor, int gameID) {
}
