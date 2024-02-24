package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    public void clear();
    public int createGame(String name);
    public ChessGame getGame();
    public GameData[] listGames();
    public void updateGame(String gameID);
}
