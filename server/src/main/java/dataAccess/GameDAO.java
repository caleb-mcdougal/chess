package dataAccess;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import model.GameData;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public int createGame(String name);
    public GameData getGame(int gameID)throws BadRequestException;
    public GameData[] listGames();
    public void updateGame(int gameID, String color, String username);
}
