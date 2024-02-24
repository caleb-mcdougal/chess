package dataAccess;

import chess.ChessGame;
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
        GameIDIncrementer += 1;
        ChessGame newGame = new ChessGame();
        GameData gd = new GameData(GameIDIncrementer, "", "", name, newGame);
        GameDB.put(GameIDIncrementer, gd);
        return GameIDIncrementer;
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException{
        if(!GameDB.containsKey(gameID)){
            throw new BadRequestException("Game does not exist");
        }
        return GameDB.get(gameID);
    }

    @Override
    public String listGames() {
        StringBuilder result = new StringBuilder();
        for (GameData value : GameDB.values()) {
            result.append(value).append("\n");
        }
        return result.toString();
    }

    @Override
    public void updateGame(int gameID, String color, String username) {
        GameData oldGame = GameDB.get(gameID);
        GameData gd;
        if(color.equals("WHITE")){
            gd = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        }
        else{
            gd = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        }
        GameDB.put(gameID, gd);
    }


    public void clear() {
        GameDB.clear();
    }

    public int getDBSize(){
//        System.out.println(UserDB);
        if(GameDB != null) {
            return GameDB.size();
        }
        else {
            return 0;
        }
    }
}
