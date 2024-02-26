package dataAccess;

import dataAccess.Exceptions.BadRequestException;
import model.GameData;

public interface GameDAO {
    public void clear();
    public int createGame(String name);
    public GameData getGame(int gameID)throws BadRequestException;
    public GameData[] listGames();
    public void updateGame(int gameID, String color, String username);
}
