package dataAccess;

import dataAccess.Exceptions.BadRequestException;
import dataAccess.GameDAO;
import model.GameData;

public class SQLGameDAO extends SQLDAOParent implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String name) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        return null;
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String color, String username) {

    }
}
