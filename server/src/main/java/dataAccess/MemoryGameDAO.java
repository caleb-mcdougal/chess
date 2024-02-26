package dataAccess;

import chess.ChessGame;
import dataAccess.Exceptions.BadRequestException;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, GameData> GameDB;
    private static int GameIDIncrementer = 0;

    // Static block to initialize the HashMap for testing
    static {
        GameDB = new HashMap<>();
    }
    @Override
    public int createGame(String name){
        //Increment the static gameID counter variable
        GameIDIncrementer += 1;

        //Create new game in the DB
        ChessGame newGame = new ChessGame();
        GameData gd = new GameData(GameIDIncrementer, null, null, name, newGame);
        GameDB.put(GameIDIncrementer, gd);

        //Return the game ID
        return GameIDIncrementer;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        //Return game data information given the gameID if it exists, otherwise throw bad request exception
        if(!GameDB.containsKey(gameID)){
            throw new BadRequestException("Game does not exist");
        }
        return GameDB.get(gameID);
    }

    @Override
    public GameData[] listGames() {
        return GameDB.values().toArray(new GameData[0]);
    }

    @Override
    public void updateGame(int gameID, String color, String username) {
        //get new and old game data fields
        GameData oldGame = GameDB.get(gameID);
        GameData gd;

        //Add the new username to the color indicated
        if(color.equals("WHITE")){
            gd = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        }
        else{
            gd = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        }
        GameDB.put(gameID, gd);
    }


    public void clear() {
        //Delete all game DB elements
        GameDB.clear();
    }

    public int getDBSize(){
        if(GameDB != null) {
            return GameDB.size();
        }
        else {
            return 0;
        }
    }
}
