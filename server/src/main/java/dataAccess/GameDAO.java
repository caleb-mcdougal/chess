package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    public void clear();
    public int createGame(String name);
    public GameData getGame(int gameID)throws BadRequestException;
    public String listGames();
    public void updateGame(int gameID, String color, String username);
}
