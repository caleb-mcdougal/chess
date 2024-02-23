package dataAccess;

import chess.ChessGame;

public interface GameDAO {
    public void clear();
    public int createGame(String name);
    public ChessGame getGame();
    public ChessGame[] listGames();
    public void updateGame(String gameID);
}
