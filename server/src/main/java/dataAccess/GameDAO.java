package dataAccess;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import model.GameData;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public int createGame(String name) throws DataAccessException;
    public GameData getGame(int gameID)throws BadRequestException, DataAccessException;
    public GameData[] listGames();
    public void updateGame(int gameID, String color, String username);
}
