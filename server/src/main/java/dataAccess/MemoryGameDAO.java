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
    public ChessGame getGame() {
        return null;
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void updateGame(String gameID) {

    }


    public void clear() {
//        System.out.println("in MemoryUserDAO clear");
//        System.out.println(UserDB);
        GameDB.clear();
//        System.out.println(UserDB);
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
