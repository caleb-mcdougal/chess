package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    public void clear();
    public int createGame(String name);
    public GameData getGame(int gameID);
    public String listGames();
    public void updateGame(String gameID);
}
