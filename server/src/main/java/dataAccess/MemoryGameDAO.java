package dataAccess;

import chess.ChessGame;
import dataAccess.Exceptions.BadRequestException;
import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, GameData> gameDB;
    private static int gameIDIncrementer = 0;

    // Static block to initialize the HashMap for testing
    static {
        gameDB = new HashMap<>();
    }
    @Override
    public int createGame(String name){
        //Increment the static gameID counter variable
        gameIDIncrementer += 1;

        //Create new game in the DB
        ChessGame newGame = new ChessGame();
        GameData gd = new GameData(gameIDIncrementer, null, null, name, newGame);
        gameDB.put(gameIDIncrementer, gd);

        //Return the game ID
        return gameIDIncrementer;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
        //Return game data information given the gameID if it exists, otherwise throw bad request exception
        if(!gameDB.containsKey(gameID)){
            throw new BadRequestException("Game does not exist");
        }
        return gameDB.get(gameID);
    }

    @Override
    public GameData[] listGames() {
        return gameDB.values().toArray(new GameData[0]);
    }

    @Override
    public void updateGame(int gameID, String color, String username) {
        //get new and old game data fields
        GameData oldGame = gameDB.get(gameID);
        GameData gd;

        //Add the new username to the color indicated
        if(color.equals("WHITE")){
            gd = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        }
        else{
            gd = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        }
        gameDB.put(gameID, gd);
    }


    public void clear() {
        //Delete all game DB elements
        gameDB.clear();
    }

//    public int getDBSize(){
//        if(gameDB != null) {
//            return gameDB.size();
//        }
//        else {
//            return 0;
//        }
//    }
}
